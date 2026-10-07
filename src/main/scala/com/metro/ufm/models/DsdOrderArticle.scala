package com.metro.ufm.models

final case class DsdOrderArticle(
  orderList: String,
  orderNumber: String,
  articleNumber: String,
  subsystemNumber: String,
  description: String,
  sgCnu: String,
  variantNumber: String,
  bundleNumber: String,
  sortText: String,
  sizeText: String,
  cbb: String,
  moq: String,
  orderQuantity: String,
  price: String,
  mrp: String,
  promotion: String,
  stock: String,
  supplierNumber: String,
  supplierName: String
) extends Serializable
