package cn.jxau.yuan.scala.learning.loop

/**
  * @author zhaomingyuan
  */
object Loop {

    def main(args: Array[String]): Unit = {
        val vector = Vector[Int](1, 2, 3)

        val v = Vector[Int](1, 2, 3)

        val a = vector :+ 4

        val b = v.+:(4)

        a.foreach(println)

        b.foreach(println)
    }
}
