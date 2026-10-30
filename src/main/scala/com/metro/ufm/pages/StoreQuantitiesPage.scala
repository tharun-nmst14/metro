package com.metro.ufm.pages

import com.metro.ufm.panels.{HeaderPanel, NavigationPanel, UserInfoPanel}
import com.metro.ufm.config.ApplicationSettings
import com.metro.ufm.models.{StoreQuantity, StoreQuantityFilter}
import com.metro.ufm.services.StoreQuantitiesService

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.AttributeModifier
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse, JavaScriptHeaderItem}
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.image.Image
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.model.{LoadableDetachableModel, Model}
import org.apache.wicket.request.resource.PackageResourceReference

import scala.jdk.CollectionConverters._
import org.apache.wicket.model.IModel

class StoreQuantitiesPage extends WebPage {

  // --------------------------------------------------
  // Service
  // --------------------------------------------------

  private val storeQuantitiesService = new StoreQuantitiesService()

  // --------------------------------------------------
  // Toolbar models
  // --------------------------------------------------

  private val puarModel = Model.of("151")
  private val sidModel = Model.of("10-MUELHEIM")
  private val orderDayModel = Model.of("14.10.2026")
  private val cutoffTimeModel = Model.of("10:30:00")

  // Draft models: changes are applied only when Apply is clicked.
  private val puarDraft = Model.of("")
  private val sidDraft = Model.of("")
  private val orderDayDraft = Model.of("")
  private val cutoffTimeDraft = Model.of("")

  // Search and Edit State

  private var searchResults: List[StoreQuantity] = List.empty

  private var savedQuantities: Map[String, Int] = Map.empty
  private var draftOrderQuantities: Map[String, String] = Map.empty
  private var hasSavedChanges = false
  private var released = false

  private val actionMessageModel = Model.of("")
  private val saveDisabledModel = Model.of[java.lang.Boolean](java.lang.Boolean.TRUE)
  private val releaseAvailableModel = Model.of[java.lang.Boolean](java.lang.Boolean.FALSE)


  // --------------------------------------------------
  // Application shell
  // --------------------------------------------------

  add(new HeaderPanel("headerPanel", "Store Quantities"))

  add(new NavigationPanel("navigationPanel"))

  add(
    new UserInfoPanel(
      "userInfoPanel",
      settings = ApplicationSettings.load()
    )
  )

  // --------------------------------------------------
  // Toolbar values
  // --------------------------------------------------

  private val puarValue = new Label("puarValue", puarModel)
  puarValue.setOutputMarkupId(true)
  add(puarValue)

  private val sidValue = new Label("sidValue", sidModel)
  sidValue.setOutputMarkupId(true)
  add(sidValue)

  private val orderDayValue = new Label("orderDayValue", orderDayModel)
  orderDayValue.setOutputMarkupId(true)
  add(orderDayValue)

  private val cutoffTimeValue =
    new Label("cutoffTimeValue", cutoffTimeModel)

  cutoffTimeValue.setOutputMarkupId(true)
  add(cutoffTimeValue)

  // --------------------------------------------------
  // Results section
  // --------------------------------------------------

  // This component corresponds to wicket:id="resultsSection"
  // in StoreQuantitiesPage.html.

  private val resultsSection =
    new WebMarkupContainer("resultsSection")

  resultsSection.setOutputMarkupId(true)
  resultsSection.setOutputMarkupPlaceholderTag(true)

  add(resultsSection)

  // Initially, no results have been loaded.

  // private var searchResults: List[StoreQuantity] = List.empty

  // --------------------------------------------------
  // Empty-state container
  // --------------------------------------------------

  private val emptyStateContainer =
    new WebMarkupContainer("emptyStateContainer")

  emptyStateContainer.setOutputMarkupId(true)
  emptyStateContainer.setOutputMarkupPlaceholderTag(true)

  emptyStateContainer.add(
    new Image(
      "emptyStateImage",
      new PackageResourceReference(
        classOf[StoreQuantitiesPage],
        "null.png"
      )
    )
  )

  // Initially visible because searchResults is empty.
  emptyStateContainer.setVisible(true)

