package cn.id55503.example.akka.actor

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

object Test {
  def main(args: Array[String]): Unit = {
    println("Test")
    val test: Test = new Test
    println(test.getArray(1,2,3))
    println(test.getT(1, 2, 3))
    Array.apply(Array(1, 2, 3))
  }
}

class Test {
  def getArray[T: ClassTag](t: T*): Array[T] = {
    for (i <- t) {
      println(i)
    }
    Array.empty
  }

  def getT[T](t: T*) = {
    ArrayBuffer.apply(t)
  }
}
