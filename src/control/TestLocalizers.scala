package control
import model._
import GridUtils._
import java.io._

import scala.collection.mutable.Queue
import scala.util.Random

object TestLocalizers {
  def run(size: Int = 8, name: String = "out.txt"): Unit = {
    def testRun(localizer: EstimatorInterface, iter: Int = 1000): Double = {
      def predict(localizer: EstimatorInterface) =
        Grid(size, size).maxBy[Double](p => localizer.getCurrentProb(p.x, p.y))
      def actual(localizer: EstimatorInterface) = {val a = localizer.getCurrentTruePosition; (a(0), a(1))}
      val errors = Queue[Int]()
      for (_ <- 1 to iter) {
        localizer.update()
        errors += predict(localizer) mdist actual(localizer)
      }
      errors.sum.toDouble / iter
    }
    val guessAvg = testRun(new RandomLocalizer(size))
    val readAvg = testRun(new SensorLocalizer(size))
    val modelAvg = testRun(new AwesomeLocalizer(size))

    val file = new FileWriter(name, true)
    file.write(f"Run ${Random.nextInt}%x\n"
      + "-"*30 + "\n"
      + f"random: $guessAvg%.3f\n"
      + f"reading: $readAvg%.3f\n"
      + f"model: $modelAvg%.3f\n"
      + "-"*30 + "\n")
    file.flush
    file.close
  }
}
