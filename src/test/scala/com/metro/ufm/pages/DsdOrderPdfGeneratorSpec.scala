package com.metro.ufm.pages

import com.metro.ufm.models.DsdOrderArticle
import org.scalatest.funsuite.AnyFunSuite

final class DsdOrderPdfGeneratorSpec extends AnyFunSuite {
  test("generates a landscape PDF from the saved DSD order snapshot") {
    val data = DsdOrderExportData(
      "151", "11", "10 - MUELHEIM", "MUELHEIM", "1049", "YEX BV",
      "21.09.2026", "DSD-151-11",
      Seq(DsdOrderArticle(
        "DSD-151-11", "45001234", "100245", "01", "Organic bananas 1kg",
        "2710", "2", "1", "Organic bananas", "1kg", "1", "1.00",
        "3.00", "12.50", "0.00", "0.00", "0.00", "1049", "YEX BV"
      ))
    )

    val pdf = DsdOrderPdfGenerator.generate(data)

    assert(pdf.length > 100)
    assert(new String(pdf.take(4), "US-ASCII") == "%PDF")
  }
}
