package com.metro.ufm.pages

import com.metro.ufm.MetroUfmSession
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.WebPage

class FoundationPage extends WebPage {
	add(new Label("sessionStatus", MetroUfmSession.current.username.map(name => s"Signed in as $name.").getOrElse("Not signed in.")))
	add(new Link[Void]("logoutLink") {
		override def onClick(): Unit = {
			MetroUfmSession.current.signOut()
			setResponsePage(classOf[LoginPage])
		}
	}.setVisible(MetroUfmSession.current.isSignedIn))
}
