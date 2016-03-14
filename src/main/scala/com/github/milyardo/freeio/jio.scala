package com.github.milyardo.freeio

import java.io.{FileOutputStream, FileInputStream}

import com.github.milyardo.freeio.file._

import scalaz._
import scalaz.concurrent.Task

object jio {
  import java.io.{File => JFile}



  private[jio] case class JavaFile(jFile: JFile) extends File

  def withJavaIO: FileOp ~> Task = new (FileOp ~> Task) {
    import Task.delay

    override def apply[A](fa: FileOp[A]): Task[A] = fa match {
      case Create(StringPath(path)) => delay(JavaFile(touch(new JFile(path))))
      case Read(JavaFile(fileP)) => delay(readWholeFile(fileP))
      case Write(JavaFile(fileP), contents) => delay(JavaFile(writeWholeFile(fileP,contents)))
      case op => sys.error(s"$op is not supported by java.io.")
    }
  }

  def touch(fileP: JFile): JFile = {
    val absFile = fileP.getAbsoluteFile
    if(absFile.exists()) {
      absFile
    } else if(absFile.isDirectory) {
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
    val fis = new FileInputStream(fileP)
    fis.read(array)
    fis.close()
    array
  }

  def writeWholeFile(fileP: JFile, contents: Array[Byte]): JFile = {
    val existingFile = touch(fileP)
    val fos = new FileOutputStream(existingFile)
    fos.write(contents)
    fos.close()
    existingFile
  }
}

