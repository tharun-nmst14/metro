package com.metro.ufm.pages

import com.metro.ufm.config.ApplicationSettings
import com.metro.ufm.models.{StockAssignmentDcDetail, StockAssignmentResult}
import com.metro.ufm.panels.{HeaderPanel, NavigationPanel, UserInfoPanel}
import com.metro.ufm.repositories.MockStockAssignmentRepository
import com.metro.ufm.services.StockAssignmentService
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.ajax.AjaxEventBehavior
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.AttributeModifier
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.{CheckBox, DropDownChoice, TextField}
import org.apache.wicket.markup.html.image.Image
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.{WebMarkupContainer, WebPage}
import org.apache.wicket.model.Model
import org.apache.wicket.model.util.ListModel
import org.apache.wicket.request.resource.PackageResourceReference

import scala.jdk.CollectionConverters._
import scala.collection.mutable.ArrayBuffer
import java.time.LocalDate

class StockAssignmentPage extends WebPage {
  private val settings = ApplicationSettings.load()
  @transient private var stockAssignmentService: StockAssignmentService = new StockAssignmentService(new MockStockAssignmentRepository())

  private val assortmentModel = Model.of("FL Duroc/Iberico GG CS")
  private val puarModel = Model.of("163")
  private val dcModel = Model.of("")
  private val assignmentDayModel = Model.of(LocalDate.now().toString)
  private val deliveryDaySidModel = Model.of("21.09.2026")
  private val cutoffTimeModel = Model.of("1")
  private val articlesWithQuantityModel = Model.of(java.lang.Boolean.FALSE)
  private val storesWithQuantityModel = Model.of(java.lang.Boolean.FALSE)
  private val autoExportAllowedModel = Model.of(java.lang.Boolean.FALSE)
  private val pushThroughModel = Model.of(java.lang.Boolean.FALSE)
  private val quantityGapModel = Model.of("")
  private val exportedDcsModel = Model.of("")
  private val unexportedDcsModel = Model.of("")

  private val draftAssortmentModel = Model.of(assortmentModel.getObject)
  private val draftDcModel = Model.of(dcModel.getObject)
  private val draftAssignmentDayModel = Model.of(assignmentDayModel.getObject)
  private val draftDeliveryDaySidModel = Model.of(deliveryDaySidModel.getObject)
  private val draftCutoffTimeModel = Model.of(cutoffTimeModel.getObject)
  private val draftArticlesWithQuantityModel = Model.of(articlesWithQuantityModel.getObject)
  private val draftStoresWithQuantityModel = Model.of(storesWithQuantityModel.getObject)
  private val draftQuantityGapModel = Model.of(quantityGapModel.getObject)
  private val draftSgNumberModel = Model.of("")
  private val draftArticleNumberModel = Model.of("")
  private val draftSubsystemNumberModel = Model.of("")
  private val draftMerchGroupModel = Model.of("")
  private val draftArticleDescriptionModel = Model.of("")
  private val draftRemainingStockModel = Model.of("")
  private val sgNumberModel = Model.of("")
  private val articleNumberModel = Model.of("")
  private val subsystemNumberModel = Model.of("")
  private val merchGroupModel = Model.of("")
  private val articleDescriptionModel = Model.of("")
  private val remainingStockModel = Model.of("")

  private val resultRowsModel = new ListModel[StockAssignmentResult](new java.util.ArrayList[StockAssignmentResult]())
  private val dcDetailRowsModel = new ListModel[StockAssignmentDcDetail](new java.util.ArrayList[StockAssignmentDcDetail]())
  private val allResultRows = ArrayBuffer.empty[StockAssignmentResult]
  private val currentPageModel = Model.of(Integer.valueOf(0))
  private val pageNumbersModel = new ListModel[Int](new java.util.ArrayList[Int]())
  private val resultCountModel = Model.of(Integer.valueOf(0))
  private val pageSize = 1
  private val editedAssignedStockQuantities = scala.collection.mutable.Map.empty[Int, String]
  private val saveEnabledModel = Model.of[java.lang.Boolean](java.lang.Boolean.FALSE)
  private val exportEnabledModel = Model.of[java.lang.Boolean](java.lang.Boolean.FALSE)
  private var savedExportData: Option[StockAssignmentExportData] = None
  private val saveMessageModel = Model.of("")

