package model

import control.EstimatorInterface
import model.GridUtils._
import breeze.linalg._

/**
  * Created by ine13kbe on 28/02/17.
  */
class AwesomeLocalizer extends EstimatorInterface{
  val bot: BotSimulator = new BotSimulator()
  val grid = Grid(bot.rows, bot.cols)
  val sensorProb = for(reading <- grid) yield for(position <- grid) yield
    if((reading.cdist(position)) == 2) 0.025
    else if ((reading.cdist(position)) == 1) 0.05
    else if (reading == position) 0.1
    else 0
  

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
