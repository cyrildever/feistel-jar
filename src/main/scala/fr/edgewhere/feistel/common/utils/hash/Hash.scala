package fr.edgewhere.feistel.common.utils.hash

import fr.edgewhere.feistel.common.utils.hex.Hex

/**
 * Hash type
 *
 * @author  Cyril Dever
 * @since   2.0
 * @version 1.0
 */
object Hash {
  type Hash = Seq[Byte]

  implicit class HashOps(h: Hash) {
    def toHex: String = h.map(b => f"$b%02x").mkString.toLowerCase
  }

  def fromHex(str: String): Hash = Hex.stringToSomeByteArray(str).getOrElse(Seq.empty)
}
