package fr.edgewhere.feistel

import fr.edgewhere.feistel.common.exception.WrongCipherParametersException
import fr.edgewhere.feistel.common.utils.base256.Readable
import fr.edgewhere.feistel.common.utils.base256.Readable._
import fr.edgewhere.feistel.common.utils.bytes.BytesUtil._
import fr.edgewhere.feistel.common.utils.hash.Engine
import fr.edgewhere.feistel.common.utils.hash.Engine._
import fr.edgewhere.feistel.common.utils.strings.StringsUtil._
import fr.edgewhere.feistel.common.utils.xor.Neutral
import fr.edgewhere.feistel.common.utils.xor.Operation._
import java.nio.{ByteBuffer, ByteOrder}

/**
 * Feistel object and classes
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.1
 */
object Feistel {
  import fr.edgewhere.feistel.common.utils.hash.Hash._

  final case class FPECipher(engine: Engine, key: String, rounds: Int) extends FormatPreservingEncryption {
    self =>

    override def encrypt(line: String): Readable =
      if (self.key.isEmpty || self.rounds < 2 || !isAvailable(self.engine)) throw WrongCipherParametersException()
      else if (line.isEmpty) line
      else {
        // Apply the FPE Feistel cipher
        var (left, right) = split(line)
        for (i <- 0 until self.rounds) {
          var (leftRound, rightRound) = (left, right)
          left = right
          if (rightRound.length < leftRound.length) {
            rightRound = rightRound + Neutral(1).toString
          }
          val rnd = round(rightRound, i)
          val (tmp, crop) = if (leftRound.length + 1 == rnd.length) {
            (leftRound + Neutral(1).toString, true)
          } else (leftRound, false)
          right = tmp ^ rnd
          if (crop) {
            right = right.substring(0, right.length-1)
          }
        }
        Readable(left.getBytes ++ right.getBytes)
      }

    def encryptNumber(n: Int): Int =
      if (self.key.isEmpty || self.rounds < 2 || !isAvailable(self.engine)) throw WrongCipherParametersException()
      else if (n == 0) 0
      else {
        val (data, short) = if (n < 256) {
          (Array[Byte](n.toByte), true)
        } else {
          val buf = ByteBuffer.allocate(4)
          buf.order(ByteOrder.BIG_ENDIAN)
          buf.putInt(n)
          (buf.array, false)
        }
        // Apply the FPE Feistel cipher
        var (left, right) = splitBytes(data)
        for (i <- 0 until self.rounds) {
          var (leftRound, rightRound) = (left, right)
          left = right
          if (rightRound.length < leftRound.length) {
            rightRound = rightRound ++ Neutral(1).getBytes
          }
          val rnd = roundBytes(rightRound, i)
          val (tmp, crop) = if (leftRound.length + 1 == rnd.length) {
            (leftRound ++ Neutral(1).getBytes, true)
          } else (leftRound, false)
          right = xorBytes(tmp, rnd)
          if (crop) {
            right = right.slice(0, right.length-1)
          }
        }
        val res = ByteBuffer.allocate(4)
        res.order(ByteOrder.BIG_ENDIAN)
        if (short) res.put(Array[Byte](0, 0) ++ left ++ right)
        else res.put(left ++ right)
        res.getInt(0)
      }

    override def decrypt(ciphered: Readable): String =
      if (self.key.isEmpty || self.rounds < 2 || !isAvailable(self.engine)) throw WrongCipherParametersException()
      else if (ciphered.isEmpty) ""
      else {
        // Apply the FPE Feistel cipher
        var (left, right) = split(ciphered.string)
        if (self.rounds % 2 != 0 && left.length != right.length) {
          left = left + right.substring(0, 1)
          right = right.substring(1)
        }
        for (i <- 0 until self.rounds) {
          var (leftRound, rightRound) = (left, right)
          if (left.length < right.length) {
            leftRound = leftRound + Neutral(1).toString
          }
          right = left
          val rnd = round(leftRound, self.rounds - i - 1)
          val (tmp, extended) = if (rightRound.length + 1 == rnd.length) {
            rightRound = rightRound + left.substring(left.length - 1)
            (rightRound ^ rnd, true)
          } else {
            (rightRound ^ rnd, false)
          }
          left = if (extended) tmp.substring(0, tmp.length - 1) else tmp
        }
        left + right
      }

    private[FPECipher] def round(item: String, index: Int): String = {

      val addition = add(item, extract(self.key, index, item.length))
      val hashed = Engine.hash(addition, self.engine)
      extract(hashed.toHex, index, item.length)
    }

    private[FPECipher] def roundBytes(item: Array[Byte], index: Int): Array[Byte] = {
      val addition = addBytes(item, extractBytes(self.key.getBytes, index, item.length))
      val hashed = Engine.hashBytes(addition, self.engine)
      extractBytes(hashed.toHex.getBytes, index, item.length)
    }
  }
}
