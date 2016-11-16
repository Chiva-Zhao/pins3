package com.chiva

/**
  * Created by Coder on 2016/11/16.
  */
object NineBitNumber extends App {
  def find(): Unit = {
    for (i <- 100000000 to 999999999) {
      val j = i.toString.toSet;
      if (j.size == 9 && !j.contains('0')) {
        val second = i / Math.pow(10, 9 - 2) toInt
        val third = i / Math.pow(10, 9 - 3) toInt
        val forth = i / Math.pow(10, 9 - 4) toInt
        val fifth = i / Math.pow(10, 9 - 5) toInt
        val sixth = i / Math.pow(10, 9 - 6) toInt
        val seventh = i / Math.pow(10, 9 - 7) toInt
        val eighth = i / Math.pow(10, 9 - 8) toInt
        val nithth = i
        val list: List[Int] = List(second, third, forth, fifth, sixth, seventh, eighth, nithth)
        var finish = true
        for (k <- 2 to 9; if finish)
          finish = finish && (list(k - 2) % k == 0)
        if (finish)
          println(i)
      }
    }
  }

  find()
}
