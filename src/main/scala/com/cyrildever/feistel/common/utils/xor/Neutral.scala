package com.cyrildever.feistel.common.utils.xor

/**
 * Neutral for XOR operation
 */
final case class Neutral(size: Int) { self =>
  def getBytes: Array[Byte] = new Array[Byte](size)

  override def toString: String = Seq[Byte](0).map(_.toChar).mkString * self.size
}

