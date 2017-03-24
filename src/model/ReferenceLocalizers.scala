package model

import control.EstimatorInterface

import scala.util.Random

abstract class StupidLocalizer(val gridSize: Int) extends EstimatorInterface {
  val bot = new BotSimulator(gridSize)
  var pos = (0,0)
  import BotSimulator.dirs

  override def getNumRows: Int = bot.rows

  override def getNumCols: Int = bot.cols

  override def getNumHead: Int = dirs

  override def getCurrentTruePosition: Array[Int] = Array(bot.pos._1, bot.pos._2)

  override def getCurrentReading: Array[Int] = bot.reading match {
    case Some((x, y)) => Array(x, y)
    case None => null
  }

  override def getCurrentProb(x: Int, y: Int): Double = if ((x, y) == pos) 1 else 0

  override def getOrXY(rX: Int, rY: Int, x: Int, y: Int): Double = if((rX, rY) == (x,y)) 1 else 0

  override def getTProb(x: Int, y: Int, h: Int, nX: Int, nY: Int, nH: Int): Double = return 0
}

class RandomLocalizer(size: Int) extends StupidLocalizer(size) {
  override def update(): Unit = {
    bot.update()
    pos = (Random.nextInt(bot.rows), Random.nextInt(bot.cols))
  }
}

class SensorLocalizer(size: Int) extends StupidLocalizer(size) {
  override def update(): Unit = {
    bot.update()
    pos = bot.reading.getOrElse((0,0))
  }
}