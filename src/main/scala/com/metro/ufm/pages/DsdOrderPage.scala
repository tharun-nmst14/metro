package com.metro.ufm.pages

import com.metro.ufm.config.ApplicationSettings
import com.metro.ufm.models.{DsdOrderArticle, Supplier}
import com.metro.ufm.panels.{HeaderPanel, NavigationPanel, UserInfoPanel}
import com.metro.ufm.repositories.{MockDsdOrderArticleRepository, MockSupplierRepository}
import com.metro.ufm.services.{DsdOrderArticleService, SupplierService}

import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.AjaxEventBehavior
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.{DropDownChoice, Radio, RadioGroup, TextField}
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.{WebMarkupContainer, WebPage}
import org.apache.wicket.AttributeModifier
import org.apache.wicket.model.Model
import org.apache.wicket.model.util.ListModel
import org.apache.wicket.request.resource.PackageResourceReference

import java.math.BigDecimal
import java.time.LocalDate

import scala.jdk.CollectionConverters._

class DsdOrderPage extends WebPage {

  def this(data: DsdOrderExportData) = {
    this()
    allFilteredRows ++= data.rows
    summaryPuarModel.setObject(data.puar)
    summarySlModel.setObject(data.sl)
    summarySidModel.setObject(data.sid)
    summaryStoreNameModel.setObject(data.storeName)
    supplierNumberModel.setObject(data.supplierNumber)
    supplierNameModel.setObject(data.supplierName)
    summaryDeliveryDayModel.setObject(data.deliveryDay)
    summaryOrderModel.setObject(data.order)
    savedExportData = Some(data)
    exportEnabledModel.setObject(true)
    filterSummary.setVisible(true)
    resultTableContainer.setVisible(true)
    refreshPagination()
    showPage(0)
  }

  private val settings = ApplicationSettings.load()

  @transient
  private var supplierService: SupplierService =
    new SupplierService(new MockSupplierRepository())

  private val supplierNumberModel = Model.of("")
  private val supplierNameModel = Model.of("%%")
  private val selectedSupplierModel = new Model[Supplier](null)

  private val supplierRowsModel =
    new ListModel[Supplier](
      new java.util.ArrayList[Supplier]()
    )

  @transient
  private var dsdOrderArticleService: DsdOrderArticleService =
    new DsdOrderArticleService(
      new MockDsdOrderArticleRepository()
    )

  private val orderListModel = Model.of("")
  private val orderNumberModel = Model.of("")
  private val articleNumberModel = Model.of("")
  private val subsystemNumberModel = Model.of("")
  private val descriptionModel = Model.of("")

  private val resultRowsModel =
    new ListModel[DsdOrderArticle](
      new java.util.ArrayList[DsdOrderArticle]()
    )

  private val allFilteredRows =
    scala.collection.mutable.ArrayBuffer.empty[DsdOrderArticle]

  private val editedValues =
    scala.collection.mutable.Map.empty[
      RowKey,
      (String, String)
    ]

  private val saveEnabledModel =
    Model.of[java.lang.Boolean](
      java.lang.Boolean.FALSE
    )

  private val exportEnabledModel =
    Model.of[java.lang.Boolean](
      java.lang.Boolean.FALSE
    )

  private var savedExportData: Option[DsdOrderExportData] = None

  private val saveMessageModel =
    Model.of("")

  private val currentPageModel =
    Model.of(Integer.valueOf(0))

  private val pageNumbersModel =
    new ListModel[Int](
      new java.util.ArrayList[Int]()
    )

  private val resultCountModel =
    Model.of(Integer.valueOf(0))

  private val pageSize = 20

  private val summaryPuarModel =
    Model.of("151")

  private val summarySlModel =
    Model.of("11")

  private val summarySidModel =
    Model.of("10 - MUELHEIM")

  private val summaryStoreNameModel =
    Model.of("MUELHEIM")

  private val summaryDeliveryDayModel =
    Model.of("28.09.2026")

  private val summaryOrderModel =
    Model.of("")

  override def onInitialize(): Unit = {
    super.onInitialize()

    if (supplierService == null) {
      supplierService = new SupplierService(new MockSupplierRepository())
    }

    if (dsdOrderArticleService == null) {
      dsdOrderArticleService =
        new DsdOrderArticleService(
          new MockDsdOrderArticleRepository()
        )
    }
  }

