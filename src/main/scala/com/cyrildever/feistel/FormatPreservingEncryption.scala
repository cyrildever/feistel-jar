package com.cyrildever.feistel

/**
 * FormatPreservingEncryption interface
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
trait FormatPreservingEncryption {
  def encrypt(str: String): String
  def decrypt(str: String): String
}