  private def copyCurrentCriteriaToDraft(): Unit = {
    draftAssortmentModel.setObject(assortmentModel.getObject)
    draftDcModel.setObject(dcModel.getObject)
    draftAssignmentDayModel.setObject(assignmentDayModel.getObject)
    draftDeliveryDaySidModel.setObject(deliveryDaySidModel.getObject)
    draftCutoffTimeModel.setObject(cutoffTimeModel.getObject)
    draftArticlesWithQuantityModel.setObject(articlesWithQuantityModel.getObject)
    draftStoresWithQuantityModel.setObject(storesWithQuantityModel.getObject)
    draftQuantityGapModel.setObject(quantityGapModel.getObject)
    draftSgNumberModel.setObject(sgNumberModel.getObject)
    draftArticleNumberModel.setObject(articleNumberModel.getObject)
    draftSubsystemNumberModel.setObject(subsystemNumberModel.getObject)
    draftMerchGroupModel.setObject(merchGroupModel.getObject)
    draftArticleDescriptionModel.setObject(articleDescriptionModel.getObject)
    draftRemainingStockModel.setObject(remainingStockModel.getObject)
  }

  private def updateEditedAssignedStockQuantity(rowIndex: Int, originalValue: String, value: String): Unit = {
    if (value.trim == originalValue.trim) editedAssignedStockQuantities.remove(rowIndex)
    else editedAssignedStockQuantities.update(rowIndex, value)
    saveEnabledModel.setObject(editedAssignedStockQuantities.nonEmpty)
    exportEnabledModel.setObject(editedAssignedStockQuantities.isEmpty && savedExportData.nonEmpty)
  }

  private def saveChanges(target: AjaxRequestTarget): Unit = {
    val invalidValue = editedAssignedStockQuantities.values.find { value =>
      value.trim.isEmpty || !isDecimal(value)
    }

    invalidValue match {
      case Some(_) =>
        saveMessageModel.setObject("Ass. Stock Qty must be a numeric value.")
        saveMessage.setVisible(true)
      case None =>
        editedAssignedStockQuantities.foreach { case (rowIndex, value) =>
          val row = allResultRows(rowIndex)
          allResultRows.update(rowIndex, row.copy(assignedStockQuantity = value))
        }
        editedAssignedStockQuantities.clear()
        saveEnabledModel.setObject(false)
        savedExportData = Some(
          StockAssignmentExportData(
            assortment = assortmentModel.getObject,
            dc = dcModel.getObject,
            assignmentDay = assignmentDayModel.getObject,
            deliveryDaySid = deliveryDaySidModel.getObject,
            rows = allResultRows.toSeq
          )
        )
        exportEnabledModel.setObject(true)
        saveMessageModel.setObject("Stock assignment details saved successfully.")
        saveMessage.setVisible(true)
        showPage(currentPageModel.getObject)
        target.add(resultRowsContainer, pagination)
    }

    target.add(saveIcon, exportIcon, saveMessage)
  }

  private def isDecimal(value: String): Boolean =
    try {
      new java.math.BigDecimal(value.trim)
      true
    } catch {
      case _: NumberFormatException => false
    }

  private def applyDraftCriteria(): Unit = {
    assortmentModel.setObject(draftAssortmentModel.getObject)
    dcModel.setObject(draftDcModel.getObject)
    assignmentDayModel.setObject(draftAssignmentDayModel.getObject)
    deliveryDaySidModel.setObject(draftDeliveryDaySidModel.getObject)
    cutoffTimeModel.setObject(draftCutoffTimeModel.getObject)
    articlesWithQuantityModel.setObject(draftArticlesWithQuantityModel.getObject)
    storesWithQuantityModel.setObject(draftStoresWithQuantityModel.getObject)
    quantityGapModel.setObject(draftQuantityGapModel.getObject)
    sgNumberModel.setObject(draftSgNumberModel.getObject)
    articleNumberModel.setObject(draftArticleNumberModel.getObject)
    subsystemNumberModel.setObject(draftSubsystemNumberModel.getObject)
    merchGroupModel.setObject(draftMerchGroupModel.getObject)
    articleDescriptionModel.setObject(draftArticleDescriptionModel.getObject)
    remainingStockModel.setObject(draftRemainingStockModel.getObject)
  }