  add(
    new HeaderPanel(
      "headerPanel",
      "DSD Order"
    )
  )

  add(
    new NavigationPanel(
      "navigationPanel"
    )
  )

  add(
    new UserInfoPanel(
      "userInfoPanel",
      settings
    )
  )

  private val filterModal =
    new WebMarkupContainer("filterModal")

  private val deliveryDayPicker = new WebMarkupContainer("deliveryDayPicker")
  private val today = LocalDate.now().toString
  deliveryDayPicker.add(AttributeModifier.replace("min", today))
  deliveryDayPicker.add(AttributeModifier.replace("value", today))

  filterModal.setOutputMarkupId(true)
  filterModal.setOutputMarkupPlaceholderTag(true)
  filterModal.setVisible(false)

  private val supplierNumberField =
    new TextField[String](
      "supplierNumberField",
      supplierNumberModel
    )

  private val supplierNameField =
    new TextField[String](
      "supplierNameField",
      supplierNameModel
    )

  private val orderListField =
    new DropDownChoice[String](
      "orderListField",
      orderListModel,
      Seq(
        "",
        "DSD-151-11",
        "DSD-151-12"
      ).asJava
    )

  private val orderNumberField =
    new TextField[String](
      "orderNumberField",
      orderNumberModel
    )

  private val articleNumberField =
    new TextField[String](
      "articleNumberField",
      articleNumberModel
    )

  private val subsystemNumberField =
    new TextField[String](
      "subsystemNumberField",
      subsystemNumberModel
    )

  private val descriptionField =
    new TextField[String](
      "descriptionField",
      descriptionModel
    )

  private val resultRows =
    new ListView[DsdOrderArticle](
      "resultRows",
      resultRowsModel
    ) {

      override def populateItem(
          item: org.apache.wicket.markup.html.list.ListItem[DsdOrderArticle]
      ): Unit = {

        val article =
          item.getModelObject

        val rowKey =
          RowKey.from(article)

        val edited =
          editedValues.getOrElse(
            rowKey,
            (
              article.orderQuantity,
              article.price
            )
          )

        item.add(
          new Label(
            "resultSgCnu",
            article.sgCnu
          )
        )

        item.add(
          new Label(
            "resultArticleNumber",
            article.articleNumber
          )
        )

        item.add(
          new Label(
            "resultVariantNumber",
            article.variantNumber
          )
        )

        item.add(
          new Label(
            "resultBundleNumber",
            article.bundleNumber
          )
        )

        item.add(
          new Label(
            "resultSubsystemNumber",
            article.subsystemNumber
          )
        )

        item.add(
          new Label(
            "resultDescription",
            article.description
          )
        )

        item.add(
          new Label(
            "resultSortText",
            article.sortText
          )
        )

        item.add(
          new Label(
            "resultSizeText",
            article.sizeText
          )
        )

        item.add(
          new Label(
            "resultCbb",
            article.cbb
          )
        )

        item.add(
          new Label(
            "resultMoq",
            article.moq
          )
        )

        item.add(
          editableValueField(
            "resultOrderQuantity",
            edited._1,
            rowKey,
            isOrderQuantity = true
          )
        )

        item.add(
          editableValueField(
            "resultPrice",
            edited._2,
            rowKey,
            isOrderQuantity = false
          )
        )

        item.add(
          new Label(
            "resultMrp",
            article.mrp
          )
        )

        item.add(
          new Label(
            "resultPromotion",
            article.promotion
          )
        )

        item.add(
          new Label(
            "resultStock",
            article.stock
          )
        )
      }
    }

  private val resultRowsContainer =
    new WebMarkupContainer(
      "resultRowsContainer"
    )

  resultRowsContainer.setOutputMarkupId(true)
  resultRowsContainer.add(resultRows)

  // ============================================================
  // RESULT TOOLBAR
  // ============================================================

  private val resultToolbar =
    new WebMarkupContainer(
      "resultToolbar"
    )

  private val saveIcon =
    new WebMarkupContainer(
      "saveIcon"
    )

  saveIcon.setOutputMarkupId(true)

  saveIcon.add(
    AttributeModifier.replace(
      "disabled",
      saveEnabledModel.map(
        enabled =>
          if (enabled) null
          else "disabled"
      )
    )
  )

  saveIcon.add(
    new AjaxEventBehavior("click") {

      override def onEvent(
          target: AjaxRequestTarget
      ): Unit = {
        saveChanges(target)
      }
    }
  )

