package com.chiva.abstractmem

/**
  * Created by Coder on 2016/11/3.
  */
object TestAbmem extends App {
  val x = 2
  println(new {
    val numerArg = 1 * x
    val denomArg = 2 * x
  } with RationalTrait)

  //  Pre-initialized fields in an object definition
  object twoThirds extends {
    val numerArg = 2
    val denomArg = 3
  } with RationalTrait

  println(twoThirds)

  //  Pre-initialized fields in a class definition
  class RationalClass(n: Int, d: Int) extends {
    val numerArg = n
    val denomArg = d
  } with RationalTrait {
    def +(that: RationalClass) = new RationalClass(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )
  }

  object Demo {
    lazy val x = {
      println("initializing x");
      "done"
    }
  }

  Demo
  Demo.x
  print(new LazyRationalTrait {
    override val denomArg: Int = 1 * x
    override val numerArg: Int = 2 * x
  })

  /* Path-dependent types*/
  new Cow().eat(new Grass)
  val bessy: Animal = new Cow
  //  bessy.eat(new Fish)
  val lassie = new Dog
  //  lassie.eat(new bessy.SuitableFood)
  val bootsie = new Dog
  lassie.eat(new bootsie.SuitableFood)

  val o1 = new Outer
  val a1 = new o1.Inner
  val o2 = new Outer
  val a2 = new o2.Inner
  //  Enumration
  import Color._

  val red = Red
  for (d <- Direction.values) println(d + " " + d.id)
}

trait Abstract {
  type T

  def transform(x: T): T

  val initial: T
  var current: T
}

class Concrete extends Abstract {
  override type T = String

  override def transform(x: String) = x + x

  override val initial = "hi"
  override var current = initial
}

abstract class Fruit {
  val v: String

  // ‘v' for value
  def m: String // ‘m' for method
}

abstract class Apple extends Fruit {
  val v: String
  val m: String // OK to override a ‘def' with a ‘val'
}

abstract class BadApple extends Fruit {
  // ERROR: cannot override a ‘val' with a ‘def'
  //  def v: String

  def m: String
}

trait RationalTrait {
  val numerArg: Int
  val denomArg: Int
  require(denomArg != 0)
  private val g = gcd(numerArg, denomArg)
  val numer = numerArg / g
  val denom = denomArg / g

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  override def toString = numer + "/" + denom
}

trait LazyRationalTrait {
  val numerArg: Int
  val denomArg: Int
  lazy val numer = numerArg / g
  lazy val denom = denomArg / g

  override def toString = numer + "/" + denom

  private lazy val g = {
    require(denomArg != 0)
    gcd(numerArg, denomArg)
  }

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
}

//Path-dependent types
class Food

abstract class Animal {
  type SuitableFood <: Food

  def eat(food: SuitableFood)
}

class Grass extends Food

class Fish extends Food

class Cow extends Animal {
  override type SuitableFood = Grass

  override def eat(food: SuitableFood) = {}
}

class DogFood extends Food

class Dog extends Animal {
  type SuitableFood = DogFood

  override def eat(food: DogFood) = {}
}

class Outer {

  class Inner

}

/*Refinement types*/
class Pasture {
  var animals: List[Animal {type SuitableFood = Grass}] = Nil
}

/*Enumerations*/
object Color extends Enumeration {
  val Red, Green, Blue = Value
}

//object Direction extends Enumeration {
//  val North, East, South, West = Value
//}
object Direction extends Enumeration {
  val North = Value("North")
  val East = Value("East")
  val South = Value("South")
  val West = Value("West")
}
