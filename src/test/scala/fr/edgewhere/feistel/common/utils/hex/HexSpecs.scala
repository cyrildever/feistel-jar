package fr.edgewhere.feistel.common.utils.hex

import fr.edgewhere.BasicUnitSpecs

/**
 * HexSpecs test class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.4
 */
class HexSpecs extends BasicUnitSpecs {

  "Hex.stringToSomeByteArray" should "return the appropriate byte array" in {
    val hexString = "123abc"

    val found = Hex.stringToSomeByteArray(hexString)
    found.isEmpty shouldBe false
    found.get should equal (Seq(18, 58, -68))
  }
}
