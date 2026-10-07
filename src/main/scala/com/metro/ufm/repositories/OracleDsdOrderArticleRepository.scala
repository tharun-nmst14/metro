package com.metro.ufm.repositories

import java.sql.{ResultSet, SQLException}
import scala.util.Using

import com.metro.ufm.database.OracleConnectionProvider
import com.metro.ufm.models.DsdOrderArticle

/** Reference-only Oracle implementation. It is not selected by application wiring. */
final class OracleDsdOrderArticleRepository(
  connectionProvider: OracleConnectionProvider
) extends DsdOrderArticleRepository {

  override def findAll(): Seq[DsdOrderArticle] = {
    try {
      Using.Manager { use =>
        val connection = use(connectionProvider.openConnection())
        val statement = use(connection.prepareStatement(OracleDsdOrderArticleRepository.FindAllSql))
        val resultSet = use(statement.executeQuery())
        readArticles(resultSet)
      }.get
    } catch {
      case exception: SQLException =>
        throw new DsdOrderArticleRepositoryException(
          "DSD order article lookup failed against the proposed Oracle schema.",
          exception
        )
      case exception: DsdOrderArticleRepositoryException =>
        throw exception
    }
  }

  private def readArticles(resultSet: ResultSet): Seq[DsdOrderArticle] = {
    val articles = Seq.newBuilder[DsdOrderArticle]
    while (resultSet.next()) {
      articles += DsdOrderArticle(
        orderList = value(resultSet, "ORDER_LIST_CODE"),
        orderNumber = value(resultSet, "ORDER_NO"),
        articleNumber = value(resultSet, "ARTICLE_NO"),
        subsystemNumber = value(resultSet, "SUBSYSTEM_NO"),
        description = value(resultSet, "DESCRIPTION"),
        sgCnu = value(resultSet, "SG_CNU"),
        variantNumber = value(resultSet, "VARIANT_NO"),
        bundleNumber = value(resultSet, "BUNDLE_NO"),
        sortText = value(resultSet, "SORT_TEXT"),
        sizeText = value(resultSet, "SIZE_TEXT"),
        cbb = "",
        moq = "",
        orderQuantity = value(resultSet, "ORDER_QUANTITY"),
        price = value(resultSet, "PRICE"),
        mrp = value(resultSet, "MRP"),
        promotion = value(resultSet, "PROMOTION"),
        stock = "",
        supplierNumber = value(resultSet, "SUPPLIER_NO"),
        supplierName = value(resultSet, "SUPPLIER_NAME")
      )
    }
    articles.result()
  }

  private def value(resultSet: ResultSet, column: String): String =
    Option(resultSet.getString(column)).getOrElse("")
}

object OracleDsdOrderArticleRepository {
  // CBB, MOQ, stock and the business meaning of the displayed order context
  // are not represented by confirmed columns in the current proposed schema.
  private[repositories] val FindAllSql =
    """SELECT oh.ORDER_LIST_CODE,
      |       oh.ORDER_NO,
      |       a.ARTICLE_NO,
      |       COALESCE(ol.SUBSYSTEM_NO, a.SUBSYSTEM_NO) AS SUBSYSTEM_NO,
      |       a.DESCRIPTION,
      |       a.SG_CNU,
      |       COALESCE(ol.VARIANT_NO, a.VARIANT_NO) AS VARIANT_NO,
      |       COALESCE(ol.BUNDLE_NO, a.BUNDLE_NO) AS BUNDLE_NO,
      |       a.SORT_TEXT,
      |       a.SIZE_TEXT,
      |       ol.ORDER_QUANTITY,
      |       ol.PRICE,
      |       ol.MRP,
      |       ol.PROMOTION,
      |       s.SUPPLIER_NO,
      |       s.SUPPLIER_NAME
      |FROM ORDER_LINE ol
      |JOIN ORDER_HEADER oh ON oh.ORDER_HEADER_ID = ol.ORDER_HEADER_ID
      |LEFT JOIN ARTICLE a ON a.ARTICLE_ID = ol.ARTICLE_ID
      |LEFT JOIN SUPPLIER s ON s.SUPPLIER_ID = oh.SUPPLIER_ID
      |ORDER BY oh.ORDER_NO, a.ARTICLE_NO""".stripMargin
}

final class DsdOrderArticleRepositoryException(message: String, cause: Throwable)
    extends RuntimeException(message, cause)
