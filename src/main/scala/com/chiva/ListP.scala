package com.chiva

/**
  * Created by Coder on 2016/10/18.
  */
object ListP extends App {
  def isort(list: List[Int]): List[Int] = {
    if (list isEmpty) Nil
    else insert(list.head, isort(list.tail))
  }

  def insert(x: Int, xs: List[Int]): List[Int] = {
    if (xs.isEmpty || x <= xs.head) x :: xs
    else xs.head :: insert(x, xs.tail)
  }

  def isort1(list: List[Int]): List[Int] = list match {
    case Nil => Nil
    case x :: xs => insert1(x, isort(xs))
  }

  def insert1(x: Int, xs: List[Int]): List[Int] = xs match {
    case Nil => Nil
    case y :: ys => if (x <= y) x :: xs
    else y :: insert(x, ys)
  }

  val list = List(4, 1, 65, 10)
  val sorted = isort1(list)
  println(sorted)

  def append[T](xs: List[T], ys: List[T]): List[T] =
    xs match {
      case Nil => ys
      case x :: xs1 => x :: append(xs1, ys)
    }

  println(append(List(1, 2), List(3, 4, 5)))

  def rev[T](list: List[T]): List[T] = list match {
    case Nil => list
    case x :: xs => rev(xs) ::: List(x)
  }

  println(rev(List(5, 7, 80, 10)))
  List(List(1, 2), List(3), List(), List(4, 5)).flatten

  val abcde = List('a', 'b', 'c', 'd', 'e')
  val zipped = abcde zip List(1, 2, 3)
  println(abcde zipWithIndex)
  println(zipped.unzip)
  println(abcde.mkString("-", "|", ">"))
  val buf = new StringBuilder
  println(abcde addString(buf, "(", ",", ")"))

  def msort[T](less: (T, T) => Boolean)(xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]): List[T] = {
      (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (less(x, y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }
    }
    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs.splitAt(n)
      merge(msort(less)(ys), msort(less)(zs))
    }
  }

  println(msort((x: Int, y: Int) => x < y)(List(5, 3, 7, 1, 10)))
  val intSort = msort((x: Int, y: Int) => x < y) _
  println(intSort(List(5, 3, 7, 1, 10)))
  val revSort = msort((x: Int, y: Int) => x > y) _
  println(revSort(List(5, 3, 7, 1, 10)))
  List.range(1, 5) flatMap (
    i => List.range(1, i) map (j => (i, j))
    )
  for (i <- List.range(1, 5); j <- List.range(1, i)) yield (i, j)
  List(1, 2, 3, 4, 5) partition (_ % 2 == 0)
  List(1, 2, 3, 4, 5) find (_ % 2 == 0)
  List(1, 2, 3, -4, 5) takeWhile (_ > 0)
  val words = List("the", "quick", "brown", "fox")
  words dropWhile (_ startsWith "t")
  //  xs span p equals (xs takeWhile p, xs dropWhile p)
  List(1, 2, 3, -4, 5) span (_ > 0)

  def hasZeroRow(m: List[List[Int]]) =
    m exists (row => row forall (_ == 0))

  val diag3: List[List[Int]] =
    List(
      List(1, 0, 0),
      List(0, 1, 0),
      List(0, 0, 1)
    )
  hasZeroRow(diag3)

  def sum(list: List[Int]): Int = (0 /: list) (_ + _)

  println(sum(List.range(1, 5)))

  def flattenLeft[T](xss: List[List[T]]) =
    (List[T]() /: xss) (_ ::: _)

  def flattenRight[T](xss: List[List[T]]) =
    (xss :\ List[T]()) (_ ::: _)

  def reverseLeft[T](xs: List[T]) = (List[T]() /: xs) ((a, b) => b :: a)

  List(1, -3, 4, 2, 6) sortWith (_ < _)
  words sortWith (_.length > _.length)
  val squares = List.tabulate(5)(n => n * n)
  val multiplication = List.tabulate(5, 5)(_ * _)
  List.concat(List('a', 'b'), List('c'))
  (List(10, 20), List(3, 4, 5)).zipped.map(_ * _)

  abcde sortWith (_ > _)
  msort[Char](_ > _)(abcde)
}
