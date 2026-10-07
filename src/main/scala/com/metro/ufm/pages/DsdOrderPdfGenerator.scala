package com.metro.ufm.pages

import java.io.ByteArrayOutputStream

import com.lowagie.text.{Document, Element, PageSize, Paragraph}
import com.lowagie.text.pdf.{PdfPTable, PdfWriter}

object DsdOrderPdfGenerator {
  private val Title = "DSD Order Export Confirmation"
  private val Headers = Seq(
    "S/G (CNU)", "Art-No", "Var-No", "Bundle-No", "Subsys No.",
    "Description", "Sorttext", "Sizetext", "CBB", "MOQ", "Order Qty",
    "Price", "MRP", "Promotion", "Stock"
  )

  def generate(data: DsdOrderExportData): Array[Byte] = {
    val output = new ByteArrayOutputStream()
    val document = new Document(PageSize.A4.rotate(), 24, 24, 24, 24)

    PdfWriter.getInstance(document, output)
    document.open()
    try {
      val title = new Paragraph(Title)
      title.setAlignment(Element.ALIGN_CENTER)
      document.add(title)
      document.add(new Paragraph("PUAR: " + data.puar + "    SL: " + data.sl + "    SID: " + data.sid))
      document.add(new Paragraph("Store Name: " + data.storeName + "    Supplier No: " + data.supplierNumber + "    Supplier Name: " + data.supplierName))
      document.add(new Paragraph("Delivery Day: " + data.deliveryDay + "    Order: " + data.order))
      document.add(new Paragraph(" "))

      val table = new PdfPTable(Headers.length)
      table.setWidthPercentage(100)
      Headers.foreach(header => table.addCell(header))
      data.rows.foreach { row =>
        Seq(
          row.sgCnu, row.articleNumber, row.variantNumber, row.bundleNumber,
          row.subsystemNumber, row.description, row.sortText, row.sizeText,
          row.cbb, row.moq, row.orderQuantity, row.price, row.mrp,
          row.promotion, row.stock
        ).foreach(value => table.addCell(Option(value).getOrElse("")))
      }
      document.add(table)
    } finally {
      document.close()
    }

    output.toByteArray
  }
}
