package model

import model.GridUtils.Point
//import scala.language.reflectiveCalls

object GridUtils {
  implicit class Point(val coord: (Int, Int)) {
    def x() = coord._1
    def y() = coord._2

    def -(o: Point) = (x - o.x, y - o.y)
    def +(o: Point) = (x + o.x, y + o.y)

    import math._
    def cdist(o: Point) = max(abs(x - o.x), abs(y - o.y))

    def moore(r: Int) = for(i <- x -r to x + r; j <- y -r to y + r if (this cdist (i, j)) == r) yield (i, j)
  }

  case class Grid(val h: Int, val w: Int) extends Seq[(Int, Int)] {
    def length: Int = h*w

    override def apply(i: Int) = (i / h, i % w)

    override def iterator: Iterator[(Int, Int)] = Iterator.tabulate(length)(this(_))

    override def toString = h + "× " + w + " grid"

    def contains(pt: (Int, Int)) = pt.x >= 0 && pt.x < h && pt.y >= 0 && pt.y < w
  }

  implicit class withIn[T](obj: T) {
    def in(seq: Seq[T]) = seq.contains(obj)
  }
}
