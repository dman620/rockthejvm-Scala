package com.rockthejvm.advanced.lectures.part3concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Random, Success} //important for Futures (handles thread allocation)

object FuturesAndPromieses extends App {
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife
  } //the global implicit is injected by the compiler here automatically

  println(aFuture.value) //Option[Try[Int]]
  println("Waiting on the future")
  aFuture.onComplete(t => t match {
    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
    case Failure(exception) => println(s"I have failed! with $exception")
  }) //called by SOME thread. no guarantees
  Thread.sleep(3000)

  //mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) =
      println(s"${this.name} poked ${anotherProfile.name}")
  }

  object SocialNetwork {
    //database
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.3-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )
    val random = new Random

    //API
    def fetchProfile(id: String): Future[Profile] = {
      Thread.sleep(random.nextInt(300))
      Future(Profile(id, names(id)))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = {
      Thread.sleep(random.nextInt(400))
      val bfID = friends(profile.id)
      Future(Profile(bfID, names(bfID)))
    }
  }

  //client: mark to poke his best friend (the complex way, avoid this method)
/*  val mark: Future[Profile] = SocialNetwork.fetchProfile("fb.id.1-zuck")
  mark.onComplete {
    case Success(markProfile: Profile) => {
      val bestFriend = SocialNetwork.fetchBestFriend(markProfile)
      bestFriend.onComplete {
        case Success(bestFriendProfile) => markProfile.poke(bestFriendProfile)
        case Failure(e) => e.printStackTrace()
      }
    }
    case Failure(e) => e.printStackTrace()
  }
  Thread.sleep(2000)*/

  //functional composition of futures
  //map, flatmap, filter
  //val nameOnTheWall = mark.map(profile => profile.name)
  //if the original future fails with an exception, the mapped future will fail with the same exception

  //val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

 // val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  //for-comprehensions
  //this accomplishes the same thing as the complex way above
  for{
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } yield mark.poke(bill)
  Thread.sleep(1000)

  //fallbacks for when things to south
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknownID").recover{
    case _: Throwable => Profile("fb.id.0-dummy", "ERROR")
  }

  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknownID").recoverWith{
    case _: Throwable => SocialNetwork.fetchProfile("fb.id.3-dummy")
  }

  val fallbackResult = SocialNetwork.fetchProfile("unknownID").fallbackTo(SocialNetwork.fetchProfile("fb.id.3-dummy"))

  //PART 3:
  //how to lock futures
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp{
    val name = "Rock The JVM banking"
    def fetchUser(name: String): Future[User] = Future{
      //simulate fetching from the DB
      Thread.sleep(500)
      User(name)
    }
    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future{
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }
    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      //fetch user
      //make sure user has enough
      //create transaction from user to merchant
      //wait for transaction to finish
      val transactionStatusFuture = for{
        user <- fetchUser(username)
        transaction <- /*if user has enough*/ createTransaction(user, merchantName, cost)
      } yield transaction.status
      Await.result(transactionStatusFuture, 2.seconds)
    }
  }
  println(BankingApp.purchase("Derek", "computer", "microcenter", 2000))

  //promises
  val promise = Promise[Int]()
  val future = promise.future

  //thread 1 the consumer
  future.onComplete({
    case Success(r) => println(s"[consumer] I've received ${r}")
  })

  //thread 2 the producer
  val producer = new Thread(() => {
    println("[producer] crunching numbers")
    Thread.sleep(1000)
    //fulfilling the promise
    promise.success(42)
    //to give a failure, use promise.failure(exception)
    println("[producer] I have produced 42")
  })
  producer.start()
  Thread.sleep(1000)
}
