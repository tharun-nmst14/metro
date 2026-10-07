package com.metro.ufm.services

trait AuthenticationService {
  def authenticate(username: String, password: String): Boolean
}
