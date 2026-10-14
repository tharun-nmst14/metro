package com.metro.ufm.repositories

import com.metro.ufm.models.{StoreQuantity, StoreQuantityFilter}


class StoreQuantitiesRepository {

    private val mockData: List[StoreQuantity] = List(
         StoreQuantity(articleNo = "A1001", description = "Fresh Milk 1L", subsystemNo = "S001", merchandiseGroup = "Dairy", stockQuantity = 100, salesQuantity = 50, orderQuantity = 20),
            StoreQuantity(articleNo = "A1002", description = "Whole Wheat Bread", subsystemNo = "S002", merchandiseGroup = "Bakery", stockQuantity = 80, salesQuantity = 30, orderQuantity = 15),
            StoreQuantity(articleNo = "A1003", description = "Organic Eggs 12pcs", subsystemNo = "S003", merchandiseGroup = "Dairy", stockQuantity = 60, salesQuantity = 25, orderQuantity = 10),
            StoreQuantity(articleNo = "A1004", description = "Fresh Orange Juice 1L", subsystemNo = "S004", merchandiseGroup = "Beverages", stockQuantity = 90, salesQuantity = 40, orderQuantity = 18),
            StoreQuantity(articleNo = "A1005", description = "Cheddar Cheese 200g", subsystemNo = "S005", merchandiseGroup = "Dairy", stockQuantity = 70, salesQuantity = 35, orderQuantity = 12),
            StoreQuantity(articleNo = "A1006", description = "Fresh Yogurt 500g", subsystemNo = "S006", merchandiseGroup = "Dairy", stockQuantity = 50, salesQuantity = 20, orderQuantity = 8)
         )

         def findAll(): List[StoreQuantity] = mockData
}