package com.metro.ufm.pages

import com.metro.ufm.models.DsdOrderArticle

final case class DsdOrderExportData(
  puar: String,
  sl: String,
  sid: String,
  storeName: String,
  supplierNumber: String,
  supplierName: String,
  deliveryDay: String,
  order: String,
  rows: Seq[DsdOrderArticle]
) extends Serializable