  resultToolbar.add(saveIcon)

  // IMPORTANT:
  // saveMessage is NOT added to resultToolbar anymore.

  resultToolbar.add(
    new WebMarkupContainer(
      "refreshIcon"
    )
  )

  resultToolbar.add(
    new WebMarkupContainer(
      "sendIcon"
    )
  )

  resultToolbar.add(
    new WebMarkupContainer(
      "truckIcon"
    )
  )

  resultToolbar.add(
    new WebMarkupContainer(
      "databaseIcon"
    )
  )

  private val exportIcon = new WebMarkupContainer("exportIcon")
  exportIcon.setOutputMarkupId(true)
  exportIcon.add(
    AttributeModifier.replace(
      "disabled",
      exportEnabledModel.map(enabled => if (enabled) null else "disabled")
    )
  )
  exportIcon.add(new AjaxEventBehavior("click") {
    override def onEvent(target: AjaxRequestTarget): Unit = {
      savedExportData.foreach(data => setResponsePage(new DsdOrderExportConfirmationPage(data)))
    }
  })
  resultToolbar.add(exportIcon)

  // ============================================================
  // SAVE MESSAGE
  // ============================================================

  private val saveMessage =
    new Label(
      "saveMessage",
      saveMessageModel
    )

  saveMessage.setOutputMarkupId(true)

  // Hide the message initially.
  saveMessage.setVisible(false)

  // Keep a placeholder so Ajax can make it visible later.
  saveMessage.setOutputMarkupPlaceholderTag(true)

  // ============================================================
  // PAGINATION
  // ============================================================

  private val pagination =
    new WebMarkupContainer(
      "pagination"
    )

  pagination.setOutputMarkupId(true)

  pagination.add(
    new AjaxLink[Void](
      "firstPageLink"
    ) {

      override def onClick(
          target: AjaxRequestTarget
      ): Unit = {

        showPage(0)

        target.add(
          resultTableContainer
        )
      }
    }
  )

  pagination.add(
    new AjaxLink[Void](
      "previousPageLink"
    ) {

      override def onClick(
          target: AjaxRequestTarget
      ): Unit = {

        showPage(
          currentPageModel.getObject - 1
        )

        target.add(
          resultTableContainer
        )
      }
    }
  )

  pagination.add(
    new ListView[Int](
      "pageNumbers",
      pageNumbersModel
    ) {

      override def populateItem(
          item: org.apache.wicket.markup.html.list.ListItem[Int]
      ): Unit = {

        val page =
          item.getModelObject

        item.add(
          new AjaxLink[Void](
            "pageLink"
          ) {

            override def onClick(
                target: AjaxRequestTarget
            ): Unit = {

              showPage(page)

              target.add(
                resultTableContainer
              )
            }
          }.add(
            new Label(
              "pageNumber",
              (page + 1).toString
            )
          )
        )
      }
    }
  )

  pagination.add(
    new AjaxLink[Void](
      "nextPageLink"
    ) {

      override def onClick(
          target: AjaxRequestTarget
      ): Unit = {

        showPage(
          currentPageModel.getObject + 1
        )

        target.add(
          resultTableContainer
        )
      }
    }
  )

  pagination.add(
    new AjaxLink[Void](
      "lastPageLink"
    ) {

      override def onClick(
          target: AjaxRequestTarget
      ): Unit = {

        showPage(
          pageCount - 1
        )

        target.add(
          resultTableContainer
        )
      }
    }
  )

  pagination.add(
    new Label(
      "resultCount",
      resultCountModel
    )
  )

  pagination.add(
    new WebMarkupContainer(
      "gridIcon"
    )
  )

  private def pageCount: Int =
    math.max(
      1,
      math.ceil(
        allFilteredRows.size.toDouble / pageSize
      ).toInt
    )

  private def showPage(
      page: Int
  ): Unit = {

    val boundedPage =
      math.max(
        0,
        math.min(
          page,
          pageCount - 1
        )
      )

    currentPageModel.setObject(
      boundedPage
    )

    val start =
      boundedPage * pageSize

    resultRowsModel.setObject(
      allFilteredRows
        .slice(
          start,
          start + pageSize
        )
        .asJava
    )
  }

  // ============================================================
  // EDITABLE VALUE FIELD
  // ============================================================

