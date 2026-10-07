package com.metro.ufm.pages

import com.metro.ufm.MetroUfmSession
import com.metro.ufm.config.ApplicationSettings
import com.metro.ufm.panels.{HeaderPanel, NavigationPanel, UserInfoPanel}
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.resource.PackageResourceReference

class ApplicationShellPage extends WebPage {
  private val settings = ApplicationSettings.load()

  if (!MetroUfmSession.current.isSignedIn) {
    setResponsePage(classOf[LoginPage])
  }

  add(new HeaderPanel("headerPanel", "Main Application"))
  add(new NavigationPanel("navigationPanel"))
  add(new UserInfoPanel("userInfoPanel", settings))

  override def renderHead(response: IHeaderResponse): Unit = {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(new PackageResourceReference(classOf[ApplicationShellPage], "ApplicationShellPage.css")))
  }
}
