package com.metro.ufm.pages

import com.metro.ufm.config.ApplicationSettings
import com.metro.ufm.panels.{HeaderPanel, NavigationPanel, UserInfoPanel}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.{CheckBox, Radio, RadioGroup}
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.{CssResourceReference, PackageResourceReference}

final class SteeringParameterPage extends WebPage {
  private val settings = ApplicationSettings.load()

  private val saturdayOrderDayModel = Model.of(java.lang.Boolean.TRUE)
  private val sundayOrderDayModel = Model.of(java.lang.Boolean.FALSE)
  private val ediForDsdModel = Model.of(java.lang.Boolean.FALSE)
  private val dispoDayHoliday99Model = Model.of(java.lang.Boolean.FALSE)

  private val saturdayDeliveryDayModel = Model.of(java.lang.Boolean.TRUE)
  private val sundayDeliveryDayModel = Model.of(java.lang.Boolean.FALSE)
  private val noDeliveryDayHoliday99Model = Model.of(java.lang.Boolean.FALSE)
  private val displaySalesForecastModel = Model.of(java.lang.Boolean.FALSE)

  private val considerSaturdayOplShiftingModel = Model.of(java.lang.Boolean.FALSE)
  private val noDeliveryDayHolidayModel = Model.of(java.lang.Boolean.TRUE)
  private val orderByComsModel = Model.of("3")
  private val articleLevelModel = Model.of(java.lang.Boolean.TRUE)
  private val dcLevelModel = Model.of(java.lang.Boolean.TRUE)

  private var savedValues = currentValues()

  private val statusModel = Model.of("")
  private val status = new Label("status", statusModel)
  status.setOutputMarkupPlaceholderTag(true)
  status.setVisible(false)

  add(new HeaderPanel("headerPanel", "Steering Parameter"))
  add(new NavigationPanel("navigationPanel"))
  add(new UserInfoPanel("userInfoPanel", settings))
  add(new Label("activeTab", "Steering Parameter"))
  add(new WebMarkupContainer("tabPlaceholders"))

  private val parameterPanel = new WebMarkupContainer("parameterPanel")
  parameterPanel.setOutputMarkupId(true)
  add(parameterPanel)

  parameterPanel.add(new CheckBox("saturdayOrderDay", saturdayOrderDayModel))
  parameterPanel.add(new CheckBox("sundayOrderDay", sundayOrderDayModel))
  parameterPanel.add(new CheckBox("ediForDsd", ediForDsdModel))
  parameterPanel.add(new CheckBox("dispoDayHoliday99", dispoDayHoliday99Model))
  parameterPanel.add(new CheckBox("saturdayDeliveryDay", saturdayDeliveryDayModel))
  parameterPanel.add(new CheckBox("sundayDeliveryDay", sundayDeliveryDayModel))
  parameterPanel.add(new CheckBox("noDeliveryDayHoliday99", noDeliveryDayHoliday99Model))
  parameterPanel.add(new CheckBox("displaySalesForecast", displaySalesForecastModel))
  parameterPanel.add(new CheckBox("considerSaturdayOplShifting", considerSaturdayOplShiftingModel))
  parameterPanel.add(new CheckBox("noDeliveryDayHoliday", noDeliveryDayHolidayModel))
  parameterPanel.add(new CheckBox("articleLevel", articleLevelModel))
  parameterPanel.add(new CheckBox("dcLevel", dcLevelModel))

  private val orderByComsGroup = new RadioGroup[String]("orderByComs", orderByComsModel)
  orderByComsGroup.add(new Radio[String]("orderByComs3", Model.of("3")))
  orderByComsGroup.add(new Radio[String]("orderByComs5", Model.of("5")))
  orderByComsGroup.add(new Radio[String]("orderByComs7", Model.of("7")))
  parameterPanel.add(orderByComsGroup)

  private val toolbar = new WebMarkupContainer("toolbar")
  toolbar.add(new AjaxLink[Void]("saveLink") {
    override def onClick(target: AjaxRequestTarget): Unit = {
      savedValues = currentValues()
      statusModel.setObject("Steering Parameter values saved successfully.")
      status.setVisible(true)
      target.add(status)
    }
  })
  toolbar.add(new AjaxLink[Void]("refreshLink") {
    override def onClick(target: AjaxRequestTarget): Unit = {
      restoreValues(savedValues)
      statusModel.setObject("Steering Parameter values refreshed.")
      status.setVisible(true)
      target.add(parameterPanel, status)
    }
  })
  add(toolbar)
  add(status)

  private def currentValues(): SteeringParameterValues = SteeringParameterValues(
    saturdayOrderDayModel.getObject,
    sundayOrderDayModel.getObject,
    ediForDsdModel.getObject,
    dispoDayHoliday99Model.getObject,
    saturdayDeliveryDayModel.getObject,
    sundayDeliveryDayModel.getObject,
    noDeliveryDayHoliday99Model.getObject,
    displaySalesForecastModel.getObject,
    considerSaturdayOplShiftingModel.getObject,
    noDeliveryDayHolidayModel.getObject,
    orderByComsModel.getObject,
    articleLevelModel.getObject,
    dcLevelModel.getObject
  )

  private def restoreValues(values: SteeringParameterValues): Unit = {
    saturdayOrderDayModel.setObject(values.saturdayOrderDay)
    sundayOrderDayModel.setObject(values.sundayOrderDay)
    ediForDsdModel.setObject(values.ediForDsd)
    dispoDayHoliday99Model.setObject(values.dispoDayHoliday99)
    saturdayDeliveryDayModel.setObject(values.saturdayDeliveryDay)
    sundayDeliveryDayModel.setObject(values.sundayDeliveryDay)
    noDeliveryDayHoliday99Model.setObject(values.noDeliveryDayHoliday99)
    displaySalesForecastModel.setObject(values.displaySalesForecast)
    considerSaturdayOplShiftingModel.setObject(values.considerSaturdayOplShifting)
    noDeliveryDayHolidayModel.setObject(values.noDeliveryDayHoliday)
    orderByComsModel.setObject(values.orderByComs)
    articleLevelModel.setObject(values.articleLevel)
    dcLevelModel.setObject(values.dcLevel)
  }

  override def renderHead(response: IHeaderResponse): Unit = {
    super.renderHead(response)
    response.render(
      CssHeaderItem.forReference(
        new PackageResourceReference(classOf[ApplicationShellPage], "ApplicationShellPage.css")
      )
    )
    response.render(
      CssHeaderItem.forReference(
        new CssResourceReference(classOf[SteeringParameterPage], "SteeringParameterPage.css")
      )
    )
  }
}

private final case class SteeringParameterValues(
  saturdayOrderDay: java.lang.Boolean,
  sundayOrderDay: java.lang.Boolean,
  ediForDsd: java.lang.Boolean,
  dispoDayHoliday99: java.lang.Boolean,
  saturdayDeliveryDay: java.lang.Boolean,
  sundayDeliveryDay: java.lang.Boolean,
  noDeliveryDayHoliday99: java.lang.Boolean,
  displaySalesForecast: java.lang.Boolean,
  considerSaturdayOplShifting: java.lang.Boolean,
  noDeliveryDayHoliday: java.lang.Boolean,
  orderByComs: String,
  articleLevel: java.lang.Boolean,
  dcLevel: java.lang.Boolean
) extends Serializable
