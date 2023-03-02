package com.cyrildever.feistel.common.utils.hex

import com.cyrildever.BasicUnitSpecs

/**
 * HexSpecs test class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
class HexSpecs extends BasicUnitSpecs {

  "Hex.stringToSomeByteArray" should "return the appropriate byte array" in {
    val hexString = "123abc"

    val found = Hex.stringToSomeByteArray(hexString)
    found.isEmpty shouldBe false
    found.get should equal (Seq(18, 58, -68))
  }
}
