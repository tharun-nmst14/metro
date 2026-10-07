package com.metro.ufm.repositories

import com.metro.ufm.models.Supplier

trait SupplierRepository {
  def findAll(): Seq[Supplier]
}

final class MockSupplierRepository extends SupplierRepository {
  override def findAll(): Seq[Supplier] = Seq(
    Supplier("1049", "YEX BV"),
    Supplier("1053", "TENFOOD BV"),
    Supplier("1068", "PHU IMPORT EXPORT"),
    Supplier("1312", "MEDICAL PLUS GMBH"),
    Supplier("1399", "COCA-COLA EURO PACIFIC PARTNERS"),
    Supplier("1402", "PepsiCo Beverages Deutschland GmbH"),
    Supplier("1409", "Nestlé Deutschland AG")

  )
}
