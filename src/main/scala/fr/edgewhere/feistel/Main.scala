package fr.edgewhere.feistel

import fr.edgewhere.feistel.client._

/**
 * Main application entry point
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 * @example {{{
 *  $ sbt assembly && java -cp target/scala-2.12/feistel-jar-0.1.0.jar fr.edgewhere.feistel.Main -i=myWordToObfuscate
 *  $ java -cp target/scala-2.12/feistel-jar-0.1.0.jar fr.edgewhere.feistel.Main -i=myObfuscatedWord -d
 * }}}
 */
object Main extends App {
  try {
    // Prepare operation
    val config = Config.init(args = args)
    if (!config.checks) throw new Exception("Bad arguments: verify usage")
    val operation = if (config.decrypt) Operation.DECRYPT else Operation.ENCRYPT

    // Process operation
//    val worker = Redacted(operation, config)
//    if (!worker.process) throw new Exception("Unable to process operation")
  } catch {
    case e: Exception =>
      println(e)
      e.getStackTrace foreach println
      println
      println(Config.getUsage)
      System.exit(1)
  }
}
