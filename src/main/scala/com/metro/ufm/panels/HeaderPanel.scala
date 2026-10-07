package com.metro.ufm.panels

import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel

class HeaderPanel(id: String, title: String) extends Panel(id) {
  add(new Label("pageTitle", title))
}
