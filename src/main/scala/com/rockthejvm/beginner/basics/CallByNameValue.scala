package beginners

object CallByNameValue extends App {
  def calledByValue(x: Long): Unit = {
    println(s"by value: $x")
    println(s"by value: $x")
  }
  def calledByName(x: => Long): Unit = {
    println(s"by value: $x")
    println(s"by value: $x")
  }
  calledByValue(System.nanoTime())
  calledByName(System.nanoTime()) //prints 2 different times, because it re-calls System.nanotime()

  def infinite(): Int = 1 + infinite() //if evaluated, this will cause a stack overflow
  def printFirst(x: Int, y: => Int) = println(x)

  //printFirst(infinite(), 34) //infinite is evaluated and immediately overflows
  printFirst(34, infinite()) //here, infinite() is called by name and since printFirst() does not evaluate that parameter, infinite() is never evaluated.

   /* CALL BY VALUE:
    *  -value is computed before call
    *  -same value used everywhere in the function
    *
    * CALL BY NAME (reference):
    *  -expression is literally passed into the function
    *  -expression is evaluated whenever it is used
    */
}
