package com.cyrildever.feistel.common.exception

/**
 * NotSameLengthException class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
final case class NotSameLengthException(
  private val message: String = "Items must be of the same length",
  private val cause: Throwable = None.orNull
) extends Exception(message, cause)
