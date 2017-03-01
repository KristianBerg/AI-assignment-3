package model

/**
  * Created by erik on 3/1/17.
  */
object GridUtils {
  implicit class Point(val coord: (Int, Int)) {
    def x() = coord._1
    def y() = coord._2

    def -(o: Point) = (x - o.x, y - o.y)
    def +(o: Point) = (x + o.x, y + o.x)

    import math._
    def cdist(o: Point) = max(abs(x - o.x), abs(y - o.y));
  }
}
