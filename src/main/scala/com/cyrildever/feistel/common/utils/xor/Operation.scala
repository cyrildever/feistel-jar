package com.cyrildever.feistel.common.utils.xor

import com.cyrildever.feistel.common.exception.NotSameLengthException

/**
 * XOR utilities
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
object Operation {
  implicit class XorOps(s: String) {
    /**
     * Apply XOR operation
     *
     * @throws  NotSameLengthException
     */
    def ^(that: String): String = {
      if (s.length != that.length) throw NotSameLengthException()
      else s.zipWithIndex.foldLeft("")((xored, item) => xored ++ (item._1.toByte ^ that(item._2).toByte).toChar.toString)
    }
  }
}
