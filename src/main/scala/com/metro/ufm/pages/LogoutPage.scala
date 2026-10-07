package com.metro.ufm.pages

import com.metro.ufm.MetroUfmSession
import org.apache.wicket.markup.html.WebPage

class LogoutPage extends WebPage {
  MetroUfmSession.current.signOut()
  setResponsePage(classOf[LoginPage])
}
