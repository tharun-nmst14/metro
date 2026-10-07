package com.metro.ufm

import com.metro.ufm.pages.{ApplicationShellPage, DsdOrderPage, LoginPage, LogoutPage, SteeringParameterPage, StockAssignmentPage}
import org.apache.wicket.{Page, Session}
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.{Request, Response}

class MetroUfmApplication extends WebApplication {
  override def getHomePage: Class[_ <: Page] = classOf[LoginPage]

  override def newSession(request: Request, response: Response): Session = {
    new MetroUfmSession(request)
  }

  override def init(): Unit = {
    super.init()
    getMarkupSettings.setStripWicketTags(true)
    mountPage("/login", classOf[LoginPage])
    mountPage("/shell", classOf[ApplicationShellPage])
    mountPage("/ordering/dsd-order", classOf[DsdOrderPage])
    mountPage("/ordering/stock-assignment", classOf[StockAssignmentPage])
    mountPage("/maintenance/steering-parameter", classOf[SteeringParameterPage])
    mountPage("/logout", classOf[LogoutPage])
  }
}