  private def editableValueField(
      wicketId: String,
      value: String,
      rowKey: RowKey,
      isOrderQuantity: Boolean
  ): TextField[String] = {

    val field =
      new TextField[String](
        wicketId,
        Model.of(value)
      )

    field.setOutputMarkupId(true)

    field.add(
      new AjaxEventBehavior("change") {

        override def onEvent(
            target: AjaxRequestTarget
        ): Unit = {

          val submittedValue =
            getRequest
              .getRequestParameters
              .getParameterValue(
                field.getInputName
              )
              .toString

          updateEditedValue(
            rowKey,
            submittedValue,
            isOrderQuantity
          )

          target.add(
            saveIcon
          )
        }
      }
    )

    field
  }

  private def updateEditedValue(
      rowKey: RowKey,
      value: String,
      isOrderQuantity: Boolean
  ): Unit = {

    val original =
      allFilteredRows
        .find(
          article =>
            RowKey.from(article) == rowKey
        )
        .map(
          article =>
            (
              article.orderQuantity,
              article.price
            )
        )
        .getOrElse(
          ("", "")
        )

    val current =
      editedValues.getOrElse(
        rowKey,
        original
      )

    val updated =
      if (isOrderQuantity)
        (
          value,
          current._2
        )
      else
        (
          current._1,
          value
        )

    if (updated == original)
      editedValues.remove(rowKey)
    else
      editedValues.update(
        rowKey,
        updated
      )

    saveEnabledModel.setObject(
      editedValues.nonEmpty
    )
  }

  // ============================================================
  // SAVE
  // ============================================================

  private def saveChanges(
      target: AjaxRequestTarget
  ): Unit = {

    val invalid =
      editedValues.values
        .flatMap { values =>
          Seq(
            values._1,
            values._2
          ).find(
            value =>
              value.trim.isEmpty ||
              !isDecimal(value)
          )
        }
        .toSeq
        .headOption

    invalid match {

      case Some(_) =>

        saveMessageModel.setObject(
          "Order quantity and price must be numeric values."
        )

        saveMessage.setVisible(true)

      case None =>

        allFilteredRows.indices.foreach { index =>

          val article =
            allFilteredRows(index)

          editedValues
            .get(
              RowKey.from(article)
            )
            .foreach { values =>

              allFilteredRows.update(
                index,
                article.copy(
                  orderQuantity = values._1,
                  price = values._2
                )
              )
            }
        }

        editedValues.clear()

        saveEnabledModel.setObject(
          false
        )

        savedExportData = Some(
          DsdOrderExportData(
            puar = summaryPuarModel.getObject,
            sl = summarySlModel.getObject,
            sid = summarySidModel.getObject,
            storeName = summaryStoreNameModel.getObject,
            supplierNumber = supplierNumberModel.getObject,
            supplierName = supplierNameModel.getObject,
            deliveryDay = summaryDeliveryDayModel.getObject,
            order = summaryOrderModel.getObject,
            rows = allFilteredRows.toSeq
          )
        )
        exportEnabledModel.setObject(true)

        saveMessageModel.setObject(
          "DSD Order details saved successfully."
        )

        // Show confirmation message above the result table.
        saveMessage.setVisible(true)

        showPage(
          currentPageModel.getObject
        )

        target.add(
          resultRowsContainer,
          pagination
        )
    }

    target.add(
          saveIcon,
          exportIcon,
      saveMessage
    )
  }

  private def isDecimal(
      value: String
  ): Boolean = {

    try {
      new BigDecimal(
        value.trim
      )

      true

    } catch {
      case _: NumberFormatException =>
        false
    }
  }

  private def refreshPagination(): Unit = {

    pageNumbersModel.setObject(
      (0 until pageCount).toList.asJava
    )

    resultCountModel.setObject(
      allFilteredRows.size
    )
  }

  // ============================================================
  // EMPTY STATE
  // ============================================================

  private val emptyState =
    new WebMarkupContainer(
      "emptyState"
    )

  emptyState.setOutputMarkupId(true)

  // ============================================================
  // FILTER SUMMARY
  // ============================================================

  private val filterSummary =
    new WebMarkupContainer(
      "filterSummary"
    )

  filterSummary.setOutputMarkupId(true)
  filterSummary.setVisible(false)

  filterSummary.add(
    new Label(
      "summaryPuar",
      summaryPuarModel
    )
  )

