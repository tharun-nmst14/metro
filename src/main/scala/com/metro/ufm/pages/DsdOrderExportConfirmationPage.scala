package com.metro.ufm.pages

import com.metro.ufm.models.DsdOrderArticle
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.handler.resource.ResourceRequestHandler
import org.apache.wicket.request.resource.AbstractResource.{ResourceResponse, WriteCallback}
import org.apache.wicket.request.resource.{
  AbstractResource,
  ContentDisposition,
  IResource,
  PackageResourceReference
}

import scala.jdk.CollectionConverters._

class DsdOrderExportConfirmationPage(data: DsdOrderExportData)
    extends WebPage {

  // ============================================================
  // SAVED FILTER / HEADER INFORMATION
  // ============================================================

  add(new Label("summaryPuar", data.puar))

  add(new Label("summarySl", data.sl))

  add(new Label("summarySid", data.sid))

  add(new Label("summaryStoreName", data.storeName))

  add(new Label("summarySupplierNumber", data.supplierNumber))

  add(new Label("summarySupplierName", data.supplierName))

  add(new Label("summaryDeliveryDay", data.deliveryDay))

  add(new Label("summaryOrder", data.order))

  // ============================================================
  // SAVED DSD ORDER RESULT ROWS
  // ============================================================

  add(new ListView[DsdOrderArticle](
    "savedRows",
    data.rows.asJava
  ) {

    override def populateItem(
        item: org.apache.wicket.markup.html.list.ListItem[DsdOrderArticle]
    ): Unit = {

      val row = item.getModelObject

      Seq(
        "resultSgCnu" -> row.sgCnu,
        "resultArticleNumber" -> row.articleNumber,
        "resultVariantNumber" -> row.variantNumber,
        "resultBundleNumber" -> row.bundleNumber,
        "resultSubsystemNumber" -> row.subsystemNumber,
        "resultDescription" -> row.description,
        "resultSortText" -> row.sortText,
        "resultSizeText" -> row.sizeText,
        "resultCbb" -> row.cbb,
        "resultMoq" -> row.moq,
        "resultOrderQuantity" -> row.orderQuantity,
        "resultPrice" -> row.price,
        "resultMrp" -> row.mrp,
        "resultPromotion" -> row.promotion,
        "resultStock" -> row.stock
      ).foreach {
        case (id, value) =>
          item.add(new Label(id, value))
      }
    }
  })

  // ============================================================
  // BACK BUTTON
  // ============================================================

  add(new Link[Void]("backLink") {

    override def onClick(): Unit = {
      setResponsePage(new DsdOrderPage())
    }
  })

  // ============================================================
  // EXPORT ERROR MESSAGE
  // ============================================================

  private val exportMessage =
    new Label("exportMessage", "")

  exportMessage.setOutputMarkupPlaceholderTag(true)
  exportMessage.setVisible(false)

  add(exportMessage)

  // ============================================================
  // CONFIRM EXPORT
  // ============================================================
  //
  // IMPORTANT:
  // This is intentionally a normal Wicket Link rather than
  // AjaxLink.
  //
  // A PDF download needs a normal resource response. Using
  // AjaxLink can cause the Ajax response to interfere with
  // the browser download.
  // ============================================================

  add(new Link[Void]("confirmExportLink") {

    override def onClick(): Unit = {

      try {

        // --------------------------------------------------------
        // Generate PDF from the already saved in-memory data.
        // No Oracle/database query is performed here.
        // --------------------------------------------------------

        val bytes =
          DsdOrderPdfGenerator.generate(data)

        // --------------------------------------------------------
        // Create a Wicket resource which contains the PDF bytes.
        // --------------------------------------------------------

        val resource = new AbstractResource {

          override protected def newResourceResponse(
              attributes: IResource.Attributes
          ): ResourceResponse = {

            val response =
              new ResourceResponse()

            // PDF content type
            response.setContentType("application/pdf")

            // Force browser download
            response.setContentDisposition(
              ContentDisposition.ATTACHMENT
            )

            // Downloaded filename
            response.setFileName(
              "dsd-order-export-confirmation.pdf"
            )

            // Write generated PDF bytes to HTTP response
            response.setWriteCallback(
              new WriteCallback {

                override def writeData(
                    attributes: IResource.Attributes
                ): Unit = {

                  attributes.getResponse.write(bytes)
                }
              }
            )

            response
          }
        }

        // --------------------------------------------------------
        // Schedule the resource request after the current
        // Wicket request.
        // --------------------------------------------------------

        RequestCycle
          .get()
          .scheduleRequestHandlerAfterCurrent(
            new ResourceRequestHandler(
              resource,
              null
            )
          )

      } catch {

        case _: Exception =>

          // ------------------------------------------------------
          // User-friendly error message.
          // ------------------------------------------------------

          exportMessage.setDefaultModelObject(
            "Unable to generate the PDF export. Please try again."
          )

          exportMessage.setVisible(true)
      }
    }
  })

  // ============================================================
  // PAGE CSS
  // ============================================================

  override def renderHead(
      response: IHeaderResponse
  ): Unit = {

    super.renderHead(response)

    response.render(
      CssHeaderItem.forReference(
        new PackageResourceReference(
          classOf[DsdOrderExportConfirmationPage],
          "DsdOrderExportConfirmationPage.css"
        )
      )
    )
  }
}