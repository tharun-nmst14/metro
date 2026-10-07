package com.metro.ufm.models

final case class StockAssignmentResult(
  sgCnu: String,
  merchGroup: String,
  articleNumber: String,
  variant: String,
  bundle: String,
  subsystemNumber: String,
  description: String,
  sortText: String,
  sizeText: String,
  cbb: String,
  bc: String,
  salesForecast: String,
  storeQuantity: String,
  assignedStockQuantity: String,
  recalc: String,
  keyDistribution: String,
  quantityGap: String,
  remainingQuantity: String,
  ufmStock: String,
  promotion: String,
  ibc: String,
  articleExchange: String
) extends Serializable
