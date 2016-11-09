package com.chiva

import Element.elem
abstract class Element {
  def contents: Array[String]
  def height: Int = contents.length
  def width: Int = contents(0).length
  def above(that: Element): Element = {
    val this1 = this widen that.width
    val that1 = that widen this.width
    assert(this1.width == that1.width)
    elem(this.contents ++ that.contents)
  }

  //  def beside(that: Element): Element = {
  //    val contents = new Array[String](this.contents.length)
  //    for (i <- 0 until that.contents.length) {
  //      contents(i) = this.contents(i) + that.contents(i)
  //    }
  //    new ArrayElement(contents)
  //  }

  def beside(that: Element): Element = {
    val this1 = this heighten that.height
    val that1 = that heighten this.height
    elem(for ((a1, a2) <- this1.contents zip that1.contents) yield a1 + a2)
  }

  override def toString = contents mkString "\n"

  def widen(w: Int): Element = {
    if (w <= width) this
    else {
      val left = elem(' ', (w - width) / 2, height)
      val right = elem(' ', w - width - left.width, height)
      left beside this beside right
    } ensuring (w < _.width)
  }

  def heighten(h: Int): Element = {
    if (h <= height) this
    else {
      val top = elem(' ', width, (h - height) / 2)
      val bottom = elem(' ', width, h - height - top.height)
      top above this above bottom
    }
  }
}

object Element {
  private class ArrayElement(
    val contents: Array[String]) extends Element

  private class LineElement(s: String) extends Element {
    val contents = Array(s)
    override def height = 1
    override def width = s.length()
  }

  private class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element {
    private val line = ch.toString() * width
    def contents: Array[String] = Array.fill(height)(line)
  }
  def elem(contents: Array[String]): Element = new ArrayElement(contents)
  def elem(ch: Char, width: Int, height: Int): Element = new UniformElement(ch, width, height)
  def elem(line: String): Element = new LineElement(line)
}