  resultsSection.add(emptyStateContainer)

  private val actionMessage = new Label("actionMessage", actionMessageModel)
  actionMessage.setOutputMarkupId(true)
  resultsSection.add(actionMessage)

// Results form

private val resultsForm =
  new Form[Void]("resultsForm")

resultsForm.setOutputMarkupId(true)

resultsSection.add(resultsForm)

  // Sidebar actions share the results form with the editable table.
  private val actionSidebar = new WebMarkupContainer("actionSidebar")
  actionSidebar.setOutputMarkupId(true)
  actionSidebar.setOutputMarkupPlaceholderTag(true)
  actionSidebar.setVisible(false)

  private val saveAction = new AjaxButton("saveAction", resultsForm) {
    override def onSubmit(target: AjaxRequestTarget): Unit =
      saveDraftQuantities(target)

    override def onError(target: AjaxRequestTarget): Unit = {
      showActionMessage("Unable to save. Check that every quantity is a non-negative whole number.")
      refreshActionAvailability()
      target.add(actionMessage, actionSidebar)
    }
  }
  saveAction.add(AttributeModifier.replace(
    "disabled",
    saveDisabledModel.map(disabled => if (disabled) "disabled" else null)
  ))
  actionSidebar.add(saveAction)

  private val releaseAction = new AjaxButton("releaseAction", resultsForm) {
    override def onSubmit(target: AjaxRequestTarget): Unit =
      releaseSavedQuantities(target)

    override def onError(target: AjaxRequestTarget): Unit = {
      showActionMessage("Unable to release. Check the quantity values and save changes first.")
      refreshActionAvailability()
      target.add(actionMessage, actionSidebar)
    }
  }
  releaseAction.add(AttributeModifier.replace(
    "disabled",
    releaseAvailableModel.map(enabled => if (enabled) null else "disabled")
  ))
  releaseAction.add(AttributeModifier.replace(
    "data-release-available",
    releaseAvailableModel.map(enabled => if (enabled) "true" else "false")
  ))
  actionSidebar.add(releaseAction)

  resultsForm.add(actionSidebar)

  // --------------------------------------------------
  // Results table container
  // --------------------------------------------------

  private val resultsContainer =
    new WebMarkupContainer("resultsContainer")

  resultsContainer.setOutputMarkupId(true)
  resultsContainer.setOutputMarkupPlaceholderTag(true)
  resultsForm.add(resultsContainer)

  // Hide the table on the initial page load.
  resultsContainer.setVisible(false)

  // --------------------------------------------------
  // Editable Table rows
  // --------------------------------------------------

  private val quantityRows =
    new ListView[StoreQuantity](
      "quantityRows",
      new LoadableDetachableModel[java.util.List[StoreQuantity]] {

        override def load(): java.util.List[StoreQuantity] = {
          searchResults.asJava
        }
      }
    ) {

      override def populateItem(item: ListItem[StoreQuantity]): Unit = {

        val quantity = item.getModelObject

        item.add(new Label("articleNo", quantity.articleNo))

        item.add(new Label("description", quantity.description))

        item.add(new Label("subsystemNo", quantity.subsystemNo))

        item.add(new Label("merchandiseGroup", quantity.merchandiseGroup))

        item.add(
          new Label(
            "stockQuantity",
            quantity.stockQuantity.toString
          )
        )

        item.add(
          new Label(
            "salesQuantity",
            quantity.salesQuantity.toString
          )
        )

        val orderQuantityModel = new IModel[String] {
          override def getObject: String =
            draftOrderQuantities.getOrElse(quantity.articleNo, quantity.orderQuantity.toString)

          override def setObject(value: String): Unit = {
            draftOrderQuantities = draftOrderQuantities.updated(
              quantity.articleNo,
              Option(value).getOrElse("")
            )
          }
        }

        val orderQuantityInput = new TextField[String]("orderQuantity", orderQuantityModel) {
          override protected def getInputTypes(): Array[String] = Array("number")
        }
        orderQuantityInput.add(AttributeModifier.replace(
          "data-saved-quantity",
          savedQuantities.getOrElse(quantity.articleNo, quantity.orderQuantity).toString
        ))

        item.add(orderQuantityInput)
        
      }
    }

