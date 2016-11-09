package com.chiva.matcher

import com.chiva.Element
import com.chiva.Element.elem

/**
  * Created by Coder on 2016/10/17.
  */
sealed abstract class Expr

case class Var(name: String) extends Expr

case class Number(num: Double) extends Expr

case class UnOp(operator: String, arg: Expr) extends Expr

case class BinOp(operator: String,
                 left: Expr, right: Expr) extends Expr

class ch15 {
  def simplifyAll(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", e)) =>
      simplifyAll(e) // ‘-' is its own inverse
    case BinOp("+", e, Number(0)) =>
      simplifyAll(e) // ‘0' is a neutral element for ‘+'
    case BinOp("*", e, Number(1)) =>
      simplifyAll(e) // ‘1' is a neutral element for ‘*'
    case UnOp(op, e) =>
      UnOp(op, simplifyAll(e))
    case BinOp(op, l, r) =>
      BinOp(op, simplifyAll(l), simplifyAll(r))
    case _ => expr
  }

  def describe(exp: Expr): String = exp match {
    case Number(_) => "a number"
    case Var(_) => "a variable"
    case _ => throw new RuntimeException
  }

  def describe1(exp: Expr): String = (exp: @unchecked) match {
    case Number(_) => "a number"
    case Var(_) => "a variable"
  }

  val myTuple = (123, "abc")
  val (number, string) = myTuple

  def second(list: List[Int]): Int = list match {
    case x :: y :: _ => y
  }

  val second: PartialFunction[List[Int], Int] = {
    case x :: y :: _ => y
  }

  val results = List(Some("apple"), None, Some("orange"))
  for (Some(fruit) <- results) println(fruit)
}

class ExprFormatter {
  // Contains operators in groups of increasing precedence
  private val opGroups =
  Array(
    Set("|", "||"),
    Set("&", "&&"),
    Set("ˆ"),
    Set("==", "!="),
    Set("<", "<=", ">", ">="),
    Set("+", "-"),
    Set("*", "%")
  )
  // A mapping from operators to their precedence
  private val precedence = {
    val assocs =
      for {
        i <- 0 until opGroups.length
        op <- opGroups(i)
      } yield op -> i
    assocs.toMap
  }
  private val unaryPrecedence = opGroups.length
  private val fractionPrecedence = -1

  private def format(e: Expr, enclPrec: Int): Element =
    e match {
      case Var(name) =>
        elem(name)
      case Number(num) =>
        def stripDot(s: String) =
          if (s endsWith ".0") s.substring(0, s.length - 2)
          else s
        elem(stripDot(num.toString))
      case UnOp(op, arg) =>
        elem(op) beside format(arg, unaryPrecedence)
      case BinOp("/", left, right) =>
        val top = format(left, fractionPrecedence)
        val bot = format(right, fractionPrecedence)
        val line = elem('-', top.width max bot.width, 1)
        val frac = top above line above bot
        if (enclPrec != fractionPrecedence) frac
        else elem(" ") beside frac beside elem(" ")
      case BinOp(op, left, right) =>
        val opPrec = precedence(op)
        val l = format(left, opPrec)
        val r = format(right, opPrec + 1)
        val oper = l beside elem(" " + op + " ") beside r
        if (enclPrec <= opPrec) oper
        else elem("(") beside oper beside elem(")")
    }

  def format(e: Expr): Element = format(e, 0)
}
