package com.metro.ufm.pages

import java.io.ByteArrayOutputStream

import com.lowagie.text.{Document, Element, PageSize, Paragraph}
import com.lowagie.text.pdf.{PdfPTable, PdfWriter}
import com.metro.ufm.models.StockAssignmentResult

object StockAssignmentPdfGenerator {
  private val Title = "Stock Assignment Export Confirmation"
  private val Headers = Seq(
    "S/G (CNU)", "Merch Group", "Art-No", "Variant", "Bundle", "Subsystem",
    "Description", "Sorttext", "Sizetext", "CBB", "BC", "Sales Forecast",
    "Store Qty", "Ass. Stock Qty", "Recalc", "Key Distribution", "Qty Gap",
    "Remaining Qty", "UFM Stock", "Promotion", "IBC", "Article Exchange"
  )

  def generate(rows: Seq[StockAssignmentResult]): Array[Byte] = {
    val output = new ByteArrayOutputStream()
    val document = new Document(PageSize.A4.rotate(), 18, 18, 18, 18)

    PdfWriter.getInstance(document, output)
    document.open()
    try {
      val title = new Paragraph(Title)
      title.setAlignment(Element.ALIGN_CENTER)
      document.add(title)
      document.add(new Paragraph(" "))

      val table = new PdfPTable(Headers.length)
      table.setWidthPercentage(100)
      Headers.foreach(table.addCell)
      rows.foreach { row =>
        Seq(
          row.sgCnu, row.merchGroup, row.articleNumber, row.variant, row.bundle,
          row.subsystemNumber, row.description, row.sortText, row.sizeText,
          row.cbb, row.bc, row.salesForecast, row.storeQuantity,
          row.assignedStockQuantity, row.recalc, row.keyDistribution,
          row.quantityGap, row.remainingQuantity, row.ufmStock, row.promotion,
          row.ibc, row.articleExchange
        ).foreach(value => table.addCell(Option(value).getOrElse("")))
      }
      document.add(table)
    } finally {
      document.close()
    }

    output.toByteArray
  }
}
