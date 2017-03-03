package model

import control.EstimatorInterface
import model.GridUtils._
import breeze.linalg._

/**
  * Created by ine13kbe on 28/02/17.
  */
class AwesomeLocalizer extends EstimatorInterface {
  val bot: BotSimulator = new BotSimulator()
  val grid = Grid(bot.rows, bot.cols)
  val states = 0 to grid.length * 4;
  val sensorProb = for (reading <- grid) yield for (position <- grid) yield
    if ((reading.cdist(position)) == 2) 0.025
    else if ((reading.cdist(position)) == 1) 0.05
    else if (reading == position) 0.1
    else 0
  val noReadingProb = for(i <- 0 until grid.length) yield 1 - sensorProb(i).sum

  val O = for (row <- sensorProb :+ noReadingProb) yield
    new DenseVector(row.toArray.flatMap(x => List.fill(4)(x / 4)));

  val T = new DenseMatrix(states.length, states.length,
    (for(from <- states; to <- states) yield {
      import BotSimulator.dirVector;
      val (dir, newDir, pos, newPos) = (from % 4, to % 4, grid(from / 4), grid(to / 4))
      val colliding = !grid.contains(pos + dirVector(dir))
      val free = pos.neumann(1).filter(_ in grid).size
      val inbound = pos + dirVector(dir) == newPos
      if(newPos == pos + dirVector(newDir)) {
        if (colliding)
          1.0 / free
        else if (inbound)
          0.7
        else
          0.3 / (free - 1)
      } else 0
    }).toArray)

  var f = DenseVector.fill(states.length) {
    1.0 / states.length
  }
  var alpha: Double = 0

  override def getNumRows: Int = bot.rows

  override def getNumCols: Int = bot.cols

  override def getNumHead: Int = 4

  override def update(): Unit = {
    bot.update()
    val sensorIndex = sensorReadingToIndex(bot.reading)
    alpha = 1.0 / (O(sensorIndex).t * f)
    f = alpha * (diag(O(sensorIndex)) * T.t) * f
  }

  override def getCurrentTruePosition: Array[Int] = Array(bot.pos.x, bot.pos.y)

  override def getCurrentReading: Array[Int] = bot.reading match {
      case Some((x, y)) => Array(x, y)
      case None => null
    }
  override def getCurrentProb(x: Int, y: Int): Double = f()

  override def getOrXY(rX: Int, rY: Int, x: Int, y: Int): Double = (rX, rY) match {
    case (-1, -1) => noReadingProb(grid.indexOf((x, y)))
    case (rX, rY) => sensorProb(grid.indexOf((rX, rY)))(grid.indexOf((x, y)))
  }

  override def getTProb(x: Int, y: Int, h: Int, nX: Int, nY: Int, nH: Int): Double = ???

  private def sensorReadingToIndex(reading: Option[(Int, Int)]): Int =
    reading match {
      case Some((x, y)) => x * bot.rows * 4 + y
      case None => states.length
    }
}
