package com.cyrildever.feistel

import com.cyrildever.BasicUnitSpecs
import com.cyrildever.feistel.common.utils.base256.Readable
import com.cyrildever.feistel.common.utils.hash.Engine._
import Readable._

/**
 * FeistelSpecs test class
 */
class FeistelSpecs extends BasicUnitSpecs {
  "FPECipher.encrypt" should "be deterministic" in {
    import com.cyrildever.feistel.common.utils.base256.Readable._

    val expected = "K¡(#q|r5*"
    val cipher = Feistel.FPECipher(SHA_256, "8ed9dcc1701c064f0fd7ae235f15143f989920e0ee9658bb7882c8d7d5f05692", 10)
    val found = cipher.encrypt("Edgewhere")
    found.length should equal ("Edgewhere".length)
    found should equal (expected)
    found.toHex should equal ("2a5d07024f5a501409")

    val blake2 = Feistel.FPECipher(BLAKE2b, "8ed9dcc1701c064f0fd7ae235f15143f989920e0ee9658bb7882c8d7d5f05692", 10).encrypt("Edgewhere")
    blake2 should not equal (found)
    blake2 should equal ("¼u*$q0up¢")
  }
  "FPECipher.encryptNumber" should "produce the right number" in {
    val expected = 22780178
    val cipher = Feistel.FPECipher(SHA_256, "some-32-byte-long-key-to-be-safe", 128)
    val found = cipher.encryptNumber(123456789)
    found should equal (expected)

    val smallNumber = cipher.encryptNumber(123)
    smallNumber should equal (24359)

    val zero = cipher.encryptNumber(0)
    zero should equal(0)
  }
  "FPECipher.decrypt" should "be deterministic" in {
    val nonFPE = Readable("\u0002Edgewhere")
    val cipher = Feistel.FPECipher(SHA_256, "8ed9dcc1701c064f0fd7ae235f15143f989920e0ee9658bb7882c8d7d5f05692", 10)
    var found = cipher.decrypt(fromHex("3d7c0a0f51415a521054"))
    found should equal (nonFPE)

    val ref = "K¡(#q|r5*"
    val expected = "Edgewhere"
    found = cipher.decrypt(fromHex("2a5d07024f5a501409"))
    found should equal (expected)
    found = cipher.decrypt(ref)
    found should equal (expected)
    val b256 = fromHex("2a5d07024f5a501409")
    b256 should equal (ref)

    val fromBlake2 = Readable("¼u*$q0up¢")
    val blake2 = Feistel.FPECipher(BLAKE2b, "8ed9dcc1701c064f0fd7ae235f15143f989920e0ee9658bb7882c8d7d5f05692", 10).decrypt(fromBlake2)
    blake2 should equal (expected)
  }
  "FPECipher.decryptNumber" should "return the right number" in {
    val expected = 123456789
    val cipher = Feistel.FPECipher(SHA_256, "some-32-byte-long-key-to-be-safe", 128)
    val found = cipher.decryptNumber(22780178)
    found should equal (expected)

    val smallNumber = cipher.decryptNumber(24359)
    smallNumber should equal (123)

    val zero = cipher.decryptNumber(0)
    zero should equal(0)
  }
}
