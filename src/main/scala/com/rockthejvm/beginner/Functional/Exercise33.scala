package com.rockthejvm.beginner.Functional

import scala.annotation.tailrec
import scala.collection.immutable.{Map, Set}


case class Person(name: String)

abstract class AbstractNetwork {
  //add a person to the network
  def add(person: Person, friends: Set[Person]): AbstractNetwork

  //remove a person from the network
  def remove(person: Person): AbstractNetwork

  //two people friend each-other (mutual)
  //usage mapName.friend(person1, person2) //both are added to each other's list
  def friend(person1: Person, person2: Person): AbstractNetwork

  //two people un-friend each-other (mutual)
  def unFriend(person1: Person, person2: Person): AbstractNetwork

  //number of friends of a person
  def numFriends(person: Person): Int

  //person with the most friends
  def mostFriends(): Person

  def socialConnection(person1: Person, person2: Person): Boolean

  override def toString: String = printElements

  def printElements: String
}

case class MyNetwork(data: Map[Person, Set[Person]] = Map()) extends AbstractNetwork {
  def add(person: Person, friends: Set[Person] = Set()): MyNetwork = new MyNetwork(data + (person -> friends))

  //at the moment, this method does not work because it will not remove the removed person
  //from others' friend lists
  def remove(person: Person): AbstractNetwork = new MyNetwork(data.removed(person))

  def friend(person1: Person, person2: Person): MyNetwork = {
    if (!data.contains(person1) || !data.contains(person2)) this
    else new MyNetwork(data + (person1 -> (data(person1) + person2)) + (person2 -> (data(person2) + person1)))
  }

  def unFriend(person1: Person, person2: Person): MyNetwork = new MyNetwork(data + (person1 -> (data(person1) - person2)) + (person2 -> (data(person2) - person1)))

  def numFriends(person: Person): Int = {
    @tailrec
    def go(network: Map[Person, Set[Person]]): Int = {
      if (network.isEmpty) 0
      else if (network.head._1 == person) network.head._2.size
      else go(network.tail)
    }

    go(this.data)
  }

  def mostFriends(): Person = {
    @tailrec
    def go(network: Map[Person, Set[Person]], currentPerson: Person, currentMax: Int): Person = {
      if (network.isEmpty) currentPerson
      else {
        val newPerson = network.head._1
        val newCount = numFriends(newPerson)
        if (newCount > currentMax) go(network.tail, newPerson, newCount)
        else go(network.tail, currentPerson, currentMax)
      }
    }

    go(this.data, this.data.head._1, numFriends(this.data.head._1))
  }

  //doesn't work, just returns true
  def socialConnection(person1: Person, person2: Person): Boolean = {
    def bfs(target: Person, consideredPeople: Set[Person], discoveredPeople: Set[Person]): Boolean = {
      if(discoveredPeople.isEmpty) false
      else {
        val person = discoveredPeople.head
        if(person == target)true
        else if(consideredPeople.contains(person)) bfs(target,consideredPeople, discoveredPeople.tail)
        else bfs(target, consideredPeople + person, discoveredPeople.tail ++ data(person))
      }
    }
    bfs(person2, Set(), data(person1) + data(person2).head)
  }

  override def printElements: String = {
    @tailrec
    def go(network: Map[Person, Set[Person]], accumulatedString: String): String = {
      if (network.isEmpty) accumulatedString
      else go(network.tail, accumulatedString + network.head._1.name + "(" + numFriends(network.head._1) + "):" + "\n" + go2(network.head._2, ""))
    }

    @tailrec
    def go2(friendsList: Set[Person], friendsListString: String): String = {
      if (friendsList.isEmpty) friendsListString
      else go2(friendsList.tail, friendsListString + "    >" + friendsList.head.name + "\n")
    }

    go(this.data, "")
  }
}

object Exercise33 extends App {
  //what happens if you run map to lowercase for a map like ("Jim", 333), ("JIM", 325) ?
  /*  val jims = Map[String, Int]("Jim" -> 333, "JIM" -> 321)
    println(jims.map(pair => pair._1.toLowerCase -> pair._2)) // the first pair is essentially deleted by this action*/


  /*  def add(network: Map[Person, Set[Person]], person: Person): Map[Person, Set[Person]] = network + (person -> Set())

    def remove(network: Map[Person, Set[Person]], person: Person): Map[Person, Set[Person]] = network.removed(person)

    def friend(network: Map[Person, Set[Person]], person1: Person, person2: Person): Map[Person, Set[Person]] = {
      network + (person1 -> (network(person1) + person2)) + (person2 -> (network(person2) + person1))
    }

    def unfriend(network: Map[Person, Set[Person]], person1: Person, person2: Person): Map[Person, Set[Person]] = {
      network + (person1 -> (network(person1) - person2)) + (person2 -> (network(person2) - person1))
    }
    def printElements(network: Map[Person, Set[Person]]): Unit = {
      def go(network: Map[Person, Set[Person]], accumulatedString: String): String = {
        if(network.isEmpty) accumulatedString
        else go(network.tail, accumulatedString + network.head._1.name +":\n" + go2(network.head._2, ""))
      }
      def go2(friendsList: Set[Person], friendsListString: String): String ={
        if(friendsList.isEmpty) friendsListString
        else go2(friendsList.tail, friendsListString + "    >" + friendsList.head.name + "\n")
      }
      println(go(network, ""))
    }*/


  val derek = new Person("Derek")
  val paige = new Person("Paige")
  val david = new Person("David")
  val andrew = new Person("Andrew")
  val mike = new Person("Mike")
  val jack = new Person("Jack")
  val frank = new Person("frank")


  val network: MyNetwork = MyNetwork().add(derek).add(paige).add(david).add(andrew).add(mike).add(jack) //frank not added to network
  val friendedNetwork: MyNetwork = network.friend(derek, paige).friend(derek, david).friend(derek, andrew).friend(derek, mike)
    .friend(paige, andrew).friend(paige, jack).friend(david, andrew).friend(david, mike).friend(david, jack).friend(andrew, mike).friend(andrew, jack)
    .friend(mike, frank)
  println("Unfriended Network:\n" + network)
  println("Friended Network:\n" + friendedNetwork)
  val networkWithSomeFriendsRemoved = friendedNetwork.unFriend(derek, jack).unFriend(mike, andrew).unFriend(andrew, paige)
  //println("Network with some friends removed:\n" + networkWithSomeFriendsRemoved)
  println(friendedNetwork.numFriends(frank))

  println(friendedNetwork.mostFriends())

  println(friendedNetwork.socialConnection(derek, david))
}