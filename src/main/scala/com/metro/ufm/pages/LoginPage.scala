package com.metro.ufm.pages

import com.metro.ufm.MetroUfmSession
import com.metro.ufm.config.ApplicationSettings
import com.metro.ufm.services.ConfiguredAuthenticationService

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.form.{Button, Form, PasswordTextField, TextField}
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.model.{Model, ResourceModel}

class LoginPage extends WebPage {

  private val usernameModel = Model.of("")
  private val passwordModel = Model.of("")

  @transient
  private var authenticationService: ConfiguredAuthenticationService = _

  override def onInitialize(): Unit = {
    super.onInitialize()

    if (authenticationService == null) {
      authenticationService =
        new ConfiguredAuthenticationService(ApplicationSettings.load())
    }
  }

  /**
   * Load LoginPage-specific CSS through Wicket.
   */
  override def renderHead(response: IHeaderResponse): Unit = {
    super.renderHead(response)

    response.render(
      CssHeaderItem.forReference(
        new CssResourceReference(
          classOf[LoginPage],
          "LoginPage.css"
        )
      )
    )
  }

  private val loginForm = new Form[Void]("loginForm") {

    override protected def onSubmit(): Unit = {

      val username = usernameModel.getObject.trim
      val password = passwordModel.getObject

      if (authenticationService.authenticate(username, password)) {

        MetroUfmSession.current.signIn(username)

        setResponsePage(classOf[ApplicationShellPage])

      } else {

        error("Invalid username or password.")
      }
    }
  }

  private val usernameField =
    new TextField[String]("username", usernameModel)

  usernameField.setRequired(true)

  usernameField.setLabel(
    new ResourceModel("login.username", "User name")
  )

  private val passwordField =
    new PasswordTextField("password", passwordModel)

  passwordField.setRequired(true)

  passwordField.setResetPassword(true)

  passwordField.setLabel(
    new ResourceModel("login.password", "Password")
  )

  loginForm.add(usernameField)
  loginForm.add(passwordField)

  loginForm.add(
    new Button("loginButton")
  )

  loginForm.add(
    new Button("resetButton") {

      override def onSubmit(): Unit = {
        setResponsePage(classOf[LoginPage])
      }

    }.setDefaultFormProcessing(false)
  )

  add(
    new FeedbackPanel("feedback")
  )

  add(loginForm)
}