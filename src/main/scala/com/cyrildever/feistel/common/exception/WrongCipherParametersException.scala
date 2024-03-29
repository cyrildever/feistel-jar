package com.cyrildever.feistel.common.exception

/**
 * WrongCipherParametersException class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
final case class WrongCipherParametersException(
  private val message: String = "Wrong cipher parameters",
  private val cause: Throwable = None.orNull
) extends Exception(message, cause)
