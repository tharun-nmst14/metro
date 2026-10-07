package com.metro.ufm.services

import com.metro.ufm.models.DsdOrderArticle
import com.metro.ufm.repositories.DsdOrderArticleRepository

final class DsdOrderArticleService(repository: DsdOrderArticleRepository) {
  def search(
      orderList: String,
      orderNumber: String,
      articleNumber: String,
      subsystemNumber: String,
      description: String,
      supplierNumber: String,
      supplierName: String
  ): Seq[DsdOrderArticle] = {
    val criteria = Seq(orderList, orderNumber, articleNumber, subsystemNumber, description, supplierNumber, supplierName).map(normalize)
    repository.findAll().filter { article =>
      val values = Seq(
        article.orderList,
        article.orderNumber,
        article.articleNumber,
        article.subsystemNumber,
        article.description,
        article.supplierNumber,
        article.supplierName
      ).map(normalize)

      values.zip(criteria).forall { case (value, criterion) =>
        criterion.isEmpty || value.contains(criterion)
      }
    }
  }

  private def normalize(value: String): String = Option(value).getOrElse("").trim.toLowerCase
}
