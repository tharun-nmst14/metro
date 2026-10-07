package com.metro.ufm.services

import com.metro.ufm.models.{StockAssignmentDcDetail, StockAssignmentResult}
import com.metro.ufm.repositories.StockAssignmentRepository

final class StockAssignmentService(repository: StockAssignmentRepository) {
  def findDcDetails(): Seq[StockAssignmentDcDetail] = repository.findDcDetails()

  def search(
      sgNumber: String,
      articleNumber: String,
      subsystemNumber: String,
      merchGroup: String,
      articleDescription: String
  ): Seq[StockAssignmentResult] = {
    val criteria = Seq(sgNumber, articleNumber, subsystemNumber, merchGroup, articleDescription).map(normalize)
    repository.findAll().filter { result =>
      Seq(
        result.sgCnu,
        result.articleNumber,
        result.subsystemNumber,
        result.merchGroup,
        result.description
      ).map(normalize).zip(criteria).forall { case (value, criterion) =>
        criterion.isEmpty || value.contains(criterion)
      }
    }
  }

  private def normalize(value: String): String = Option(value).getOrElse("").trim.toLowerCase
}
