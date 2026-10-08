package com.metro.ufm.pages

import com.metro.ufm.models.StockAssignmentResult
import org.scalatest.funsuite.AnyFunSuite

final class StockAssignmentPdfGeneratorSpec extends AnyFunSuite {
  test("generates a PDF using the saved assigned stock quantity") {
    val row = StockAssignmentResult(
      "1", "Fresh", "45001234", "100245", "01", "10",
      "Organic bananas 1kg", "2710", "1kg", "1", "1",
      "12.50", "3", "24", "0", "0", "0", "21",
      "18", "0", "0", "0"
    )

    val pdf = StockAssignmentPdfGenerator.generate(Seq(row))

    assert(pdf.length > 100)
    assert(new String(pdf.take(4), "US-ASCII") == "%PDF")
  }
}