  filterSummary.add(
    new Label(
      "summarySl",
      summarySlModel
    )
  )

  filterSummary.add(
    new Label(
      "summarySid",
      summarySidModel
    )
  )

  filterSummary.add(
    new Label(
      "summaryStoreName",
      summaryStoreNameModel
    )
  )

  filterSummary.add(
    new Label(
      "summarySupplierNumber",
      supplierNumberModel
    )
  )

  filterSummary.add(
    new Label(
      "summarySupplierName",
      supplierNameModel
    )
  )

  filterSummary.add(
    new Label(
      "summaryDeliveryDay",
      summaryDeliveryDayModel
    )
  )

  filterSummary.add(
    new Label(
      "summaryOrder",
      summaryOrderModel
    )
  )

  // ============================================================
  // RESULT TABLE CONTAINER
  // ============================================================

  private val resultTableContainer =
    new WebMarkupContainer(
      "resultTableContainer"
    )

  resultTableContainer.setOutputMarkupId(true)
  resultTableContainer.setVisible(false)

  resultTableContainer.add(
    resultToolbar
  )

  resultTableContainer.add(
    resultRowsContainer
  )

  resultTableContainer.add(
    pagination
  )

  // ============================================================
  // RESULT AREA
  // ============================================================

  private val resultArea =
    new WebMarkupContainer(
      "resultArea"
    )

  resultArea.setOutputMarkupId(true)

  resultArea.add(
    filterSummary
  )

  // IMPORTANT:
  // saveMessage is added here, outside resultToolbar
  // and before resultTableContainer.
  resultArea.add(
    saveMessage
  )

  resultArea.add(
    emptyState
  )

  resultArea.add(
    resultTableContainer
  )

  // ============================================================
  // SUPPLIER ROWS
  // ============================================================

  private val supplierRows =
    new ListView[Supplier](
      "supplierRows",
      supplierRowsModel
    ) {

      setOutputMarkupId(true)

      override def populateItem(
          item: org.apache.wicket.markup.html.list.ListItem[Supplier]
      ): Unit = {

        val supplier =
          item.getModelObject

        val supplierRadio =
          new Radio[Supplier](
            "supplierRadio",
            item.getModel
          )

        supplierRadio.add(
          new AjaxEventBehavior("change") {

            override def onEvent(
                target: AjaxRequestTarget
            ): Unit = {

              selectedSupplierModel.setObject(
                supplier
              )

              supplierNumberModel.setObject(
                supplier.number
              )

              supplierNameModel.setObject(
                supplier.name
              )

              target.add(
                supplierNumberField,
                supplierNameField
              )
            }
          }
        )

        item.add(
          supplierRadio
        )

        item.add(
          new Label(
            "supplierNumber",
            supplier.number
          )
        )

        item.add(
          new Label(
            "supplierName",
            supplier.name
          )
        )
      }
    }

  private val supplierRowsContainer =
    new WebMarkupContainer(
      "supplierRowsContainer"
    )

  supplierRowsContainer.setOutputMarkupId(true)
  supplierRowsContainer.add(supplierRows)

  private val supplierGroup =
    new RadioGroup[Supplier](
      "supplierGroup",
      selectedSupplierModel
    )

  supplierGroup.add(
    supplierRowsContainer
  )

  private def refreshSuppliers(): Unit = {
    supplierRowsModel.setObject(
      supplierService
        .search(
          supplierNumberModel.getObject,
          supplierNameModel.getObject
        )
        .asJava
    )
  }

  // ============================================================
  // REFRESH ORDER ARTICLES
  // ============================================================

  private def refreshOrderArticles(): Unit = {

    allFilteredRows.clear()

    editedValues.clear()

    saveEnabledModel.setObject(
      false
    )
    exportEnabledModel.setObject(false)
    savedExportData = None

    saveMessageModel.setObject(
      ""
    )

    // Hide previous save message when a new result is loaded.
    saveMessage.setVisible(false)

    allFilteredRows ++=
      dsdOrderArticleService.search(
        orderListModel.getObject,
        orderNumberModel.getObject,
        articleNumberModel.getObject,
        subsystemNumberModel.getObject,
        descriptionModel.getObject,
        Option(
          selectedSupplierModel.getObject
        ).map(
          _.number
        ).getOrElse(""),
        Option(
          selectedSupplierModel.getObject
        ).map(
          _.name
        ).getOrElse("")
      )

    refreshPagination()

    showPage(0)
  }

