package com.chiva.mytrait

trait Philosophical {
  def philosophize() = {
    println("I consume memory, therefore I am!")
  }
}

class Animal
class Frog extends Animal with Philosophical {
  override def toString = "green"
  override def philosophize() = {
    println("It ain't easy being " + toString + "!")
  }
}

class Rational(n: Int, d: Int) extends Ordered[Rational] {
  require(d != 0)
  private val g = gcd(n.abs, d.abs)
  val numer = n / g
  val denom = d / g
  def this(n: Int) = this(n, 1)
  def +(that: Rational): Rational =
    new Rational(
      numer * that.denom + that.numer * denom,
      denom * that.denom)
  def +(i: Int): Rational =
    new Rational(numer + i * denom, denom)
  def -(that: Rational): Rational =
    new Rational(
      numer * that.denom - that.numer * denom,
      denom * that.denom)
  def -(i: Int): Rational =
    new Rational(numer - i * denom, denom)
  def *(that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)
  def *(i: Int): Rational =
    new Rational(numer * i, denom)
  def /(that: Rational): Rational =
    new Rational(numer * that.denom, denom * that.numer)
  def /(i: Int): Rational =
    new Rational(numer, denom * i)
  override def toString = numer + "/" + denom
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  def compare(that: Rational): Int = {
    (this.numer * that.denom) - (that.numer * this.denom)
  }
}
