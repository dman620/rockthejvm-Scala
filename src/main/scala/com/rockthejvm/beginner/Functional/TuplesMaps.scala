package com.rockthejvm.beginner.Functional

object TuplesMaps extends App {
  //tuples = finite ordered "lists"
  val aTuple = new Tuple2(2, "hello, scala")//Tuple2[Int, String] = (Int, String)
  //                                                                ^ this is syntax sugar for the one on the left
  //tuples can at most have 22 elements (remember Function1 - Function22
  println(aTuple._1) //2
  println(aTuple._2) //"hello, scala"
  println(aTuple.copy(_2 = "goodbye, java"))
  println(aTuple.swap) //swaps elements in place ("hello, scala", 2)

  //maps - keys -> values
  val aMap: Map[String, Int] = Map()
  val phoneBook = Map(("Brenda", 555), ("Rick", 646), "Derek" -> 636).withDefaultValue(-1)//returns -1 if we use apply() with a key that does not exist
  //the default value does not have to be the same type as the tuple (Int in this case)  I was able to use Boolean
  // you can use , or ->
  //if you use -> you do not need to use () around the tuple.  You can mix/match as you see above

  println(phoneBook)

  //map operations
  println(phoneBook.contains("Derek")) // true
  println(phoneBook("Rick")) //646
  println(phoneBook("Bob SAGGET")) //-1, because we have .withDefaultValue(-1)

  //add a pairing
  val newPair = "Mary" -> 888
  val newPhonebook = phoneBook + newPair
  val newestPhonebook = newPhonebook + ("Andrew" -> 434) + ("Drax" -> 562) //the parenthesis are very important here
  println(newestPhonebook)

  //functionals on maps
  //map, flatmap, filter
  println(newestPhonebook.map(pair => pair._1.toLowerCase -> pair._2))

  //filterKeys;
  println(newestPhonebook.view.filterKeys(x => x.startsWith("D")).toMap)
  //mapvalues
  println(newestPhonebook.view.mapValues(number => number * 10).toMap)

  //conversions to other collections
  println(newestPhonebook.toList)
  println(List("Daniel" -> 545).toMap)
  val names = List("Bob", "Andrew", "Alex", "Fernandez", "Franklin", "Frank", "Christine", "Jon", "Caroline")
  println(names.groupBy(name => name.charAt(0)))

}
