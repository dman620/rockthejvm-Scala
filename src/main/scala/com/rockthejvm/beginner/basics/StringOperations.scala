package beginners

object StringOperations extends App {
  val str: String = "Hello, I am learning Scala"
  println(str.charAt(2))//return char at index 2 (start at index 0)
  println(str.substring(7, 11))
  println(str.split(" ").toList) //splits words into a list, separated by the regex
  println(str.startsWith("Hello"))
  println(str.replace('l', 'd'))
  println(str.toUpperCase())
  println(str.length)

  val aNumberString = "2"
  val aNumber = aNumberString.toInt
  println('a' +: aNumberString :+ 'z')
  println(str.reverse)
  println(str.take(2)) //takes the first 2 characters and returns as a string

  //Scala-specific: string interpolators
  //s-interpolators:
  val name = "David"
  val age = 12
  val greeting = s"Hi, my name is $name and I'm $age years old"
  val turningGreeting = s"Hi, my name is $name and I will be turning ${age + 1} years old"
  println(turningGreeting)

  //f-interpolators
  //printf-like format
  val speed = 1.2f
  val myth = f"$name can eat $speed%2.2f burgers per minute"
  println(myth)

  val x = 1.1f
  //val str = f"$x%3d" <--compiler error!  the d requires that the parameter is type Int

  //raw interpolator
  println(raw"This is a \nnewline") //the \n is not escaped becaues of the raw
  //alternatively: this approach does escape the characters:
  val escaped = "This is a \nnewline"
  println(raw"$escaped")
}
