package com.chiva.mytrait
object TraitScrach {
  val frog = new Frog
  frog.philosophize()
  val phi: Philosophical = frog
  phi.philosophize()

  val half = new Rational(1, 2)
  val third = new Rational(1, 3)
  half < third
  half > third

  val queue = new BasicIntQueue
  queue put 10
  queue put 20
  queue.get()
  queue.get()

  val q1 = new BasicIntQueue with Doubling
  q1 put 3
  q1 get

  val q2 = new BasicIntQueue with Increament with Filtering
  q2.put(-1); q2.put(0); q2.put(1);
  q2.get()
  q2.get()

  val q3 = new BasicIntQueue with Filtering with Increament
  q3.put(-1); q2.put(0); q2.put(1);
  q3.get()
  q3.get()
  q3 get
}