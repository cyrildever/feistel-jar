package fr.edgewhere.feistel.common.utils.hex

import java.math.BigInteger

/**
 * Hex utility
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.4
 */
object Hex {
  def byteArrayToHexString(bytes: Seq[Byte]): String = bytes.map(b => f"$b%02x").mkString.toLowerCase

  def stringToSomeByteArray(str: String): Option[Seq[Byte]] = try {
    Some(new BigInteger(str, 16).toByteArray)
  } catch {
    case _: Exception => None
  }
}
