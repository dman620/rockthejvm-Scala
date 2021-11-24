package beginners

object Example extends App {
  //my version of tail recursive efficientFib()
  def efficientFib(n: BigInt): BigInt  = {
    def fibHelper(n: BigInt, a: (BigInt, BigInt)): BigInt = {
      if(n <= 0) a._1
      else if(n == 1) a._2
      else fibHelper(n-1, (a._2, a._2 + a._1))
    }
    fibHelper(n-1, (0, 1))
  }
}
