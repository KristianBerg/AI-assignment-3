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
  val states = 0 to grid.length * 4;
  val sensorProb = for(reading <- grid) yield for(position <- grid) yield
    if((reading.cdist(position)) == 2) 0.025
    else if ((reading.cdist(position)) == 1) 0.05
    else if (reading == position) 0.1
    else 0
  val noReadingProb = for(i <- 0 until grid.length) yield 1 - sensorProb(i).sum

  val O = for(row <- sensorProb :+ noReadingProb) yield
    new DenseVector(row.toArray.flatMap(x => List.fill(4)(x/4)));

  val T = new DenseMatrix(states.length, states.length,
    (for(from <- states; to <- states) yield {
      import BotSimulator.dirVector; val dir = from % 4; val newdir = to % 4
      val colliding = !grid.contains(grid(from / 4) + dirVector(dir))
      val free = grid(from / 4).moore(1).filter(_ in grid).size
      val inbound = grid(from / 4) + dirVector(from % 4) == grid(to / 4)
      if(grid(to / 4) == grid(from / 4) + dirVector(to % 4)) {
        if (colliding)
          1.0 / free
        else if (inbound)
          0.7
        else
          0.3 / (free - 1)
      } else 0
    }).toArray)

  override def getNumRows: Int = ???

  override def getNumCols: Int = ???

  override def getNumHead: Int = ???

  override def update(): Unit = ???

  override def getCurrentTruePosition: Array[Int] = Array(bot.pos.x, bot.pos.y)

  override def getCurrentReading: Array[Int] = ???

  override def getCurrentProb(x: Int, y: Int): Double = ???

  override def getOrXY(rX: Int, rY: Int, x: Int, y: Int): Double = ???

  override def getTProb(x: Int, y: Int, h: Int, nX: Int, nY: Int, nH: Int): Double = ???
}
