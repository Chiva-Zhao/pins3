package com.chiva.implicits

/**
  * Created by Coder on 2016/11/7.
  */

import com.chiva.mytrait.Rational

object TestConversion extends App {
  /*Implicit conversion to an expected type*/
  implicit def double2Int(x: Double): Int = x.toInt;
  val i: Int = 3.5

  /*Converting the receiver*/
  //  Interoperating with new types
  implicit def intToRational(x: Int) = new Rational(x, 1)

  val oneHalf = new Rational(1, 2)
  1 + oneHalf
  //  Simulating new syntax
  Map(1 -> "one", 2 -> "two", 3 -> "three")

  //  Implicit classes
  case class Rectangle(width: Int, height: Int)

  implicit class RectangleMaker(width: Int) {
    def x(height: Int) = Rectangle(width, height)
  }

  val myRectangle = 3 x 4

  /*Implicit parameters*/
  class PreferredPrompt(val preference: String)

  class PreferredDrink(val preference: String)

  object Greeter {
    def greet(name: String)(implicit prompt: PreferredPrompt,
                            drink: PreferredDrink) = {
      println("Welcome, " + name + ". The system is ready.")
      print("But while you work, ")
      println("why not enjoy a cup of " + drink.preference + "?")
      println(prompt.preference)
    }
  }

  object JoesPrefs {
    implicit val prompt = new PreferredPrompt("Yes, master> ")
    implicit val drink = new PreferredDrink("tea")
  }

  import JoesPrefs._

  Greeter.greet("Joe")

  //  A function with an upper bound
  def maxListOrdering[T](elements: List[T])(ordering: Ordering[T]): T = elements match {
    case List() => throw new IllegalArgumentException("empty list")
    case List(x) => x
    case x :: rest =>
      val max = maxListOrdering(rest)(ordering)
      if (ordering.gt(x, max)) x
      else max
  }

  //  A function with an implicit parameter.
  def maxListImpParm[T](elements: List[T])(implicit ordering: Ordering[T]): T =
  elements match {
    case List() =>
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxListImpParm(rest) /*(ordering)*/
      if (ordering.gt(x, maxRest)) x
      else maxRest
  }

  print(maxListImpParm(List(1, 5, 10, 3)))
  print(maxListImpParm(List(1.5, 5.2, 10.7, 3.14159)))

  //  Context bounds
  //  A function that uses an implicit parameter internally
  def maxList1[T](elements: List[T])(implicit ordering: Ordering[T]): T =
  elements match {
    case List() =>
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxList1(rest) // (ordering) is implicit
      if (ordering.gt(x, maxRest)) x // this ordering is
      else maxRest // still explicit
  }

  //  A function that uses implicitly.
  def maxList2[T](elements: List[T])(implicit ordering: Ordering[T]): T =
  elements match {
    case List() =>
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxList2(rest)
      if (implicitly[Ordering[T]].gt(x, maxRest)) x
      else maxRest
  }

  //  A function with a context bound
  def maxList3[T: Ordering](elements: List[T]): T =
  elements match {
    case List() =>
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxList3(rest)
      if (implicitly[Ordering[T]].gt(x, maxRest)) x
      else maxRest
  }

  /*When multiple conversions apply*/
  def printLength(seq: Seq[Int]) = println(seq.length)

  implicit def intToRange(i: Int) = 1 to i

  implicit def intToDigits(i: Int) = i.toString.toList.map(_.toInt)

  //  printLength(12)
}

object Mocha extends App {

  class PreferredDrink(val preference: String)

  implicit val pref = new PreferredDrink("mocha")

  def enjoy(name: String)(implicit drink: PreferredDrink) = {
    print("Welcome, " + name)
    print(". Enjoy a ")
    print(drink.preference)
    println("!")
  }

  enjoy("reader")
}