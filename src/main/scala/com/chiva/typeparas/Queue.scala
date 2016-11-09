package com.chiva.typeparas

import scala.collection.immutable.Nil

/**
  * Created by Coder on 2016/10/27.
  */
trait Queue[T] {
  def head: T

  def tail: Queue[T]

  def enqueue(x: T): Queue[T]
}

object Queue {
  def apply[T](xs: T*): Queue[T] = new QueueImpl(xs.toList, Nil)

  private class QueueImpl[T](private val leading: List[T], private val trailing: List[T]) extends Queue[T] {
    def mirror =
      if (leading.isEmpty)
        new QueueImpl(trailing.reverse, Nil)
      else
        this

    override def head: T = mirror.head

    override def tail: Queue[T] = {
      val q = mirror
      new QueueImpl(q.leading.tail, q.trailing)
    }

    override def enqueue(x: T): Queue[T] = new QueueImpl(leading, x :: trailing)
  }

}

object TestQ extends App {
  val q = Queue[Int](5, 7, 8)

  //  def doesNotCompile(q: Queue) = {}
  def doesNotCompile(q: Queue[AnyRef]) = {}
}

class Fruit

class Apple extends Fruit

class Orange extends Fruit

class Publication(val title: String)

class Book(title: String) extends Publication(title)

object Library {
  val books: Set[Book] =
    Set(
      new Book("Programming in Scala"),
      new Book("Walden")
    )

  def printBookList(info: Book => AnyRef) = {
    for (book <- books) println(info(book))
  }
}

object Customer extends App {
  def getTitle(p: Publication): String = p.title

  Library.printBookList(getTitle)
}

class Queue1[+T] private(
                          private[this] var leading: List[T],
                          private[this] var trailing: List[T]
                        ) {
  private def mirror() =
    if (leading.isEmpty) {
      while (!trailing.isEmpty) {
        leading = trailing.head :: leading
        trailing = trailing.tail
      }
    }

  def head: T = {
    mirror()
    leading.head
  }

  def tail: Queue1[T] = {
    mirror()
    new Queue1(leading.tail, trailing)
  }

  def enqueue[U >: T](x: U) =
    new Queue1[U](leading, x :: trailing)
}