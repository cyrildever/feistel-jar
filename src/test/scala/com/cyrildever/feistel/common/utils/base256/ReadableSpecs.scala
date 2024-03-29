package com.cyrildever.feistel.common.utils.base256

import com.cyrildever.BasicUnitSpecs
import com.cyrildever.feistel.common.utils.hex.Hex

/**
 * ReadableSpecs test class
 *
 * @author  Cyril Dever
 * @since   2.0
 * @version 2.0
 */
class ReadableSpecs extends BasicUnitSpecs {
  import Readable._

  "Readable" should "be deterministic" in {
    CHARSET should have length 256
    charAt(0) should equal ("!")
    charAt(255) should equal ("ǿ")
    charAt(12345) should equal ("")

    val ref: Readable = "g¨«©½¬©·©"
    val data = "Edgewhere"
    val b256 = Readable(data.getBytes)
    b256 should equal (ref)

    val fpeEdgewhere = Readable("K¡(#q|r5*")
    fpeEdgewhere.length should equal (9)
    val fpeBytes = Seq[Byte](42, 93, 7, 2, 79, 90, 80, 20, 9)
    fpeBytes should equal (Hex.stringToSomeByteArray("2a5d07024f5a501409").get)
    val fpeB256 = Readable(fpeBytes)
    fpeB256 should equal (fpeEdgewhere)
    fpeB256.length should equal (fpeEdgewhere.length)
    fpeB256.bytes should equal (fpeBytes)
    fpeB256.getBytes should not equal fpeB256.bytes

    fpeB256.toHex should equal ("2a5d07024f5a501409")
  }
}
