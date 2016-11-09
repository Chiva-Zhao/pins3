package com.chiva.simulation

/**
  * Created by Coder on 2016/10/26.
  */
object MySimulation extends CircuitSimulation {
  override def InverterDelay: Int = 1

  override def AndGateDelay: Int = 2

  override def OrGateDelay: Int = 3
}

import MySimulation._

object RunExample extends App {
  val input1, input2, sum, carry = new Wire
  probe("sum", sum)
  probe("carry", carry)
  halfAdder(input1, input2, sum, carry)
  input1 setSignal true
  run()
  input2 setSignal true
  run()
}
