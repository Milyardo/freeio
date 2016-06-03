package com.github.milyardo.freeio

import com.github.milyardo.freeio.file._

import scalaz.{Id, ~>}

/**
  * Created by zpowers on 3/12/16.
  */
object pure extends FileAlg[PureFile, String, Nothing] {

  def withPureFiles(files: Map[String, Array[Byte]]): FileOp ~> Id.Id = new (FileOp ~> Id.Id) {
    import Id._
    override def apply[A](fa: FileOp[A]): Id[A] = fa match {
      case Create(path)          => PureFile(files.getOrElse(path, Array.empty))
      case Read(PureFile(bytes)) => bytes
      case Write(_, contents)    => PureFile(contents)
    }
  }
}

case class PureFile(contents: Array[Byte])
