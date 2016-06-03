package com.github.milyardo.freeio

import java.io.{File => JFile}
import java.io.{FileOutputStream, FileInputStream}

import com.github.milyardo.freeio.file._

import scalaz._
import scalaz.concurrent.Task

object jio extends FileAlg[JFile, String, Nothing] {

  def withJavaIO: FileOp ~> Task = new (FileOp ~> Task) {
    import Task.delay

    override def apply[A](fa: FileOp[A]): Task[A] = fa match {
      case Create(path)           => delay(touch(new JFile(path)))
      case Read(fileP)            => delay(readWholeFile(fileP))
      case Write(fileP, contents) => delay(writeWholeFile(fileP, contents))
    }
  }

  def touch(fileP: JFile): JFile = {
    val absFile = fileP.getAbsoluteFile
    if (absFile.exists()) {
      absFile
    } else if (absFile.isDirectory) {
      absFile.mkdirs()
      absFile
    } else {
      absFile.getParentFile.mkdirs()
      absFile.createNewFile()
      absFile
    }
  }

  def readWholeFile(fileP: JFile): Array[Byte] = {
    val array = new Array[Byte](fileP.length().toInt)
    val fis   = new FileInputStream(fileP)
    fis.read(array)
    fis.close()
    array
  }

  def writeWholeFile(fileP: JFile, contents: Array[Byte]): JFile = {
    val existingFile = touch(fileP)
    val fos          = new FileOutputStream(existingFile)
    fos.write(contents)
    fos.close()
    existingFile
  }
}
