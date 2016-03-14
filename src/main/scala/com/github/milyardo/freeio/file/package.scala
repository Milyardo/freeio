package com.github.milyardo.freeio

import scalaz.Free
import scalaz.Free.liftF

/**
  * Created by zpowers on 3/12/16.
  */
package object file {
  type FileIO[A] = Free[FileOp,A]

  //Smart constructors for FileIO
  def create(path: String): FileIO[File] = liftF(Create(StringPath(path)))
  def read(fileP: File): FileIO[Array[Byte]] = liftF(Read(fileP))
  def write(f: File, contents: Array[Byte]): FileIO[File] = liftF(Write(f, contents))
  //def scan(file: File, cursor: Cursor = BOF): FileIO[(Array[Byte], File, Cursor)] = liftF(ChunkR(file,cursor))
}