  quantityRows.setReuseItems(true)
  resultsContainer.add(quantityRows)

  private def hasUnsavedChanges: Boolean =
    searchResults.exists { row =>
      val current = draftOrderQuantities.getOrElse(row.articleNo, row.orderQuantity.toString).trim
      val saved = savedQuantities.getOrElse(row.articleNo, row.orderQuantity)
      current.toIntOption match {
        case Some(value) => value != saved
        case None => current != saved.toString
      }
    }

  private def parsedDraftQuantities: Option[Map[String, Int]] = {
    val parsed = searchResults.map { row =>
      row.articleNo -> draftOrderQuantities
        .getOrElse(row.articleNo, row.orderQuantity.toString)
        .trim
        .toIntOption
    }

    if (parsed.forall { case (_, value) => value.exists(_ >= 0) }) {
      Some(parsed.map { case (articleNo, value) => articleNo -> value.get }.toMap)
    } else {
      None
    }
  }

  private def refreshActionAvailability(): Unit = {
    val hasResults = searchResults.nonEmpty
    val hasChanges = hasUnsavedChanges
    actionSidebar.setVisible(hasResults)
    saveDisabledModel.setObject(!hasResults || !hasChanges)
    releaseAvailableModel.setObject(hasResults && hasSavedChanges && !hasChanges && !released)
  }

  private def showActionMessage(message: String): Unit = {
    actionMessageModel.setObject(message)
  }

  private def saveDraftQuantities(target: AjaxRequestTarget): Unit = {
    if (!hasUnsavedChanges) {
      showActionMessage("There are no changed quantities to save.")
    } else {
      parsedDraftQuantities match {
        case None =>
          showActionMessage("Order Quantity must be a non-negative whole number for every article.")
        case Some(quantities) =>
          searchResults = searchResults.map { row =>
            row.copy(orderQuantity = quantities(row.articleNo))
          }
          savedQuantities = quantities
          draftOrderQuantities = quantities.map { case (articleNo, value) =>
            articleNo -> value.toString
          }
          hasSavedChanges = true
          released = false
          quantityRows.removeAll()
          showActionMessage("Changes saved")
      }
    }

    refreshActionAvailability()
    target.add(actionMessage, actionSidebar, resultsContainer)
  }

  private def releaseSavedQuantities(target: AjaxRequestTarget): Unit = {
    if (searchResults.isEmpty) {
      showActionMessage("Load Store Quantities results before releasing.")
    } else if (hasUnsavedChanges) {
      showActionMessage("Save the changed quantities before releasing.")
    } else if (!hasSavedChanges || released) {
      showActionMessage("Save changed quantities before releasing.")
    } else {
      released = true
      showActionMessage("Demo release completed.")
    }

    refreshActionAvailability()
    target.add(actionMessage, actionSidebar)
  }

  // --------------------------------------------------
  // Filter modal
  // --------------------------------------------------

  private val filterModal =
    new WebMarkupContainer("filterModal")

  filterModal.setOutputMarkupId(true)
  filterModal.setOutputMarkupPlaceholderTag(true)
  filterModal.setVisible(false)

  add(filterModal)

  // --------------------------------------------------
  // Filter form
  // --------------------------------------------------

  private val filterForm =
    new Form[Void]("filterForm")

  filterModal.add(filterForm)

  private val puarInput =
    new TextField[String]("puarInput", puarDraft)

  private val sidInput =
    new TextField[String]("sidInput", sidDraft)

  private val orderDayInput =
    new TextField[String]("orderDayInput", orderDayDraft)

  private val cutoffTimeInput =
    new TextField[String]("cutoffTimeInput", cutoffTimeDraft)

  filterForm.add(puarInput)
  filterForm.add(sidInput)
  filterForm.add(orderDayInput)
  filterForm.add(cutoffTimeInput)

  // --------------------------------------------------
  // Open filter popup
  // --------------------------------------------------

