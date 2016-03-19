package com.github.milyardo.freeio

import java.nio.file.StandardOpenOption

/**
  * Created by zpowers on 3/12/16.
  */
object FileApp extends App {
  import file._

  val prog = for {
    aFile <- create("aFile.txt")
    contents <- read(aFile)
  } yield contents.map(_.toChar).mkString(",")

  val prog2 = for {
    bFile <- create("bFile.txt")
    res <- write(bFile, "Goodbye, World!".getBytes)
    contents <- read(bFile)
  } yield contents.map(_.toChar).mkString(",")


  {
    import jio._

    println(
      benchMark(prog.foldMap(withJavaIO).unsafePerformSyncAttempt)
    )
    println(
      benchMark(prog2.foldMap(withJavaIO).unsafePerformSyncAttempt)
    )
  }

  {
    import nio._

    println(
      benchMark(prog.foldMap(withJavaNIO(StandardOpenOption.CREATE)).unsafePerformSyncAttempt)
    )
    println(
      benchMark(prog2.foldMap(withJavaNIO(StandardOpenOption.CREATE)).unsafePerformSyncAttempt)
    )
  }

  {
    import pure._
    val files = Map(
      "aFile.txt" -> "Hello".getBytes,
      "bFile.txt" -> "Goodbye".getBytes
    )
    println(
      benchMark(prog.foldMap(withPureFiles(files)))
    )
    println(
      benchMark(prog2.foldMap(withPureFiles(files)))
    )
  }

  def benchMark[A](a: => A) = {
    val start = System.nanoTime()
    val res = a
    val end = System.nanoTime()
    (a, end - start)
  }
}