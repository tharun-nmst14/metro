package com.metro.ufm.config

final class OracleDatabaseSettings private (
  val jdbcUrl: Option[String],
  val username: Option[String],
  private val configuredPassword: Option[String]
) extends Serializable {

  def password: Option[String] = configuredPassword

  def isConfigured: Boolean = jdbcUrl.nonEmpty || username.nonEmpty || configuredPassword.nonEmpty

  def validationErrors: Seq[String] = {
    val errors = Seq.newBuilder[String]

    if (jdbcUrl.isEmpty) {
      errors += "Oracle JDBC URL is not configured."
    } else if (!jdbcUrl.exists(_.startsWith("jdbc:oracle:"))) {
      errors += "Oracle JDBC URL must start with jdbc:oracle:."
    }

    if (username.isEmpty) {
      errors += "Oracle JDBC username is not configured."
    }

    if (configuredPassword.isEmpty) {
      errors += "Oracle JDBC password is not configured."
    }

    errors.result()
  }

  override def toString: String =
    s"OracleDatabaseSettings(jdbcUrl=$jdbcUrl, username=$username, passwordConfigured=${configuredPassword.nonEmpty})"
}

object OracleDatabaseSettings {
  val UrlProperty = "ufm.oracle.jdbc.url"
  val UsernameProperty = "ufm.oracle.jdbc.username"
  val PasswordProperty = "ufm.oracle.jdbc.password"

  val UrlEnvironment = "UFM_ORACLE_JDBC_URL"
  val UsernameEnvironment = "UFM_ORACLE_JDBC_USERNAME"
  val PasswordEnvironment = "UFM_ORACLE_JDBC_PASSWORD"

  def load(): OracleDatabaseSettings = {
    fromValues(
      propertyOrEnvironment(UrlProperty, UrlEnvironment),
      propertyOrEnvironment(UsernameProperty, UsernameEnvironment),
      propertyOrEnvironment(PasswordProperty, PasswordEnvironment)
    )
  }

  def fromValues(
    jdbcUrl: Option[String],
    username: Option[String],
    password: Option[String]
  ): OracleDatabaseSettings = {
    new OracleDatabaseSettings(
      normalize(jdbcUrl),
      normalize(username),
      password.filter(_.nonEmpty)
    )
  }

  private def propertyOrEnvironment(propertyName: String, environmentName: String): Option[String] = {
    sys.props.get(propertyName).filter(_.nonEmpty).orElse(
      sys.env.get(environmentName).filter(_.nonEmpty)
    )
  }

  private def normalize(value: Option[String]): Option[String] = {
    value.map(_.trim).filter(_.nonEmpty)
  }
}
