package com.chiva

/**
  * Created by Coder on 2016/11/9.
  */
object ForExpress extends App {

  case class Person(name: String, isMale: Boolean, children: Person*)

  val lara = Person("Lara", false)
  val bob = Person("Bob", true)
  val julie = Person("Julie", false, lara, bob)
  val persons = List(lara, bob, julie)
  val pc = persons filter (!_.isMale) flatMap (p => p.children map (c => (p.name, c.name)))
  persons withFilter (!_.isMale) flatMap (p => p.children map (c => (p.name, c.name)))
  for (p <- persons; if !p.isMale; c <- p.children)
    yield (p.name, c.name)
  //  for ( seq ) yield expr
  //  Here, seq is a sequence of generators, definitions, and filters,
  // with semicolons between successive elements
  val matches = for (a <- List(1, 2); b <- List("one", "two"))
    yield (a, b)
  print(matches)

  //  The n-queens problem
  def inCheck(q1: (Int, Int), q2: (Int, Int)) =
  q1._1 == q2._1 || //same row
    q1._2 == q2._2 || //same column
    (q1._1 - q2._1).abs == (q1._2 - q2._2).abs

  def isSafe(queen: (Int, Int), queens: List[(Int, Int)]) = queens forall (q => !inCheck(queen, q))

  def queens(n: Int): List[List[(Int, Int)]] = {
    def placeQueens(k: Int): List[List[(Int, Int)]] = {
      if (k == 0)
        List(List())
      else
        for {
          queens <- placeQueens(k - 1)
          column <- 1 to n
          queen = (k, column)
          if isSafe(queen, queens)
        } yield queen :: queens
    }
    placeQueens(n)
  }

  println(queens(8))

  //  Querying with for expressions
  case class Book(title: String, authors: String*)

  val books: List[Book] =
    List(
      Book(
        "Structure and Interpretation of Computer Programs",
        "Abelson, Harold", "Sussman, Gerald J."
      ),
      Book(
        "Principles of Compiler Design",
        "Aho, Alfred", "Ullman, Jeffrey"
      ),
      Book(
        "Programming in Modula-2",
        "Wirth, Niklaus"
      ),
      Book(
        "Elements of ML Programming",
        "Ullman, Jeffrey"
      ),
      Book(
        "The Java Language Specification", "Gosling, James",
        "Joy, Bill", "Steele, Guy", "Bracha, Gilad"
      )
    )

  println(for (b <- books; a <- b.authors if a startsWith "Gosling") yield b.title)
  println(for (b <- books if (b.title indexOf "Program") >= 0) yield b.title)

  //  Translation of for expressions
  //  Every for expression can be expressed in terms of the three higher-order
  //  functions map, flatMap, and withFilter
  //  Going the other way
  object Demo {
    def map[A, B](xs: List[A], f: A => B): List[B] =
      for (x <- xs) yield f(x)

    def flatMap[A, B](xs: List[A], f: A => List[B]): List[B] =
      for (x <- xs; y <- f(x)) yield y

    def filter[A](xs: List[A], p: A => Boolean): List[A] =
      for (x <- xs if p(x)) yield x
  }

}
