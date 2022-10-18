package fr.edgewhere.feistel.common.utils.bytes

import fr.edgewhere.feistel.common.exception.NotSameLengthException
import java.nio.ByteBuffer

/**
 * BytesUtil
 *
 * @author  Cyril Dever
 * @since   1.4
 * @version 1.0
 */
object BytesUtil {
  def addBytes(bytes1: Array[Byte], bytes2: Array[Byte]): Array[Byte] =
    if (bytes1.length != bytes2.length) throw NotSameLengthException("to be added, byte arrays must be of the same length")
    else bytes1.zipWithIndex.foldLeft(Array[Byte]())((acc, item) => {
      val it1 = if (item._1.toInt < 0) item._1.toInt + 256 else item._1.toInt
      val it2 = if (bytes2(item._2).toInt < 0) bytes2(item._2).toInt + 256 else bytes2(item._2).toInt
      acc ++ bitValueToUtf8Bytes((it1 + it2) % 256)
    })

  def extractBytes(from: Array[Byte], startIndex: Int, desiredLength: Int): Array[Byte] = {
    val buf = ByteBuffer.allocate(desiredLength)
    val actualStartIndex = startIndex % from.length
    val lengthNeeded = actualStartIndex + desiredLength
    val firstPart = from.slice(actualStartIndex, Math.min(lengthNeeded + actualStartIndex, from.length))
    val repetitions = lengthNeeded / from.length + 1
    val base = if (repetitions > 1) List.fill(repetitions)(from).toArray.flatten else from
    val end = desiredLength - firstPart.length
    if (end > 0) buf.put(firstPart ++ base.slice(0, end)).array
    else buf.put(firstPart.slice(0, desiredLength)).array
  }

  def splitBytes(item: Array[Byte]): (Array[Byte], Array[Byte]) =
    if (item.length == 1) (Array[Byte](0.toByte), item)
    else {
      val half = item.length / 2
      (item.slice(0, half), item.slice(half, item.length))
    }

  def xorBytes(bytes1: Array[Byte], bytes2: Array[Byte]): Array[Byte] =
    if (bytes1.length != bytes2.length) throw NotSameLengthException("to be xored, byte arrays must be of the same length")
    else bytes1.zipWithIndex.map{ case (b1, idx) => (b1 ^ bytes2(idx)).toByte }

  private[BytesUtil] def bitValueToUtf8Bytes(value: Int): Array[Byte] = {
    (if (value < 128) Seq(value.toByte)
    else {
      if (value < 192) Seq(194.toByte, value.toByte)
      else Seq(195.toByte, (value - 64).toByte)
    }).toArray
  }

  /**
   * Display a byte array as its list of bit value in the range 0 to 255 included surrounded by brackets, eg. [44 123 5]
   * @param bytes The byte array to use
   * @return the string representation of the passed byte array
   */
  def printString(bytes: Array[Byte]): String = "[" + bytes.map(c => if (c.toInt < 0) c.toInt + 256 else c.toInt).mkString(" ") + "]"
}
