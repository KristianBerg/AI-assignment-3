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
  val sensorProb = (for(reading <- grid) yield for(position <- grid) yield
    if((reading.cdist(position)) == 2) 0.025
    else if ((reading.cdist(position)) == 1) 0.05
    else if (reading == position) 0.1
    else 0)

  val O = for(row <- sensorProb) yield
    new DenseVector(row.toArray.flatMap(x => List.fill(4)(x/4)));

  val T = new DenseMatrix(states.length, states.length,
    (for(from <- states; to <- states) yield
      from + to).toArray)

  var f = DenseVector.fill(states.length){1/states.length}
  var alpha = 0

  override def getNumRows: Int = ???

  override def getNumCols: Int = ???

  override def getNumHead: Int = ???

  override def update(): Unit = ???

  override def getCurrentTruePosition: Array[Int] = Array(bot.pos.x, bot.pos.y)

  override def getCurrentReading: Array[Int] = ???

  override def getCurrentProb(x: Int, y: Int): Double = ???

  override def getOrXY(rX: Int, rY: Int, x: Int, y: Int): Double = ???

  override def getTProb(x: Int, y: Int, h: Int, nX: Int, nY: Int, nH: Int): Double = ???

  private def alphaUpdate() =
    alpha = 0

    for (i <- 0 to states.length){
      //alpha += f(i) + sensorProb
    }

}
