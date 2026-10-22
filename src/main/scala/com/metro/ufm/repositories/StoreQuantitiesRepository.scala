package com.metro.ufm.repositories

import com.metro.ufm.models.StoreQuantity

class StoreQuantitiesRepository extends Serializable {

  private val mockData: List[StoreQuantity] = List(
    StoreQuantity(
      "A1001",
      "Fresh Milk 1L",
      "S001",
      "Dairy",
      100,
      50,
      20
    ),
    StoreQuantity(
      "A1002",
      "Whole Wheat Bread",
      "S002",
      "Bakery",
      80,
      30,
      15
    ),
    StoreQuantity(
      "A1003",
      "Organic Eggs 12pcs",
      "S003",
      "Dairy",
      60,
      25,
      10
    ),
    StoreQuantity(
      "A1004",
      "Fresh Orange Juice 1L",
      "S004",
      "Beverages",
      90,
      40,
      18
    ),
    StoreQuantity(
      "A1005",
      "Cheddar Cheese 200g",
      "S005",
      "Dairy",
      70,
      35,
      12
    ),
    StoreQuantity(
      "A1006",
      "Fresh Yogurt 500g",
      "S006",
      "Dairy",
      50,
      20,
      8
    )
  )

  def findAll(): List[StoreQuantity] =
    mockData
}