  private def openFilterModal(target: AjaxRequestTarget): Unit = {

    // Populate draft fields with the currently applied values.
    puarDraft.setObject(puarModel.getObject)
    sidDraft.setObject(sidModel.getObject)
    orderDayDraft.setObject(orderDayModel.getObject)
    cutoffTimeDraft.setObject(cutoffTimeModel.getObject)

    filterModal.setVisible(true)

    target.add(filterModal)
  }

  // --------------------------------------------------
  // Toolbar filter links
  // --------------------------------------------------

  private def createToolbarLink(id: String): AjaxLink[Void] = {

    new AjaxLink[Void](id) {

      override def onClick(target: AjaxRequestTarget): Unit = {
        openFilterModal(target)
      }
    }
  }

  add(createToolbarLink("puarFilter"))
  add(createToolbarLink("sidFilter"))
  add(createToolbarLink("orderDayFilter"))
  add(createToolbarLink("cutoffTimeFilter"))

  // --------------------------------------------------
  // Close filter popup
  // --------------------------------------------------

  filterModal.add(
    new AjaxLink[Void]("closeFilter") {

      override def onClick(target: AjaxRequestTarget): Unit = {

        filterModal.setVisible(false)

        target.add(filterModal)
      }
    }
  )

  // --------------------------------------------------
  // Apply filters
  // --------------------------------------------------

  filterForm.add(
    new AjaxButton("applyFiltersButton", filterForm) {

      override def onSubmit(target: AjaxRequestTarget): Unit = {

        // Build the filter from the draft field values.
        val filter = StoreQuantityFilter(
          puar = puarDraft.getObject,
          sid = sidDraft.getObject,
          orderDay = orderDayDraft.getObject,
          cutoffTime = cutoffTimeDraft.getObject
        )

        // Retrieve results from the service.
        searchResults = storeQuantitiesService.search(filter)

        // Update the toolbar values.
        puarModel.setObject(filter.puar)
        sidModel.setObject(filter.sid)
        orderDayModel.setObject(filter.orderDay)
        cutoffTimeModel.setObject(filter.cutoffTime)

        // Determine which section should be displayed.
        val hasResults = searchResults.nonEmpty

        emptyStateContainer.setVisible(!hasResults)
        resultsContainer.setVisible(hasResults)

        savedQuantities = searchResults.map { row =>
          row.articleNo -> row.orderQuantity
        }.toMap
        draftOrderQuantities = searchResults.map { row =>
          row.articleNo -> row.orderQuantity.toString
        }.toMap
        hasSavedChanges = false
        released = false
        quantityRows.removeAll()
        showActionMessage("")
        refreshActionAvailability()

        // Close the filter popup.
        filterModal.setVisible(false)

        // Refresh the popup and toolbar.
        target.add(filterModal)

        target.add(puarValue)
        target.add(sidValue)
        target.add(orderDayValue)
        target.add(cutoffTimeValue)

        // Refresh the empty state and results table.
        target.add(emptyStateContainer)
        target.add(resultsContainer)
        target.add(actionSidebar, actionMessage)
      }
    }
  )

  // --------------------------------------------------
  // Cancel filters
  // --------------------------------------------------

  filterForm.add(
    new AjaxButton("cancelFiltersButton", filterForm) {

      override def onSubmit(target: AjaxRequestTarget): Unit = {

        // Close without applying the draft values.
        filterModal.setVisible(false)

        target.add(filterModal)
      }
    }
  )

  // --------------------------------------------------
  // CSS resources
  // --------------------------------------------------

  override def renderHead(response: IHeaderResponse): Unit = {

    super.renderHead(response)

    response.render(
      CssHeaderItem.forReference(
        new PackageResourceReference(
          classOf[ApplicationShellPage],
          "ApplicationShellPage.css"
        )
      )
    )

    response.render(
      CssHeaderItem.forReference(
        new PackageResourceReference(
          classOf[StoreQuantitiesPage],
          "StoreQuantitiesPage.css"
        )
      )
    )

    response.render(
      JavaScriptHeaderItem.forReference(
        new PackageResourceReference(
          classOf[StoreQuantitiesPage],
          "StoreQuantitiesPage.js"
        )
      )
    )
  }
}