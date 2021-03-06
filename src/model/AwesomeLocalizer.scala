package model

import control.EstimatorInterface
import model.GridUtils._
import breeze.linalg._

/**
  * Created by ine13kbe on 28/02/17.
  */
class AwesomeLocalizer(val gridSize: Int) extends EstimatorInterface {
  val bot: BotSimulator = new BotSimulator(gridSize)
  import BotSimulator.dirs
  val grid = Grid(bot.rows, bot.cols)
  val states = 0 until grid.length * dirs;
  val sensorProb = for (reading <- grid) yield for (position <- grid) yield
    if ((reading.cdist(position)) == 2) 0.025
    else if ((reading.cdist(position)) == 1) 0.05
    else if (reading == position) 0.1
    else 0
  val noReadingProb = for (i <- 0 until grid.length) yield 1 - sensorProb(i).sum

  val O = for (row <- sensorProb :+ noReadingProb) yield
    new DenseVector(row.toArray.flatMap(x => List.fill(dirs)(x / dirs)));

  val T = new DenseMatrix(states.length, states.length,
    (for (to <- states; from <- states) yield {
      import BotSimulator.dirVector;
      val (dir, newDir, pos, newPos) = (from % dirs, to % dirs, grid(from / dirs), grid(to / dirs))
      val colliding = !grid.contains(pos + dirVector(dir))
      val free = pos.neumann(1).filter(_ in grid).size
      val inbound = pos + dirVector(dir) == newPos
      if (newPos == pos + dirVector(newDir)) {
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
  var averageAccuracy: Double = 0
  var numberOfIterations: Int = 0

  override def getNumRows: Int = bot.rows

  override def getNumCols: Int = bot.cols

  override def getNumHead: Int = dirs

  override def update(): Unit = {
    bot.update()
    //val oldf = f
    val sensorIndex = sensorReadingToIndex(bot.reading)
    //alpha = 1.0 / (O(sensorIndex).t * f)
    //if (alpha.isNaN() || alpha.isInfinite()) alpha = 0.01
    f = /*alpha **/ (diag(O(sensorIndex)) * T.t) * f
    //f.map(x => if (x < 1e-9) 0 else x)
    f = f /:/ sum(f)
    // average accuracy calculations and printout
    numberOfIterations += 1
    val predictionIndex: (Int, Int) = grid(argmax(f)/4)
    val distance: Double = bot.pos.mdist(predictionIndex)
    averageAccuracy = averageAccuracy * (numberOfIterations - 1.0)/numberOfIterations + distance/numberOfIterations
    /*if(numberOfIterations % 5 == 0){
      println(s"Sum of f: ${sum(f)}")
      println("prediction: " + predictionIndex + " | actual: " + bot.pos)
      println(numberOfIterations + "\tdistance: " + distance + "\taverage accuracy: " + averageAccuracy)
    }*/
    /*if(f(0).isNaN()) {
      println(oldf)
      println(O(sensorIndex).t * oldf)
      println(s"Sensor repeorts at: ${grid(sensorIndex / dirs)}")
      throw new Exception()
    }*/
  }

  override def getCurrentTruePosition: Array[Int] = Array(bot.pos.x, bot.pos.y)

  override def getCurrentReading: Array[Int] = bot.reading match {
    case Some((x, y)) => Array(x, y)
    case None => null
  }

  override def getCurrentProb(x: Int, y: Int): Double = {
    val startIndex: Int = grid.indexOf((x, y)) * 4
    sum(f.slice(startIndex, startIndex + 4))
  }

  override def getOrXY(rX: Int, rY: Int, x: Int, y: Int): Double = return (rX, rY) match {
    case (_, -1) | (-1, _) | (-1, -1) => noReadingProb(grid.indexOf((x, y)))
    case (rX, rY) => sensorProb(grid.indexOf((rX, rY)))(grid.indexOf((x, y)))
  }


  override def getTProb(x: Int, y: Int, h: Int, nX: Int, nY: Int, nH: Int): Double =
    T(grid.indexOf((x, y)) * dirs + h, grid.indexOf((nX, nY)) * dirs + nH)

  private def sensorReadingToIndex(reading: Option[(Int, Int)]): Int =
    reading match {
      case Some((x, y)) => x * bot.cols + y
      case None => O.length - 1
    }
}
