package model

import scala.util.Random
import GridUtils._

/**
  * Created by ine13kbe on 28/02/17.
  */
class BotSimulator(val rows: Int, val cols: Int) {
  import BotSimulator._
  def this(both: Int = 4) = this(both, both)

  val turnChance: Double = 0.3
  var pos: (Int, Int) = (Random.nextInt(rows), Random.nextInt(cols))
  var direction: Int = Random.nextInt(4)
  var reading: Option[(Int, Int)] = None

  def update(): Option[(Int, Int)] = {
    val oldDir = direction
    val chance: Double = Random.nextDouble()
    turnAround()
    def turnAround(): Unit = {
      if (outOfBounds(pos + dirVector(direction))  || (chance > turnChance && direction == oldDir)){
        direction = Random.nextInt(4)
        turnAround()
      } else {
        return
      }
    }
    def outOfBounds(pos1: (Int, Int)): Boolean = {
      val (rPos, cPos) = pos1
      cPos < 0 || cPos >= cols || rPos < 0 || rPos >= rows
    }
    pos = pos + dirVector(direction)
    var sensorReading = pos
    reading = None
    val posChance: Double = Random.nextDouble()
    if(posChance < 0.1) {

    } else if(posChance < 0.5) {
      while((sensorReading cdist pos) != 1) {
        sensorReading = pos + (Random.nextInt(3) - 1, Random.nextInt(3) - 1)
      }
    } else if(posChance < 0.9) {
      while((sensorReading cdist pos) != 2){
        sensorReading = pos + (Random.nextInt(5) - 2, Random.nextInt(5) - 2)
      }
    } else {
      return None
    }
    if(outOfBounds(sensorReading)){
      return None
    }
    reading = Some(sensorReading)
    return reading
  }

  override def toString(): String = "row: " + pos._1 + " col: " + pos._2
}

object BotSimulator {
  val UP = (-1, 0)
  val RIGHT = (0, 1)
  val DOWN = (1, 0)
  val LEFT = (0, -1)
  val dirVector = Vector(UP, RIGHT, DOWN, LEFT)
  val dirs = 4
}
