package beginners

object Functions extends App {
  def aFunction(a: String, b: Int): String = {
    a + " " + b
  }

  println(aFunction("hello", 3))

  //in scala 2 you can define the func with or w/o ().
  //and you can call it with or without ()
  //but in scala 3, if you def with () you must call with () and vice versa without ()
  def aParameterlessFunction: Int = 42
  //println(aParameterlessFunction())
  println(aParameterlessFunction)
  def aRepeatedFunction(str: String, n: Int): String = {
    if (n==1) str
    else str + aRepeatedFunction(str, n-1)
  }
  println(aRepeatedFunction("hello", 3))

  def printList(l: List[Int]): Unit = {
    if(l.size == 1) println(l.head)
    else{
      println(l.head)
      printList(l.tail)
    }
  }
  //YOU must tell the compiler what the return time is if it is recursive
  printList(List(1,2,3,4,5))
  //when you need to iterate, use recursion (like printList example)

  //nested functions, called auxilary functions
  def aBigFunction(n: Int): Int = {
    def aSmallerFunction(a: Int, b: Int): Int = a+b

    aSmallerFunction(n, n-1)
  }

  /*
  * 1. a greeting function (name, age) => hi, my name is $name and I am $age years old
  * 2. factorial function - computes full product of all nums up to given num
  * 3. a fibonacci function - find nth fibonacci number
  *   sequence: 1, 1, 2, 3, 5, 8,
  *   f(n) = f(n-1) + f(n-2)
  * 4. a func that tests if a number is prime
  */

  //PROBLEM 1: a greeting function
  def greeting(name: String, age: Int): String ={
    s"Hi, my name is $name and my age is $age"
  }
  println(greeting("Derek", 27))

  //PROBLEM 2: factorial
  def factorial(n: Int): Int = {
    if(n<=1) 1
    else n * factorial(n-1)
  }
  println(factorial(30))

  //PROBLEM 3: fibonacci
  def fibonacci(n: Int): Int = {
    if(n <= 2) 1
    else fibonacci(n - 1) + fibonacci(n - 2)
  }
  println(fibonacci(12))

  //PROBLEM 4: prime number
  def isPrime(n: Int): Boolean = {
    def isPrimeUntil(m: Int): Boolean = {
      if (m <= 1) true
      else n % m != 0 && isPrimeUntil(m-1)
    }
    isPrimeUntil(n/2)
  }
  println(isPrime(17))
  //PROBLEM 5: even number
  def isEven(n: Int): Boolean = { //only works if n is > 0
    if(n == 1) false
    else if(n == 2) true
    else isEven(n-2)
  }
  println(isEven(90))

}
