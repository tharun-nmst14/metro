package com.metro.ufm

import org.apache.wicket.Session
import org.apache.wicket.protocol.http.WebSession
import org.apache.wicket.request.Request

class MetroUfmSession(request: Request) extends WebSession(request) {
  private var signedInUsername: Option[String] = None

  def signIn(username: String): Unit = {
    signedInUsername = Some(username)
  }

  def signOut(): Unit = {
    signedInUsername = None
    invalidateNow()
  }

  def isSignedIn: Boolean = signedInUsername.nonEmpty

  def username: Option[String] = signedInUsername
}

object MetroUfmSession {
  def current: MetroUfmSession = Session.get().asInstanceOf[MetroUfmSession]
}
