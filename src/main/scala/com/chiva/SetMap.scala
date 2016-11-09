package com.chiva

/**
  * Created by Coder on 2016/10/24.
  */

import scala.collection.mutable.ListBuffer

object SetMap extends App {
  val buf = new ListBuffer[Int]
  buf += 1
  2 +=: buf

  def hasUpperCase(s: String) = s.exists(_.isUpper)

  hasUpperCase("Robert Frost")
  val text = "See Spot run. Run, Spot. Run!"
  val wordsArray = text.split("[ !,.]+")
  //Set
  val nums = Set(1, 2, 3)
  nums + 5
  nums - 3
  nums ++ List(1, 2, 8, 9)
  nums -- List(1, 2)
  nums & Set(1, 3, 5, 7)
  nums.size
  nums.contains(3)

  import scala.collection.mutable

  val words1 = mutable.Set.empty[String]
  words1 += "set"
  words1 -= "set"
  words1 ++= List("do", "ri", "mi")
  words1 --= List("do", "ri")
  words1.clear
  //  Map
  val map = mutable.Map.empty[String, Int]
  map("Hello") = 1
  map("world") = 2

  def countWords(text: String) = {
    val counts = mutable.Map.empty[String, Int]
    for (raw <- text.split("[ ,!.]")) {
      val word = raw.toLowerCase
      val ct = if (counts.contains(word)) counts(word)
      else 0
      counts += (word -> (ct + 1))
    }
    counts
  }

  countWords("See Spot run! Run, Spot. Run!")
  val nums1 = Map("i" -> 1, "ii" -> 2)
  nums1 + ("vi" -> 5)
  nums1 - "ii"
  nums1 ++ List("a" -> 5, "b" -> 6)
  nums1 -- List("i", "ii")
  nums1.size
  nums1.contains("ii")
  nums1("ii")
  nums1.keys
  nums1.keySet
  nums1.values
  nums1.isEmpty
  val words = mutable.Map.empty[String, Int]
  words += ("a" -> 1)
  words -= ("a")
  words ++= List("b" -> 3, "c" -> 2)
  words --= List("b", "c")

  import scala.collection.immutable.TreeSet
  import scala.collection.immutable.TreeMap

  val ts = TreeSet(9, 3, 1, 8, 0, 2, 7, 4, 6, 5)
  var tm = TreeMap(3 -> 'x', 1 -> 'x', 4 -> 'x')

  //  val people = Set("Nancy", "Jane")
  //  people += "Bob"
  var people = Set("Nancy", "Jane")
  people += "Bob"
  people -= "Jane"
  people ++= List("Tom", "Harry")
  //convert mutable <->immutable
  val colors = List("blue", "yellow", "red", "green")

  import scala.collection.immutable.TreeSet

  //  val treeSet = TreeSet(colors)
  val treeSet = TreeSet[String]() ++ colors
  treeSet.toList
  treeSet.toArray
  val mutaSet = mutable.Set.empty ++= treeSet
  val immutaSet = Set.empty ++ mutaSet
  val muta = mutable.Map("i" -> 1, "ii" -> 2)
  val immu = Map.empty ++ muta

  //  Tuples
  def longestWord(words: Array[String]) = {
    var idx = 0
    var word = words(0)
    for (i <- 1 until words.length)
      if (words(i).length > word.length) {
        word = words(i)
        idx = i
      }
    (word, idx)
  }

  println(longestWord("The quick brown fox".split(" ")))
}

