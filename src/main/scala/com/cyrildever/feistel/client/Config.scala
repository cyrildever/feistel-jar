package com.cyrildever.feistel.client

import Config._
import com.cyrildever.feistel.common.utils.hash.Engine._
import scopt.{DefaultOParserSetup, OParser, OParserBuilder}

/**
 * Config class for command-line parsing
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 *
 * @see Config.parser details for parameter definition
 */
final case class Config(
  input: String,
  decrypt: Boolean = false,
  hash: Option[Engine] = Some(SHA_256),
  rounds: Option[Int] = Some(DEFAULT_ROUNDS),
  key: Option[String]= Some(DEFAULT_KEY),
  output: Option[String] = None
) { self =>

  /**
   * @return  the feistel application full version name, eg. 1.5.0
   */
  lazy val appVersion: String = getClass.getPackage.getImplementationVersion

  /**
   * Verify that the passed options and data are valid for handling a full feistel process
   */
  def checks: Boolean = {
    val cipher = self.rounds.getOrElse(0) >= 2 && self.key.nonEmpty && isAvailable(self.hash.getOrElse(""))
    if (!cipher)
      println(s"""Invalid arguments [key=${self.key.getOrElse("")},hashEngine=${self.hash.getOrElse("")},round=${self.rounds}]""")

    cipher && !isEmpty
  }

  def isEmpty: Boolean = self.input.isEmpty
}
object Config {
  val DEFAULT_KEY = "d51e1d9a9b12cd88a1d232c1b8730a05c8a65d9706f30cdb8e08b9ed4c7b16a0"
  val DEFAULT_ROUNDS = 10

  private val EMPTY = Config("")

  private[Config] var _instance: Config = EMPTY

  /**
   * Initialize configuration using command-line arguments
   */
  def init(args: Array[String]): Config = {
    if (_instance.isEmpty) {
      OParser.parse(parser, args, EMPTY, new DefaultOParserSetup {
        override def showUsageOnError: Some[Boolean] = Some(true)
      }) match {
        case Some(config) => _instance = config
        case _ =>
          println(getUsage)
          throw new Exception("Bad arguments")
      }
    }
    _instance
  }

  /**
   * @return the Config instance after initialization
   */
  def get(): Config = if (!_instance.isEmpty) _instance else throw new Exception("Config must be initialize first")

  /**
   * Set a new Config if it checks
   *
   * @return `true` if the passed Config was set as the new instance, `false` otherwise
   */
  def set(c: Config): Boolean = if (c.checks) {
    _instance = c
    true
  } else false

  /**
   * @return the command-line usage text
   */
  def getUsage: String = {
    OParser.usage[Config](parser)
  }

  private[Config] lazy val builder: OParserBuilder[Config] = OParser.builder[Config]
  private[Config] lazy val parser: OParser[Unit, Config] = {
    import builder._
    val v = EMPTY.appVersion
    OParser.sequence(
      head("feistel", v),
      programName(s"java -cp feistel-jar-${v}.jar fr.edgewhere.feistel.Main"),
      opt[Unit]("decrypt")
        .abbr("d")
        .action((x, c) => c.copy(decrypt = true))
        .text("add to deobfuscate the passed input"),
      opt[Engine]("hashEngine")
        .abbr("h")
        .action((x, c) => c.copy(hash = Some(x)))
        .text("the hash engine for the round function (default SHA-256)"),
      opt[Int]("rounds")
        .abbr("r")
        .action((x, c) => c.copy(rounds = Some(x)))
        .text("the number of rounds for the Feistel cipher (default 10)"),
      opt[String]("key")
        .abbr("k")
        .action((x, c) => c.copy(key = Some(x)))
        .text("the optional key for the FPE scheme (leave it empty to use default)"),
      opt[String]("output")
        .abbr("o")
        .action((x, c) => c.copy(output = Some(x)))
        .text("the optional name of the output file"),
      arg[String]("<input>")
        .unbounded()
        .action((x, c) => c.copy(input = x))
        .text("the data to process")
    )
  }
}
