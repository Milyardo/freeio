package com.github.milyardo.freeio

import com.github.milyardo.freeio.file._

import scalaz.concurrent.Task
import scalaz.~>

import java.nio.file.{Path => JPath, OpenOption, Files, Paths}

/**
  * Created by zpowers on 3/12/16.
  */
object nio {

  private[nio] case class NIOPath(path: JPath) extends Path
  private[nio] case class NIOFileHandle(jPath: JPath) extends File

  def withJavaNIO(options: OpenOption): FileOp ~> Task = new (FileOp ~> Task) {
    import Task.delay

    override def apply[A](fa: FileOp[A]): Task[A] = fa match {
      case Create(StringPath(path)) => delay {
        NIOFileHandle(touch(Paths.get(path)))
      }
      case Create(NIOPath(jpath)) => delay {
        NIOFileHandle(touch(jpath))
      }
      case Read(NIOFileHandle(jpath)) => delay { Files.readAllBytes(jpath) }
      case Write(NIOFileHandle(jpath), contents) => delay {
        NIOFileHandle(Files.write(jpath,contents,options))
      }
      case op => sys.error(s"$op is not supported by java.io.")
    }
  }

  def touch(jPath: JPath): JPath = {
    if (Files.exists(jPath)) {
      jPath
    } else {
      Files.createFile(jPath)
    }
  }

  def writeWholeFile(jPath: JPath, contents: Array[Byte]) = Files.write(jPath,contents)
}