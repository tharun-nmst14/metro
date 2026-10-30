package com.metro.ufm.database

import scala.util.Using

object OracleConnectionTest {
  def main(args: Array[String]): Unit = {
    val connectionProvider = OracleConnectionProvider.fromEnvironment()

    Using.resource(connectionProvider.openConnection()) { connection =>
      Using.resource(connection.prepareStatement("SELECT SYSDATE FROM DUAL")) { statement =>
        Using.resource(statement.executeQuery()) { resultSet =>
          if (!resultSet.next()) {
            throw new IllegalStateException("Oracle SYSDATE query returned no rows.")
          }

          println(s"Oracle connection and SYSDATE query succeeded: ${resultSet.getTimestamp(1)}")
        }
      }
    }
  }
}
