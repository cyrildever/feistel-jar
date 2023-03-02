package com.cyrildever.feistel

import com.cyrildever.feistel.client._
import com.cyrildever.feistel.common.utils.hash.Engine._

/**
 * Main application entry point
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 * @example {{{
 *  $ sbt assembly && java -cp target/scala-2.12/feistel-jar-1.5.0.jar com.cyrildever.feistel.Main 'myWordToObfuscate'
 *  $ java -cp target/scala-2.12/feistel-jar-1.5.0.jar com.cyrildever.feistel.Main -d '!f^w=€¦-Hyrbq¡#bs'
 * }}}
 */
object Main extends App {
  try {
    // Prepare operation
    val config = Config.init(args = args)
    if (!config.checks) throw new Exception("Bad arguments: verify usage")
    val operation = if (config.decrypt) Operation.DECRYPT else Operation.ENCRYPT

    // Process operation
    val cipher = Feistel.FPECipher(config.hash.getOrElse(SHA_256), config.key.getOrElse(Config.DEFAULT_KEY), config.rounds.getOrElse(Config.DEFAULT_ROUNDS))
    val result = operation match {
      case Operation.ENCRYPT =>
        cipher.encrypt(config.input)
      case Operation.DECRYPT =>
        cipher.decrypt(config.input)
    }
    println(result)
  } catch {
    case e: Exception =>
      println(e)
      e.getStackTrace foreach println
      println
      println(Config.getUsage)
      System.exit(1)
  }
}
