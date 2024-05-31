package services
import java.io.{FileWriter, BufferedWriter}

object TransactionLogging {
  def appendToFile(fileName: String, text: String): Unit = {
    val fw = new FileWriter(fileName, true) // true to append
    val bw = new BufferedWriter(fw)
    try {
      bw.write(text)
      bw.newLine()
    } finally {
      bw.close()
      fw.close()
    }
  }
}
