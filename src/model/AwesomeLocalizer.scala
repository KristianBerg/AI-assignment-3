package model

import control.EstimatorInterface

/**
  * Created by ine13kbe on 28/02/17.
  */
class AwesomeLocalizer extends EstimatorInterface{
  override def getNumRows: Int = ???

  override def getNumCols: Int = ???

  override def getNumHead: Int = ???

  override def update(): Unit = ???

  override def getCurrentTruePosition: Array[Int] = ???

  override def getCurrentReading: Array[Int] = ???

  override def getCurrentProb(x: Int, y: Int): Double = ???

  override def getOrXY(rX: Int, rY: Int, x: Int, y: Int): Double = ???

  override def getTProb(x: Int, y: Int, h: Int, nX: Int, nY: Int, nH: Int): Double = ???
}