  private def refreshFilterSummary(): Unit = {

    summaryOrderModel.setObject(
      orderListModel.getObject
    )
  }

  // ============================================================
  // ROW KEY
  // ============================================================

  private final case class RowKey(
      orderNumber: String,
      articleNumber: String,
      subsystemNumber: String,
      variantNumber: String,
      bundleNumber: String
  )

  private object RowKey {

    def from(
        article: DsdOrderArticle
    ): RowKey = {

      RowKey(
        article.orderNumber,
        article.articleNumber,
        article.subsystemNumber,
        article.variantNumber,
        article.bundleNumber
      )
    }
  }

  // ============================================================
  // FILTER BEHAVIORS
  // ============================================================

  private def orderCriteriaBehavior(
      event: String
  ): AjaxFormComponentUpdatingBehavior = {

    new AjaxFormComponentUpdatingBehavior(event) {

      override def onUpdate(
          target: AjaxRequestTarget
      ): Unit = ()
    }
  }

  private val supplierSearchBehavior =
    new AjaxFormComponentUpdatingBehavior("input") {

      override def onUpdate(
          target: AjaxRequestTarget
      ): Unit = {

        refreshSuppliers()

        target.add(supplierRowsContainer)
      }
    }

  supplierNumberField.add(
    supplierSearchBehavior
  )

  supplierNameField.add(
    new AjaxFormComponentUpdatingBehavior("input") {

      override def onUpdate(
          target: AjaxRequestTarget
      ): Unit = {

        refreshSuppliers()

        target.add(supplierRowsContainer)
      }
    }
  )

  refreshSuppliers()

  orderListField.add(
    orderCriteriaBehavior("change")
  )

  orderNumberField.add(
    orderCriteriaBehavior("input")
  )

  articleNumberField.add(
    orderCriteriaBehavior("input")
  )

  subsystemNumberField.add(
    orderCriteriaBehavior("input")
  )

  descriptionField.add(
    orderCriteriaBehavior("input")
  )

  filterModal.add(
    supplierNumberField
  )

  filterModal.add(deliveryDayPicker)

  filterModal.add(
    supplierNameField
  )

  filterModal.add(
    supplierGroup
  )

  filterModal.add(
    orderListField
  )

  filterModal.add(
    orderNumberField
  )

  filterModal.add(
    articleNumberField
  )

  filterModal.add(
    subsystemNumberField
  )

  filterModal.add(
    descriptionField
  )

  add(
    resultArea
  )

  // ============================================================
  // FILTER OPEN
  // ============================================================

  add(
    new AjaxLink[Void](
      "openFilterLink"
    ) {

      override def onClick(
          target: AjaxRequestTarget
      ): Unit = {

        filterModal.setVisible(true)

        target.add(
          filterModal
        )
      }
    }
  )

  // ============================================================
  // FILTER CLOSE
  // ============================================================

  filterModal.add(
    new AjaxLink[Void](
      "closeFilterLink"
    ) {

      override def onClick(
          target: AjaxRequestTarget
      ): Unit = {

        filterModal.setVisible(false)

        target.add(
          filterModal
        )
      }
    }
  )

  filterModal.add(
    new AjaxLink[Void](
      "cancelFilterLink"
    ) {

      override def onClick(
          target: AjaxRequestTarget
      ): Unit = {

        filterModal.setVisible(false)

        target.add(
          filterModal
        )
      }
    }
  )

  // ============================================================
  // APPLY FILTER
  // ============================================================

  filterModal.add(
    new AjaxLink[Void](
      "applyFilterLink"
    ) {

      override def onClick(
          target: AjaxRequestTarget
      ): Unit = {

        refreshOrderArticles()

        refreshFilterSummary()

        filterModal.setVisible(false)

        filterSummary.setVisible(true)

        emptyState.setVisible(false)

        resultTableContainer.setVisible(true)

        target.add(
          filterModal,
          resultArea
        )
      }
    }
  )

  add(
    filterModal
  )

  // ============================================================
  // CSS
  // ============================================================

  override def renderHead(
      response: IHeaderResponse
  ): Unit = {

    super.renderHead(response)

    response.render(
      CssHeaderItem.forReference(
        new PackageResourceReference(
          classOf[DsdOrderPage],
          "DsdOrderPage.css"
        )
      )
    )
  }
}