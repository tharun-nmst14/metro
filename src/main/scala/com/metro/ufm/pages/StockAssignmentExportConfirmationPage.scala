package com.metro.ufm.pages

import com.metro.ufm.models.StockAssignmentResult
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.handler.resource.ResourceRequestHandler
import org.apache.wicket.request.resource.AbstractResource.{ResourceResponse, WriteCallback}
import org.apache.wicket.request.resource.{AbstractResource, ContentDisposition, IResource, PackageResourceReference}

import scala.jdk.CollectionConverters._

class StockAssignmentExportConfirmationPage(data: StockAssignmentExportData) extends WebPage {
  add(new Label("summaryAssortment", data.assortment))
  add(new Label("summaryDc", data.dc))
  add(new Label("summaryAssignmentDay", data.assignmentDay))
  add(new Label("summaryDeliveryDaySid", data.deliveryDaySid))

  add(new ListView[StockAssignmentResult]("savedRows", data.rows.asJava) {
    override def populateItem(item: org.apache.wicket.markup.html.list.ListItem[StockAssignmentResult]): Unit = {
      val row = item.getModelObject
      Seq(
        "resultSgCnu" -> row.sgCnu,
        "resultMerchGroup" -> row.merchGroup,
        "resultArticleNumber" -> row.articleNumber,
        "resultVariant" -> row.variant,
        "resultBundle" -> row.bundle,
        "resultSubsystemNumber" -> row.subsystemNumber,
        "resultDescription" -> row.description,
        "resultSortText" -> row.sortText,
        "resultSizeText" -> row.sizeText,
        "resultCbb" -> row.cbb,
        "resultBc" -> row.bc,
        "resultSalesForecast" -> row.salesForecast,
        "resultStoreQuantity" -> row.storeQuantity,
        "resultAssignedStockQuantity" -> row.assignedStockQuantity,
        "resultRecalc" -> row.recalc,
        "resultKeyDistribution" -> row.keyDistribution,
        "resultQuantityGap" -> row.quantityGap,
        "resultRemainingQuantity" -> row.remainingQuantity,
        "resultUfmStock" -> row.ufmStock,
        "resultPromotion" -> row.promotion,
        "resultIbc" -> row.ibc,
        "resultArticleExchange" -> row.articleExchange
      ).foreach { case (id, value) =>
        item.add(new Label(id, value))
      }
    }
  })

  add(new Link[Void]("backLink") {
    override def onClick(): Unit = setResponsePage(new StockAssignmentPage())
  })

  private val exportMessage = new Label("exportMessage", "")
  exportMessage.setOutputMarkupPlaceholderTag(true)
  exportMessage.setVisible(false)
  add(exportMessage)

  add(new Link[Void]("confirmExportLink") {
    override def onClick(): Unit = {
      try {
        val bytes = StockAssignmentPdfGenerator.generate(data.rows)
        val resource = new AbstractResource {
          override protected def newResourceResponse(attributes: IResource.Attributes): ResourceResponse = {
            val response = new ResourceResponse()
            response.setContentType("application/pdf")
            response.setContentDisposition(ContentDisposition.ATTACHMENT)
            response.setFileName("stock-assignment-export-confirmation.pdf")
            response.setWriteCallback(new WriteCallback {
              override def writeData(attributes: IResource.Attributes): Unit =
                attributes.getResponse.write(bytes)
            })
            response
          }
        }
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(new ResourceRequestHandler(resource, null))
      } catch {
        case _: Exception =>
          exportMessage.setDefaultModelObject("Unable to generate the PDF export. Please try again.")
          exportMessage.setVisible(true)
      }
    }
  })

  override def renderHead(response: IHeaderResponse): Unit = {
    super.renderHead(response)
    response.render(
      CssHeaderItem.forReference(
        new PackageResourceReference(classOf[DsdOrderExportConfirmationPage], "DsdOrderExportConfirmationPage.css")
      )
    )
  }
}
