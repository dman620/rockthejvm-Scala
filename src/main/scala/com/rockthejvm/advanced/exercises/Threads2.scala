package com.rockthejvm.advanced.exercises

object Threads2 extends App {
  /*
  * 1. think of an example  when notifyAll() would act differently that notify()
  * 2. create a deadlock - threads are all blocked, and none can continue
  * 3. create a livelock - threads yield execution to each other in such a way nobody can continue (not blocked, but cannot continue)*/

  //1 notifyAll
  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized{
        println(s"[thread<$i>] waiting...")
        bell.wait()
        println(s"[thread<$i>] hooray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock 'n Roll!")
      bell.synchronized{
        bell.notifyAll()
      }
    }).start()
  }
  //testNotifyAll()



  //2 deadlock
  case class Friend(name: String){
    def bow(other: Friend) = {
      this.synchronized {
        println(s"$name: I am bowing to my friend ${other.name}")
        other.rise(this)
        println(s"$name: my friend ${other.name} has risen")
      }
    }
    def rise(other: Friend) = {
      this.synchronized{
        println(s"$name: I am rising to my friend ${other.name}")
      }
    }
  }
  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

  new Thread(() => sam.bow(pierre)).start()
  new Thread(() => pierre.bow(sam)).start()
}
