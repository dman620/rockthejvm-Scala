package beginners

import jdk.internal.util.xml.impl.Pair

object Recursion extends App{
  def factorial(n: Int): Int = {
    if(n<=1) 1
    else n * factorial(n-1)
  }

  def anotherFactorial(n: Int): Int = {
    def factorialHelper(m: Int, accumulator: Int): Int = {
      if(m<1) accumulator
      else factorialHelper(m-1, m*accumulator)
    }
    factorialHelper(n, 1)
  }

  //my version of tail recursive efficientFib()
  def efficientFib(n: BigInt): BigInt  = {
    def fibHelper(n: BigInt, a: (BigInt, BigInt)): BigInt = {
      if(n <= 0) a._1
      else if(n == 1) a._2
      else fibHelper(n-1, (a._2, a._2 + a._1))
    }
    fibHelper(n-1, (0, 1))
  }

  println(efficientFib(231452))
  //0, 1, 1, 2, 3, 5, 8, 13, 21

  /*
  * 1. string concatenation function using tail recursion
  * 2. IsPrime tail recursive
  * 3. fibonacci function tail recursive (already did it above)
  * */

  //PROBLEM 1: string concatenation

  def strCat(str1: String, num: Int): String = {
    def go(m: Int, accumulatedString: String): String = {
      if(m <= 0) " "
      else if (m == 1) accumulatedString
      else go(m - 1, accumulatedString + str1)
    }
    go(num, str1)
  }
  println(strCat("HEY, ", 2321))

  //PROBLEM 2: isPrime
  def efficientIsPrime(n: Int): Boolean = {
    def go(m: Int, a: Boolean): Boolean = {
      /* case 1: a is false, return false
      *  case 2: m is reduced to 1, this means no numbers got a false accumulator return true
      *  case 3: recurse - reduce m, pass prime check as boolean (n%m != 0)
      * */
      if(!a) false
      else if(m <= 1) true
      else go(m-1, n%m != 0)
    }
    go(n/2, true)
  }
  println(efficientIsPrime(11))

  /*When trying to draw out recursive functions, try to do this:
    * Think about what will be passed during the recursive case.  When using a helper function, the else clause will only have a recursive call and nothing else.  Think about what values will be passed.
    *     usually just a decrement and then some extra data
    * Then draw out other cases.  There will be one or more exit cases, depending on the problem.  How will the function exit?  On what values will it exit and what values will be returned?
   */
}