  override def onInitialize(): Unit = {
    super.onInitialize()
    if (stockAssignmentService == null) {
      stockAssignmentService = new StockAssignmentService(new MockStockAssignmentRepository())
    }
  }

  add(new HeaderPanel("headerPanel", "Stock Assignment"))
  add(new NavigationPanel("navigationPanel"))
  add(new UserInfoPanel("userInfoPanel", settings))

  private val criteriaContainer = new WebMarkupContainer("criteriaContainer")
  criteriaContainer.setOutputMarkupId(true)
  criteriaContainer.add(new Label("assortment", assortmentModel))
  criteriaContainer.add(new Label("puar", puarModel))
  criteriaContainer.add(new Label("dc", dcModel))
  criteriaContainer.add(new Label("assignmentDay", assignmentDayModel))
  criteriaContainer.add(new Label("deliveryDaySid", deliveryDaySidModel))
  criteriaContainer.add(new Label("cutoffTime", cutoffTimeModel))
  criteriaContainer.add(new CheckBox("articlesWithQuantity", articlesWithQuantityModel).setEnabled(false))
  criteriaContainer.add(new CheckBox("storesWithQuantity", storesWithQuantityModel).setEnabled(false))
  criteriaContainer.add(new CheckBox("autoExportAllowed", autoExportAllowedModel).setEnabled(false))
  criteriaContainer.add(new CheckBox("pushThrough", pushThroughModel).setEnabled(false))
  criteriaContainer.add(new Label("quantityGap", quantityGapModel))
  criteriaContainer.add(new Label("exportedDcs", exportedDcsModel))
  criteriaContainer.add(new Label("unexportedDcs", unexportedDcsModel))
  add(criteriaContainer)

  private val emptyState = new WebMarkupContainer("emptyState")
  emptyState.setOutputMarkupId(true)
  emptyState.add(new Image("emptyImage", new PackageResourceReference(classOf[StockAssignmentPage], "null.png")))

  private val resultRows = new ListView[StockAssignmentResult]("resultRows", resultRowsModel) {
    override def populateItem(item: org.apache.wicket.markup.html.list.ListItem[StockAssignmentResult]): Unit = {
      val result = item.getModelObject
      val rowIndex = currentPageModel.getObject * pageSize + item.getIndex
      item.add(new Label("resultSgCnu", result.sgCnu))
      item.add(new Label("resultMerchGroup", result.merchGroup))
      item.add(new Label("resultArticleNumber", result.articleNumber))
      item.add(new Label("resultVariant", result.variant))
      item.add(new Label("resultBundle", result.bundle))
      item.add(new Label("resultSubsystemNumber", result.subsystemNumber))
      item.add(new Label("resultDescription", result.description))
      item.add(new Label("resultSortText", result.sortText))
      item.add(new Label("resultSizeText", result.sizeText))
      item.add(new Label("resultCbb", result.cbb))
      item.add(new Label("resultBc", result.bc))
      item.add(new Label("resultSalesForecast", result.salesForecast))
      item.add(new Label("resultStoreQuantity", result.storeQuantity))
      val assignedStockQuantityField = new TextField[String](
        "resultAssignedStockQuantity",
        Model.of(editedAssignedStockQuantities.getOrElse(rowIndex, result.assignedStockQuantity))
      )
      assignedStockQuantityField.setOutputMarkupId(true)
      assignedStockQuantityField.add(new AjaxEventBehavior("change") {
        override def onEvent(target: AjaxRequestTarget): Unit = {
          val submittedValue = getRequest.getRequestParameters
            .getParameterValue(assignedStockQuantityField.getInputName)
            .toString
          updateEditedAssignedStockQuantity(rowIndex, result.assignedStockQuantity, submittedValue)
          saveMessage.setVisible(false)
          target.add(saveIcon, exportIcon, saveMessage)
        }
      })
      item.add(assignedStockQuantityField)
      item.add(new Label("resultRecalc", result.recalc))
      item.add(new Label("resultKeyDistribution", result.keyDistribution))
      item.add(new Label("resultQuantityGap", result.quantityGap))
      item.add(new Label("resultRemainingQuantity", result.remainingQuantity))
      item.add(new Label("resultUfmStock", result.ufmStock))
      item.add(new Label("resultPromotion", result.promotion))
      item.add(new Label("resultIbc", result.ibc))
      item.add(new Label("resultArticleExchange", result.articleExchange))
    }
  }

