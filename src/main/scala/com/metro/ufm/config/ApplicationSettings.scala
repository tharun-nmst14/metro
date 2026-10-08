package com.metro.ufm.config

final case class ApplicationSettings(
  port: Int,
  loginUsername: Option[String],
  loginPassword: Option[String],
  applicationVersion: String
) extends Serializable

object ApplicationSettings {
  private val DefaultPort = 8080
  private val DefaultLoginUsername = Some("admin")
  private val DefaultLoginPassword = Some("admin123")
  private val DefaultApplicationVersion = "0.1.0-SNAPSHOT"

  def load(): ApplicationSettings = {
    val configuredPort = sys.props.get("ufm.port").orElse(sys.env.get("UFM_PORT"))
    ApplicationSettings(
      port = configuredPort.flatMap(toPort).getOrElse(DefaultPort),
      loginUsername = setting("ufm.login.username", "UFM_LOGIN_USERNAME").orElse(DefaultLoginUsername),
      loginPassword = setting("ufm.login.password", "UFM_LOGIN_PASSWORD").orElse(DefaultLoginPassword),
      applicationVersion = setting("ufm.application.version", "UFM_APPLICATION_VERSION").getOrElse(DefaultApplicationVersion)
    )
  }

  private def setting(propertyName: String, environmentName: String): Option[String] = {
    sys.props.get(propertyName).orElse(sys.env.get(environmentName)).filter(_.nonEmpty)
  }

  private def toPort(value: String): Option[Int] = {
    value.toIntOption.filter(port => port > 0 && port <= 65535)
  }
}
