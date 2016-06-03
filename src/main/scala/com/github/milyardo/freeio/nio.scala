package com.github.milyardo.freeio

import com.github.milyardo.freeio.file._

import scalaz.concurrent.Task
import scalaz.~>

import java.nio.file.{Path => JPath, OpenOption, Files, Paths}

/**
  * Created by zpowers on 3/12/16.
  */
object nio extends FileAlg[JPath, JPath, Nothing] {

  def withJavaNIO(options: OpenOption): FileOp ~> Task = new (FileOp ~> Task) {
    import Task.delay

    override def apply[A](fa: FileOp[A]): Task[A] = fa match {
      case Create(path)          => delay(touch(path))
      case Read(path)            => delay(Files.readAllBytes(path))
      case Write(path, contents) => delay(Files.write(path, contents, options))
    }
  }

  def touch(jPath: JPath): JPath = {
    if (Files.exists(jPath)) {
      jPath
    } else {
      Files.createFile(jPath)
    }
  }
}
