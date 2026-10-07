package com.metro.ufm.panels

import com.metro.ufm.MetroUfmSession
import com.metro.ufm.config.ApplicationSettings
import com.metro.ufm.pages.LogoutPage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.markup.html.panel.Panel

class UserInfoPanel(id: String, settings: ApplicationSettings) extends Panel(id) {
  private val now = LocalDateTime.now()

  add(new Label("username", MetroUfmSession.current.username.getOrElse("")))
  add(new Label("version", settings.applicationVersion))
  add(new Label("currentDate", now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))))
  add(new Label("currentTime", now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
  add(new BookmarkablePageLink[Void]("logoutLink", classOf[LogoutPage]))
}
