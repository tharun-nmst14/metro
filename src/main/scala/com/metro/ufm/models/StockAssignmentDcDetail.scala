package com.metro.ufm.models

final case class StockAssignmentDcDetail(
  dcNumber: String,
  dc: String,
  bc: String,
  orderFactor: String,
  salesForecast: String,
  storeQuantity: String,
  assignedStockQuantity: String,
  quantityGap: String,
  remainingStock: String,
  ufmStock: String,
  averageNnbp: String
) extends Serializable
