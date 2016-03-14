package com.github.milyardo.freeio.file

/**
  * Created by zpowers on 3/12/16.
  */
sealed trait FileOp[A]

case class Create(path: Path) extends FileOp[File]
case class Read(f: File) extends FileOp[Array[Byte]]
case class Write(f: File, contents: Array[Byte]) extends FileOp[File]
//case class ChunkR(file: File, cursor: Cursor) extends FileOp[(Array[Byte], File, Cursor)]
//case class ChunkW(contents: Array[Byte], f: File, cursor: Cursor = EOF) extends FileOp[File]

trait File //An opaque representation of a file
trait Path //An opaque representation of a Path
trait Cursor //An opaque representation of a Cursor

case object BOF extends Cursor //Beginning of File
case object EOF extends Cursor //End of File

case class StringPath(path: String) extends Path