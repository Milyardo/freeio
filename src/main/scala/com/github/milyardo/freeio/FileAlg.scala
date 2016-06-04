package com.github.milyardo.freeio

import scalaz.{:<:, Free}
import scalaz.Free._

/**
  * Created by zpowers on 3/12/16.
  */
trait FileAlg[File, Path, Cursor] {
  sealed trait FileOp[A]
  case class Create(path: Path)                    extends FileOp[File]
  case class Read(f: File)                         extends FileOp[Array[Byte]]
  case class Write(f: File, contents: Array[Byte]) extends FileOp[File]

  type FileIO[A] = Free[FileOp, A]

  //Smart constructors for FileIO
  def create(path: Path): FileIO[File]                    = liftF(Create(path))
  def read(fileP: File): FileIO[Array[Byte]]              = liftF(Read(fileP))
  def write(f: File, contents: Array[Byte]): FileIO[File] = liftF(Write(f, contents))

  /**
    * Module for lifting file operations into a a more general coproduct.
    */
  object IO {
    implicit def instance[F[_]](implicit I: FileOp :<: F) = new IO[F]
  }
  class IO[F[_]](implicit Inject: FileOp :<: F) {
    type FF[A] = Free[F, A]
    def lift[A](a: FileOp[A]) = Free.liftF(Inject.inj(a))

    def create(path: Path): FF[File]                    = lift(Create(path))
    def read(fileP: File): FF[Array[Byte]]              = lift(Read(fileP))
    def write(f: File, contents: Array[Byte]): FF[File] = lift(Write(f, contents))
  }
}
