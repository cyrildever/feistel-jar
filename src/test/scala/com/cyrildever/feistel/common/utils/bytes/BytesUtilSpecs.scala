package com.cyrildever.feistel.common.utils.bytes

import com.cyrildever.BasicUnitSpecs
import com.cyrildever.feistel.common.utils.bytes.BytesUtil._

/**
 * BytesUtilSpecs test class
 *
 * @author  Cyril Dever
 * @since   1.4
 * @version 2.0
 */
class BytesUtilSpecs extends BasicUnitSpecs {

  "BytesUtil.add" should "be deterministic" in {
    val ref = Array[Byte](4, 6)
    val ab = Array[Byte](1, 2)
    val cd = Array[Byte](3, 4)
    val found = addBytes(ab, cd)
    found should equal(ref)
  }
  "BytesUtil.extractBytes" should "be deterministic" in {
    val ref = "s is a testThis is a tes".getBytes
    var found = extractBytes("This is a test".getBytes, 3, 24)
    found should equal(ref)

    val expected = "1234abcd12".getBytes
    found = extractBytes("abcd1234".getBytes, 4, 10)
    found should equal(expected)
  }
  "BytesUtil.splitBytes" should "be deterministic" in {
    val left = "edge"
    val right = "where"
    val edgewhere = left + right
    val (leftBytes, rightBytes) = splitBytes(edgewhere.getBytes)
    leftBytes should equal (left.getBytes)
    rightBytes should equal (right.getBytes)
  }
  "BytesUtil.xorBytes" should "be deterministic" in {
    val expected = "PPPP".getBytes
    val found = xorBytes("1234".getBytes, "abcd".getBytes)
    found should equal(expected)
  }
  "BytesUtil.printString" should "return the right string" in {
    val expected = "[123 45]"
    val found = BytesUtil.printString(Array[Byte](123.toByte, 45.toByte))
    found should equal(expected)
  }
}
