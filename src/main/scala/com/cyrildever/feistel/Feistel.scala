package com.cyrildever.feistel

import com.cyrildever.feistel.common.exception.WrongCipherParametersException
import com.cyrildever.feistel.common.utils.base256.Readable
import com.cyrildever.feistel.common.utils.hash.Engine
import com.cyrildever.feistel.common.utils.bytes.BytesUtil._
import com.cyrildever.feistel.common.utils.strings.StringsUtil._
import com.cyrildever.feistel.common.utils.xor.Neutral
import com.cyrildever.feistel.common.utils.xor.Operation._
import Engine._
import Readable._
import java.nio.{ByteBuffer, ByteOrder}

/**
 * Feistel object and classes
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.1
 */
object Feistel {
  import com.cyrildever.feistel.common.utils.hash.Hash._

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

    def encryptNumber(n: Long): Long =
      if (self.key.isEmpty || self.rounds < 2 || !isAvailable(self.engine)) throw WrongCipherParametersException()
      else if (n == 0) 0
      else {
        val size = if (n > Math.pow(2,32) - 1) 8 else 4
        val (data, short) = if (n < 256) {
          (Array[Byte](n.toByte), true)
        } else {
          val buf = ByteBuffer.allocate(size)
          buf.order(ByteOrder.BIG_ENDIAN)
          if (size > 4) buf.putLong(n) else buf.putInt(n.toInt)
          (if (buf.array.head == 0) buf.array.slice(1, buf.array.length) else buf.array, false)
        }
        val bits = data.length
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
        val finalBytes = if (short) Array[Byte](0, 0) ++ left ++ right
          else if (bits < size) {
            var diff = size - bits
            var concat = left ++ right
            while (diff > 0) {
              concat = Array[Byte](0) ++ concat
              diff -= 1
            }
            concat
          }
          else left ++ right
        val res = ByteBuffer.allocate(size)
        res.order(ByteOrder.BIG_ENDIAN)
        res.put(finalBytes)
        if (size > 4) res.getLong(0) else res.getInt(0)
      }

    def encryptString(line: String): Readable = encrypt(line)

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

    def decryptNumber(ciphered: Long): Long =
      if (ciphered == 0) 0
      else {
        val size = if (ciphered > Math.pow(2,32) - 1) 8 else 4
        val buf = ByteBuffer.allocate(8)
        buf.order(ByteOrder.BIG_ENDIAN)
        buf.putLong(ciphered)
        // Apply the FPE Feistel cipher
        var (left, right) = splitBytes(buf.array.slice(8-size, 8))
        if (left(0).equals(0.toByte) && left(1).equals(0.toByte)) {
          // It's a small number
          left = Array[Byte](right(0))
          right = Array[Byte](right(1))
        }
        if (self.rounds % 2 != 0 && left.length != right.length) {
          left = left ++ Array[Byte](right(0))
          right = right.slice(1, right.length-1)
        }
        if (left.head == 0) left = left.slice(1, left.length)
        for (i <- 0 until self.rounds) {
          var (leftRound, rightRound) = (left, right)
          if (left.length < right.length) {
            leftRound = leftRound ++ Neutral(1).getBytes
          }
          right = left
          val rnd = roundBytes(leftRound, self.rounds - i - 1)
          val (tmp, extended) = if (rightRound.length + 1 == rnd.length) {
            rightRound = rightRound ++ left.slice(left.length - 1, left.length)
            (xorBytes(rightRound, rnd), true)
          } else {
            (xorBytes(rightRound, rnd), false)
          }
          left = if (extended) tmp.slice(0, tmp.length - 1) else tmp
        }
        var parts = left ++ right
        while (parts.length < size) {
          parts = Array[Byte](0) ++ parts
        }
        val res = ByteBuffer.allocate(size)
        res.order(ByteOrder.BIG_ENDIAN)
        res.put(parts)
        if (size > 4) res.getLong(0) else res.getInt(0)
      }

    def decryptString(ciphered: Readable): String = decrypt(ciphered)

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
