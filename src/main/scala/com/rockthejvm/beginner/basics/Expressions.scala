package beginners

object Expressions extends App {
  val x = 1 + 2 // expression
  println(x)
  println(2 + 3 * 4)
  /*
  * & and
  * | or
  * ^ excl or
  * << left shift
  * >> right shift
  * >>> right shift with zero extension
  * */
  println(1 == x)
  //== != < > <= >=
  println(!(1 == x))

  var aVariable = 2
  aVariable += 3 //also works with -= *= /=  ...all side effects
  println(aVariable) //only works with var

  //Instructions (DO) vs Expressions (calculate a value)

  //IF expression
  println(if(true)5 else 3)

  var i = 0

  //while expressions and assignment expressions return Unit
  //side effects are println(), whiles, reassigning

  //code blocks
  val aCodeBlock = {
    val y = 2
    val z = y + 1
    if(z>2) "hello" else "goodbye"
  }
  //that is called a code block
  //code blocks are expressions
  //value of the block is the value of its last expression
  //val/var inside code block are only valid in that scope

  //instructions are executed(like in java), expressions are evaluated(scala)
  //do not use loops lol
  //in scala we think in terms of expressions, not instructions


}