  private val resultRowsContainer = new WebMarkupContainer("resultRowsContainer")
  resultRowsContainer.setOutputMarkupId(true)
  resultRowsContainer.add(resultRows)

  private val dcDetailRows = new ListView[StockAssignmentDcDetail]("dcDetailRows", dcDetailRowsModel) {
    override def populateItem(item: org.apache.wicket.markup.html.list.ListItem[StockAssignmentDcDetail]): Unit = {
      val detail = item.getModelObject
      item.add(new Label("detailDcNumber", detail.dcNumber))
      item.add(new Label("detailDc", detail.dc))
      item.add(new Label("detailBc", detail.bc))
      item.add(new Label("detailOrderFactor", detail.orderFactor))
      item.add(new Label("detailSalesForecast", detail.salesForecast))
      item.add(new Label("detailStoreQuantity", detail.storeQuantity))
      item.add(new Label("detailAssignedStockQuantity", detail.assignedStockQuantity))
      item.add(new Label("detailQuantityGap", detail.quantityGap))
      item.add(new Label("detailRemainingStock", detail.remainingStock))
      item.add(new Label("detailUfmStock", detail.ufmStock))
      item.add(new Label("detailAverageNnbp", detail.averageNnbp))
    }
  }

  private val dcDetailRowsContainer = new WebMarkupContainer("dcDetailRowsContainer")
  dcDetailRowsContainer.add(dcDetailRows)

  private val resultToolbar = new WebMarkupContainer("resultToolbar")
  private val saveIcon = new WebMarkupContainer("saveIcon")
  saveIcon.setOutputMarkupId(true)
  saveIcon.add(AttributeModifier.replace("disabled", saveEnabledModel.map(enabled => if (enabled) null else "disabled")))
  saveIcon.add(new AjaxEventBehavior("click") {
    override def onEvent(target: AjaxRequestTarget): Unit = saveChanges(target)
  })
  resultToolbar.add(saveIcon)
  resultToolbar.add(new WebMarkupContainer("refreshIcon"))
  resultToolbar.add(new WebMarkupContainer("sendIcon"))
  resultToolbar.add(new WebMarkupContainer("truckIcon"))
  resultToolbar.add(new WebMarkupContainer("databaseIcon"))
  private val exportIcon = new WebMarkupContainer("exportIcon")
  exportIcon.setOutputMarkupId(true)
  exportIcon.add(AttributeModifier.replace("disabled", exportEnabledModel.map(enabled => if (enabled) null else "disabled")))
  exportIcon.add(new AjaxEventBehavior("click") {
    override def onEvent(target: AjaxRequestTarget): Unit = {
      savedExportData.foreach(data => setResponsePage(new StockAssignmentExportConfirmationPage(data)))
    }
  })
  resultToolbar.add(exportIcon)

  private val saveMessage = new Label("saveMessage", saveMessageModel)
  saveMessage.setOutputMarkupPlaceholderTag(true)
  saveMessage.setVisible(false)

  private def pageCount: Int = math.max(1, math.ceil(allResultRows.size.toDouble / pageSize).toInt)

  private def showPage(page: Int): Unit = {
    val boundedPage = math.max(0, math.min(page, pageCount - 1))
    currentPageModel.setObject(boundedPage)
    val start = boundedPage * pageSize
    resultRowsModel.setObject(allResultRows.slice(start, start + pageSize).asJava)
  }

  private def refreshPagination(): Unit = {
    pageNumbersModel.setObject((0 until pageCount).toList.asJava)
    resultCountModel.setObject(allResultRows.size)
  }

