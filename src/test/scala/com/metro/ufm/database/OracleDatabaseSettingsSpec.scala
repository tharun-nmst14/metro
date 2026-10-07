package com.metro.ufm.database

import com.metro.ufm.config.OracleDatabaseSettings
import org.scalatest.funsuite.AnyFunSuite

final class OracleDatabaseSettingsSpec extends AnyFunSuite {
  test("unconfigured settings do not attempt a connection") {
    val settings = OracleDatabaseSettings.fromValues(None, None, None)
    val result = new OracleConnectionProvider(settings).validateConnection()

    assert(!result.configured)
    assert(!result.connected)
    assert(result.message.contains("no connection was attempted"))
  }

  test("settings require an Oracle JDBC URL and all credentials") {
    val settings = OracleDatabaseSettings.fromValues(
      Some("jdbc:postgresql://localhost/test"),
      Some("ufm"),
      None
    )

    assert(settings.validationErrors.exists(_.contains("jdbc:oracle:")))
    assert(settings.validationErrors.exists(_.contains("password is not configured")))
  }

  test("valid settings are accepted without contacting Oracle") {
    val settings = OracleDatabaseSettings.fromValues(
      Some(" jdbc:oracle:thin:@//localhost:1521/XEPDB1 "),
      Some(" ufm_user "),
      Some("secret")
    )

    assert(settings.validationErrors.isEmpty)
    assert(settings.jdbcUrl.contains("jdbc:oracle:thin:@//localhost:1521/XEPDB1"))
    assert(settings.username.contains("ufm_user"))
  }

  test("password is not included in settings text") {
    val settings = OracleDatabaseSettings.fromValues(
      Some("jdbc:oracle:thin:@//localhost:1521/XEPDB1"),
      Some("ufm_user"),
      Some("secret-password")
    )

    assert(!settings.toString.contains("secret-password"))
  }

  test("connection failure text does not expose the password") {
    val settings = OracleDatabaseSettings.fromValues(
      Some("jdbc:oracle:thin:@//127.0.0.1:1/UNAVAILABLE"),
      Some("ufm_user"),
      Some("secret-password")
    )

    val result = new OracleConnectionProvider(settings).validateConnection()

    assert(!result.connected)
    assert(!result.message.contains("secret-password"))
  }
}
