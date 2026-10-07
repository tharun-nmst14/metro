package com.metro.ufm.repositories

import com.metro.ufm.models.{StockAssignmentDcDetail, StockAssignmentResult}

trait StockAssignmentRepository {
  def findAll(): Seq[StockAssignmentResult]
  def findDcDetails(): Seq[StockAssignmentDcDetail]
}

final class MockStockAssignmentRepository extends StockAssignmentRepository {
  override def findAll(): Seq[StockAssignmentResult] = Seq(
    StockAssignmentResult(
      "3553831", "355/5/25", "379673", "1", "1", "283614", "10kg ROTKOHL", "DE", "", "1", "", "205.00", "0.00", "0.00", "", "", "0.00", "0.00", "100.00", "", "", ""
    ),
    StockAssignmentResult(
      "3553831", "355/5/25", "379676", "1", "2", "283616", "ROTKOHL", "DE", "", "6", "", "998.00", "0.00", "0.00", "", "", "0.00", "2.00", "75.00", "", "", ""
    )
  )

  override def findDcDetails(): Seq[StockAssignmentDcDetail] = Seq(
    StockAssignmentDcDetail("85590", "MLG MARL", "", "1.00", "205.00", "0.00", "0.00", "0.00", "0.00", "100.00", "8.58")
  )
}
