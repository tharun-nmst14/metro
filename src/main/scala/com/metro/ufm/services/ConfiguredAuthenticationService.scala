package com.metro.ufm.services

import com.metro.ufm.config.ApplicationSettings

final class ConfiguredAuthenticationService(settings: ApplicationSettings) extends AuthenticationService {
  override def authenticate(username: String, password: String): Boolean = {
    settings.loginUsername.contains(username) && settings.loginPassword.contains(password)
  }
}
