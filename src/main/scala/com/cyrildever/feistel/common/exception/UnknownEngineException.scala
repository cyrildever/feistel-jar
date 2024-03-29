package com.cyrildever.feistel.common.exception

/**
 * UnknownEngineException class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
final case class UnknownEngineException(
  private val message: String = "Unknown engine",
  private val cause: Throwable = None.orNull
) extends Exception(message, cause)
