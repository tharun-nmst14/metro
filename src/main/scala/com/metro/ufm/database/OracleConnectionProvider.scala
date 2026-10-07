package com.metro.ufm.database

import java.sql.{Connection, DriverManager, SQLException}

import com.metro.ufm.config.OracleDatabaseSettings

final class OracleConnectionException(message: String)
    extends RuntimeException(message)

final case class OracleConnectionValidationResult(
  configured: Boolean,
  connected: Boolean,
  message: String
)

final class OracleConnectionProvider(settings: OracleDatabaseSettings) {

  def openConnection(): Connection = {
    val errors = settings.validationErrors
    if (errors.nonEmpty) {
      throw new OracleConnectionException(errors.mkString(" "))
    }

    try {
      DriverManager.getConnection(
        settings.jdbcUrl.get,
        settings.username.get,
        settings.password.get
      )
    } catch {
      case _: SQLException =>
        throw new OracleConnectionException(
          "Oracle connection failed for the configured JDBC URL."
        )
    }
  }

  def validateConnection(): OracleConnectionValidationResult = {
    if (!settings.isConfigured) {
      OracleConnectionValidationResult(
        configured = false,
        connected = false,
        message = "Oracle connectivity is not configured; no connection was attempted."
      )
    } else {
      try {
        val connection = openConnection()
        try {
          OracleConnectionValidationResult(
            configured = true,
            connected = !connection.isClosed,
            message = "Oracle connection validation succeeded."
          )
        } finally {
          connection.close()
        }
      } catch {
        case exception: OracleConnectionException =>
          OracleConnectionValidationResult(
            configured = true,
            connected = false,
            message = exception.getMessage
          )
      }
    }
  }
}

object OracleConnectionProvider {
  def fromEnvironment(): OracleConnectionProvider = {
    new OracleConnectionProvider(OracleDatabaseSettings.load())
  }
}
