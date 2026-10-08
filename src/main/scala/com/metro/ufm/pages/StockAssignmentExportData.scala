package com.metro.ufm.pages

import com.metro.ufm.models.StockAssignmentResult

final case class StockAssignmentExportData(
  assortment: String,
  dc: String,
  assignmentDay: String,
  deliveryDaySid: String,
  rows: Seq[StockAssignmentResult]
) extends Serializable
