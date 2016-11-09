package com.chiva.mytrait

import scala.collection.mutable.ArrayBuffer

abstract class IntQueue {
  def put(x: Int)
  def get(): Int
}
trait Doubling extends IntQueue {
  abstract override def put(x: Int) = super.put(2 * x)
}
trait Increament extends IntQueue {
  abstract override def put(x: Int) = super.put(x + 1)
}
trait Filtering extends IntQueue {
  abstract override def put(x: Int) = if (x >= 0) super.put(x);
}
class BasicIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]
  def put(x: Int): Unit = {
    buf += x
  }

  def get(): Int = {
    buf.remove(0)
  }
}

object Try extends App {
  val q2 = new BasicIntQueue with Increament with Filtering
  q2.put(-1); q2.put(0); q2.put(1);
  println(q2.get())
  println(q2.get())

  val q3 = new BasicIntQueue with Filtering with Increament
  q3.put(-1); q3.put(0); q3.put(1);
  println(q3 get)
  println(q3.get)
  println(q3 get)
}

