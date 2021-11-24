package beginners

object Arguments extends App {

  def efficientFac(n: BigInt, acc: BigInt = 1): BigInt = {
    if (n <= 1) acc
    else efficientFac(n-1, n*acc)
  }
  println(efficientFac(3)) //if no value for acc, then it uses 1

  def savePicture(format: String = "jpg", x: Int, y: Int): Unit = {
    println(s"$format type photo of area " + x*y)
  }
  savePicture("jpg", 800, 600)
  //solution to the early/all default argument problem
  /* 1. pass in every leading argument
  *  2. name the arguments*/

  savePicture(x = 900, y = 2000)
  //now compiler knows which number goes to which argument
  //you can also pass the args in whatever order you want this way
  savePicture(y =2, format = "png", x =34)
}
