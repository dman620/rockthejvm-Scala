package com.rockthejvm.beginner.OOP

object Exceptions extends App {
  //val aWeirdValue: String = throw new NullPointerException
  //exceptions are instances of classes
  //they are of type Nothing
  //throwable classes extend the Throwable class
  //exceptions, and error are the major throwables
  //exceptions are something that went wrong with the program
  //errors are something that went wrong with the JVM

  //CATCHING EXCEPTIONS
  def getInt(withExceptions: Boolean): Int = {
    if(withExceptions) throw new RuntimeException("No int for you")
    else 42
  }

  //the value of a try block will depend on its contents.  If there are many potential types to be returned it might be AnyVal
  //if all cases return Int, then the value will be Int
  val potentialFailure = try{
    //code that might fail
    throw new NullPointerException
    getInt(true)
  }catch{
    //error handling
    //try to match the error type here
    case e: RuntimeException => println("caught a runtime exception")
    case e: NullPointerException => println("caught a null pointer exception")
  }finally{
    //code will get executed no matter what
    //this is optional
    //does not influence the return type of the expression
    //so use finally only for side effects (logging, printing, etc)
    println("finally")
  }

  // HOW TO DEFINE YOUR OWN EXCEPTIONS
/*  class MyException extends Exception
  val exception = new MyException

  throw exception*/


  //OOM error
  //val array = Array.ofDim(Int.MaxValue)

  //Stack Overflow error
  //def infinite: Int = 1 + infinite
  //val wat = infinite


  case object PocketCalc{
    class OverflowException extends Exception
    class UnderflowException extends Exception
    class DivideByZeroException extends Exception
    def add(x: Int, y: Int): Int = combine(x, y)
    def sub(x: Int, y: Int): Int = combine(x, -y)
    def combine(x: Int, y: Int): Int ={
      val overflowVal = BigInt(x) + BigInt(y)
      if( overflowVal > Int.MaxValue) throw new OverflowException
      else if(overflowVal < Int.MinValue) throw new UnderflowException
      else x + y
    }
    def mul(x: Int, y: Int): Int = {
      val overflowVal = BigInt(x) * BigInt(y)
      if( overflowVal > Int.MaxValue)throw new OverflowException
      else if(overflowVal < Int.MinValue) throw new UnderflowException
      else  x * y
    }
    def div(x: Int, y: Int): Float = if(y == 0) throw new DivideByZeroException else x.asInstanceOf[Float] / y.asInstanceOf[Float]

  }

  //println(PocketCalc.div(3, 34))
  //println(PocketCalc.add(1999999998, 1999999998))//throws OverflowException properly
  //println(PocketCalc.sub(-1999999998, 1999999998)) //throws UnderflowException properly
  //println(PocketCalc.mul(1999999998, 1999999998))//throws OverflowException properly
  //println(PocketCalc.div(45, 0)) //throws DivideByZeroException properly

}
