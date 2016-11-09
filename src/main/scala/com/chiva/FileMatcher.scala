package com.chiva

import java.io.File

object FileMatcher {
  private def files = new File(".").listFiles();
  //  def filesEnding(query: String) = {
  //    for (file <- files; if file.getName.endsWith(query))
  //      yield file
  //  }
  //
  //  def filesContaining(query: String) =
  //    for (file <- files; if file.getName.contains(query))
  //      yield file
  //
  //  def filesRegex(query: String) =
  //    for (file <- files; if file.getName.matches(query))
  //      yield file
  def filesEnding(query: String) =
    filesMatching(_.endsWith(query))
  def filesContaining(query: String) =
    filesMatching(_.contains(query))
  def filesRegex(query: String) =
    filesMatching(_.matches(query))

  def filesMatching(matcher: (String) => Boolean) = {
    for (file <- files; if matcher(file.getName))
      yield file
  }

  def twice(op: Double => Double, x: Double) = op(op(x))

  var assertionsEnabled = true
  def byNameAssert(predicate: => Boolean) =
    if (assertionsEnabled && !predicate)
      throw new AssertionError
  def main(args: Array[String]): Unit = {
    println(twice(_ + 2, 5));
  }
}