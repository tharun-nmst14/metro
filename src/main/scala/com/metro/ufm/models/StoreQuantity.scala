package com.metro.ufm.models

case class StoreQuantity(
    articleNo: String,
    description: String,
    subsystemNo: String,
    merchandiseGroup: String,
    stockQuantity: Int,
    salesQuantity: Int,
    orderQuantity: Int
)

case class StoreQuantityFilter(
    puar: String,
    sid: String,
    orderDay: String,
    cutoffTime: String
)