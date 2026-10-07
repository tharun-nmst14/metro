package com.metro.ufm.database

object OracleConnectionSmokeTest {
  def main(args: Array[String]): Unit = {
    val result = OracleConnectionProvider.fromEnvironment().validateConnection()
    println(result.message)

    if (result.configured && !result.connected) {
      sys.exit(1)
    }
  }
}
