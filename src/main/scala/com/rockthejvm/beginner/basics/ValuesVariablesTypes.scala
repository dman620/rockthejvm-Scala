package beginners

object ValuesVariablesTypes extends App {
  val x = 42
  //vals are more like intermediate computations that constants
  println(x)

  val aString: String = s"hello Scala $x"
  println(aString)

  val aBoolean: Boolean = false
  val aChar: Char = 'a'
  val anInt: Int = 3
  var aShort: Short = 345
  var aLong: Long = 9999999


  //variables
  var aVariable: Int = 4
  aVariable = 31
  //used for side effects (see what our programs are doing)
  //in functional, we work with varls more than vars


}
