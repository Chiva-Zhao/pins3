package com.chiva

object BasicType extends App {
  val name = "hello"
  for (arg <- args) {
    println(s"$arg,$name");
  }

}