  private val pagination = new WebMarkupContainer("pagination")
  pagination.setOutputMarkupId(true)
  pagination.add(new AjaxLink[Void]("firstPageLink") {
    override def onClick(target: AjaxRequestTarget): Unit = {
      showPage(0)
      target.add(resultTableContainer)
    }
  })
  pagination.add(new AjaxLink[Void]("previousPageLink") {
    override def onClick(target: AjaxRequestTarget): Unit = {
      showPage(currentPageModel.getObject - 1)
      target.add(resultTableContainer)
    }
  })
  pagination.add(new ListView[Int]("pageNumbers", pageNumbersModel) {
    override def populateItem(item: org.apache.wicket.markup.html.list.ListItem[Int]): Unit = {
      val page = item.getModelObject
      item.add(new AjaxLink[Void]("pageLink") {
        override def onClick(target: AjaxRequestTarget): Unit = {
          showPage(page)
          target.add(resultTableContainer)
        }
      }.add(new Label("pageNumber", (page + 1).toString)))
    }
  })
  pagination.add(new AjaxLink[Void]("nextPageLink") {
    override def onClick(target: AjaxRequestTarget): Unit = {
      showPage(currentPageModel.getObject + 1)
      target.add(resultTableContainer)
    }
  })
  pagination.add(new AjaxLink[Void]("lastPageLink") {
    override def onClick(target: AjaxRequestTarget): Unit = {
      showPage(pageCount - 1)
      target.add(resultTableContainer)
    }
  })
  pagination.add(new Label("resultCount", resultCountModel))
  pagination.add(new WebMarkupContainer("gridIcon"))

  private val resultTableContainer = new WebMarkupContainer("resultTableContainer")
  resultTableContainer.setOutputMarkupId(true)
  resultTableContainer.setVisible(false)
  resultTableContainer.add(resultToolbar)
  resultTableContainer.add(saveMessage)
  resultTableContainer.add(resultRowsContainer)
  resultTableContainer.add(dcDetailRowsContainer)
  resultTableContainer.add(pagination)

  private val resultArea = new WebMarkupContainer("resultArea")
  resultArea.setOutputMarkupId(true)
  resultArea.add(emptyState)
  resultArea.add(resultTableContainer)
  add(resultArea)

  private val filterModal = new WebMarkupContainer("filterModal")
  filterModal.setOutputMarkupId(true)
  filterModal.setOutputMarkupPlaceholderTag(true)
  filterModal.setVisible(false)

  private def draftUpdateBehavior(event: String): AjaxFormComponentUpdatingBehavior =
    new AjaxFormComponentUpdatingBehavior(event) {
      override def onUpdate(target: AjaxRequestTarget): Unit = ()
    }

  private val assortmentField = new DropDownChoice[String]("assortmentField", draftAssortmentModel, Seq("FL Duroc/Iberico GG CS", "178-178 Display Nussen & trockenfruchte").asJava)
  private val dcField = new DropDownChoice[String]("dcField", draftDcModel, Seq("", "85590").asJava)
  private val assignmentDayField = new TextField[String]("assignmentDayField", draftAssignmentDayModel) {
    override protected def getInputTypes(): Array[String] = Array("date")
  }
  assignmentDayField.add(AttributeModifier.replace("min", LocalDate.now().toString))
  private val deliveryDaySidField = new DropDownChoice[String]("deliveryDaySidField", draftDeliveryDaySidModel, Seq("21.09.2026", "").asJava)
  private val cutoffTimeField = new DropDownChoice[String]("cutoffTimeField", draftCutoffTimeModel, Seq("1", "2", "3").asJava)
  private val articlesWithQuantityField = new CheckBox("articlesWithQuantityField", draftArticlesWithQuantityModel)
  private val storesWithQuantityField = new CheckBox("storesWithQuantityField", draftStoresWithQuantityModel)
  private val quantityGapField = new DropDownChoice[String]("quantityGapField", draftQuantityGapModel, Seq("", "Yes", "No").asJava)
  private val sgNumberField = new TextField[String]("sgNumberField", draftSgNumberModel)
  private val articleNumberField = new TextField[String]("articleNumberField", draftArticleNumberModel)
  private val subsystemNumberField = new TextField[String]("subsystemNumberField", draftSubsystemNumberModel)
  private val merchGroupField = new TextField[String]("merchGroupField", draftMerchGroupModel)
  private val articleDescriptionField = new TextField[String]("articleDescriptionField", draftArticleDescriptionModel)
  private val remainingStockField = new DropDownChoice[String]("remainingStockField", draftRemainingStockModel, Seq("", "Yes", "No").asJava)

