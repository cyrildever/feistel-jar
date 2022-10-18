package fr.edgewhere.feistel.common.utils.hash

import fr.edgewhere.feistel.common.exception.UnknownEngineException
import java.security.Security
import org.bouncycastle.jcajce.provider.digest.SHA3._
import scorex.crypto.hash._

/**
 * Engine type and utilities
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.2
 */
object Engine {
  import Hash._
  Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider())

  type Engine = String

  val BLAKE2b: Engine = "blake-2b-256"
  val KECCAK: Engine = "keccak-256"
  val SHA_256: Engine = "sha-256"
  val SHA_3: Engine = "sha3-256"

  def isAvailable(engine: String): Boolean =
    engine == BLAKE2b || engine == KECCAK || engine == SHA_256 || engine == SHA_3 // TODO Enrich at each new engine

  def hash(input: String, using: Engine): Hash = using match {
    case BLAKE2b =>
      Blake2b256.hash(input)
    case KECCAK =>
      Keccak256.hash(input)
    case SHA_256 =>
      Sha256.hash(input)
    case SHA_3 =>
      val sha3 = new Digest256()
      sha3.update(input.getBytes)
      sha3.digest
    case _ => throw UnknownEngineException()
  }

  def hashBytes(input: Array[Byte], using: Engine): Hash = using match {
    case BLAKE2b =>
      Blake2b256.hash(input)
    case KECCAK =>
      Keccak256.hash(input)
    case SHA_256 =>
      Sha256.hash(input)
    case SHA_3 =>
      val sha3 = new Digest256()
      sha3.update(input)
      sha3.digest
    case _ => throw UnknownEngineException()
  }
}
