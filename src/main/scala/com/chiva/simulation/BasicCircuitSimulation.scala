package com.chiva.simulation

/**
  * Created by Coder on 2016/10/25.
  */
abstract class BasicCircuitSimulation extends Simulation {
  def InverterDelay: Int

  def AndGateDelay: Int

  def OrGateDelay: Int

  class Wire {
    private var sigVal = false
    private var actions: List[Action] = List()

    def getSignal = sigVal

    def setSignal(s: Boolean): Unit = {
      sigVal = s
      actions foreach (_ ())
    }

    def addAction(a: Action) = {
      actions = a :: actions
      a()
    }
  }

  def inverter(input: Wire, output: Wire) = {
    def invertAction() = {
      val inputSig = input.getSignal
      afterDelay(InverterDelay) {
        output setSignal !inputSig
      }
    }
    input addAction invertAction
  }

  def andGate(a1: Wire, a2: Wire, output: Wire) = {
    def andAction() = {
      val sig1 = a1.getSignal
      val sig2 = a2.getSignal
      afterDelay(AndGateDelay) {
        output setSignal (sig1 & sig2)
      }
    }
    a1 addAction andAction
    a2 addAction andAction
  }

  def orGate(a1: Wire, a2: Wire, output: Wire) = {
    def orAction() = {
      val sig1 = a1.getSignal
      val sig2 = a2.getSignal
      output setSignal (sig1 | sig2)
    }
    a1 addAction orAction
    a2 addAction orAction
  }

  def probe(name: String, wire: Wire) = {
    def probeAction() = {
      println(name + " " + currentTime + " new-value = " + wire.getSignal)
    }
    wire addAction probeAction
  }
}