  assortmentField.add(draftUpdateBehavior("change"))
  dcField.add(draftUpdateBehavior("change"))
  assignmentDayField.add(draftUpdateBehavior("input"))
  deliveryDaySidField.add(draftUpdateBehavior("change"))
  cutoffTimeField.add(draftUpdateBehavior("change"))
  articlesWithQuantityField.add(draftUpdateBehavior("change"))
  storesWithQuantityField.add(draftUpdateBehavior("change"))
  quantityGapField.add(draftUpdateBehavior("change"))
  sgNumberField.add(draftUpdateBehavior("input"))
  articleNumberField.add(draftUpdateBehavior("input"))
  subsystemNumberField.add(draftUpdateBehavior("input"))
  merchGroupField.add(draftUpdateBehavior("input"))
  articleDescriptionField.add(draftUpdateBehavior("input"))
  remainingStockField.add(draftUpdateBehavior("change"))

  filterModal.add(assortmentField)
  filterModal.add(dcField)
  filterModal.add(assignmentDayField)
  filterModal.add(deliveryDaySidField)
  filterModal.add(cutoffTimeField)
  filterModal.add(articlesWithQuantityField)
  filterModal.add(storesWithQuantityField)
  filterModal.add(quantityGapField)
  filterModal.add(sgNumberField)
  filterModal.add(articleNumberField)
  filterModal.add(subsystemNumberField)
  filterModal.add(merchGroupField)
  filterModal.add(articleDescriptionField)
  filterModal.add(remainingStockField)

  private def closeFilter(target: AjaxRequestTarget): Unit = {
    filterModal.setVisible(false)
    target.add(filterModal)
  }

  add(new AjaxLink[Void]("openFilterLink") {
    override def onClick(target: AjaxRequestTarget): Unit = {
      copyCurrentCriteriaToDraft()
      filterModal.setVisible(true)
      target.add(filterModal)
    }
  })

  filterModal.add(new AjaxLink[Void]("closeFilterLink") {
    override def onClick(target: AjaxRequestTarget): Unit = closeFilter(target)
  })
  filterModal.add(new AjaxLink[Void]("cancelFilterLink") {
    override def onClick(target: AjaxRequestTarget): Unit = closeFilter(target)
  })
  filterModal.add(new AjaxLink[Void]("applyFilterLink") {
    override def onClick(target: AjaxRequestTarget): Unit = {
      applyDraftCriteria()
      allResultRows.clear()
      editedAssignedStockQuantities.clear()
      saveEnabledModel.setObject(false)
      exportEnabledModel.setObject(false)
      savedExportData = None
      saveMessage.setVisible(false)
      allResultRows ++= stockAssignmentService.search(
        draftSgNumberModel.getObject,
        draftArticleNumberModel.getObject,
        draftSubsystemNumberModel.getObject,
        draftMerchGroupModel.getObject,
        draftArticleDescriptionModel.getObject
      )
      refreshPagination()
      showPage(0)
      dcDetailRowsModel.setObject(stockAssignmentService.findDcDetails().asJava)
      filterModal.setVisible(false)
      emptyState.setVisible(false)
      resultTableContainer.setVisible(true)
      target.add(filterModal, criteriaContainer, resultArea)
    }
  })
  add(filterModal)

  override def renderHead(response: IHeaderResponse): Unit = {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(new PackageResourceReference(classOf[ApplicationShellPage], "ApplicationShellPage.css")))
    response.render(CssHeaderItem.forReference(new PackageResourceReference(classOf[StockAssignmentPage], "StockAssignmentPage.css")))
  }
}
