package com.cyrildever.feistel.common.utils.hex

/**
 * Hex utility
 *
 * @author  Cyril Dever
 * @since   1.1
 * @version 2.0
 */
object Hex {
  def byteArrayToHexString(bytes: Seq[Byte]): String = bytes.map(b => f"$b%02x").mkString.toLowerCase

  def stringToSomeByteArray(hex: String): Option[Seq[Byte]] = try {
    Some(hex.sliding(2,2).toArray.map(Integer.parseInt(_, 16).toByte))
  } catch {
    case _: Exception => None
  }
}
