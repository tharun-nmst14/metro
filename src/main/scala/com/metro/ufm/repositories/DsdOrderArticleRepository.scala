package com.metro.ufm.repositories

import com.metro.ufm.models.DsdOrderArticle

trait DsdOrderArticleRepository {
  def findAll(): Seq[DsdOrderArticle]
}

final class MockDsdOrderArticleRepository extends DsdOrderArticleRepository {
  override def findAll(): Seq[DsdOrderArticle] = Seq(
    DsdOrderArticle("DSD-151-11", "45001234", "100245", "01", "Organic bananas 1kg", "2710", "2", "1", "Organic bananas", "1kg", "1", "1.00", "0.00", "12.50", "0.00", "", "0.00", "1049", "YEX BV"),
    DsdOrderArticle("DSD-151-11", "45001234", "100246", "02", "Fairtrade bananas 1kg", "2710", "4", "1", "Fairtrade bananas", "1kg", "1", "1.00", "0.00", "13.00", "0.00", "", "0.00", "1049", "YEX BV"),
    DsdOrderArticle("DSD-151-11", "45001258", "200311", "01", "Red seedless grapes 500g", "2105", "5", "2", "Red seedless grapes", "500g", "12", "1.00", "0.00", "18.50", "0.00", "", "0.00", "1053", "TENFOOD BV"),
    DsdOrderArticle("DSD-151-11", "45001302", "300118", "03", "Avocado Hass loose", "1710", "9", "3", "Avocado Hass", "loose", "7", "1.00", "0.00", "22.00", "0.00", "", "0.00", "1068", "PHU IMPORT EXPORT"),
    DsdOrderArticle("DSD-151-12", "45001344", "400502", "01", "Fresh strawberries 400g", "1610", "1", "1", "Fresh strawberries", "400g", "6", "1.00", "0.00", "15.00", "0.00", "", "0.00", "1312", "MEDICAL PLUS GMBH")
  )
}
