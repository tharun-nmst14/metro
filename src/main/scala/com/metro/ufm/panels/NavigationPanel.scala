package com.metro.ufm.panels

import com.metro.ufm.pages.{DsdOrderPage, SteeringParameterPage, StockAssignmentPage}
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.markup.html.panel.Panel

class NavigationPanel(id: String) extends Panel(id) {
  add(new BookmarkablePageLink("maintenanceLink", classOf[SteeringParameterPage]))
  add(new BookmarkablePageLink("dsdOrderLink", classOf[DsdOrderPage]))
  add(new BookmarkablePageLink("stockAssignmentLink", classOf[StockAssignmentPage]))
}
