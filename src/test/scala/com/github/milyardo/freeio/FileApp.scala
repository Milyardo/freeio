package com.github.milyardo.freeio

import java.nio.file.{Paths, StandardOpenOption}

/**
  * Created by zpowers on 3/12/16.
  */
object FileApp extends App {

  def prog[F,P,C](fileAlg: FileAlg[F,P,C])(find: String => P) = {
    import fileAlg._
    for {
      aFile <- create(find("aFile.txt"))
      contents <- read(aFile)
    } yield contents.map(_.toChar).mkString(",")
  }

  def prog2[F,P,C](fileAlg: FileAlg[F,P,C])(find: String => P) = {
    import fileAlg._

    for {
      bFile <- create(find("bFile.txt"))
      res <- write(bFile, "Goodbye, World!".getBytes)
      contents <- read(bFile)
    } yield contents.map(_.toChar).mkString(",")
  }

  {
    println(
      benchMark(prog(jio)(identity).foldMap(jio.withJavaIO).unsafePerformSyncAttempt)
    )
    println(
      benchMark(prog2(jio)(identity).foldMap(jio.withJavaIO).unsafePerformSyncAttempt)
    )
  }

  {
    println(
      benchMark(prog(nio)(n => Paths.get(n)).foldMap(nio.withJavaNIO(StandardOpenOption.CREATE))
        .unsafePerformSyncAttempt)
    )
    println(
      benchMark(prog2(nio)(n => Paths.get(n)).foldMap(nio.withJavaNIO(StandardOpenOption.CREATE))
        .unsafePerformSyncAttempt)
    )
  }

  {
    val files = Map(
      "aFile.txt" -> "Hello".getBytes,
      "bFile.txt" -> "Goodbye".getBytes
    )
    println(
      benchMark(prog(pure)(identity).foldMap(pure.withPureFiles(files)))
    )
    println(
      benchMark(prog2(pure)(identity).foldMap(pure.withPureFiles(files)))
    )
  }

  def benchMark[A](a: => A) = {
    val start = System.nanoTime()
    val res = a
    val end = System.nanoTime()
    (a, end - start)
  }
}