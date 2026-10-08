package com.metro.ufm.repositories

import java.sql.{ResultSet, SQLException}
import scala.util.Using

import com.metro.ufm.database.OracleConnectionProvider
import com.metro.ufm.models.{StockAssignmentDcDetail, StockAssignmentResult}

 
final class OracleStockAssignmentRepository(
  connectionProvider: OracleConnectionProvider
) extends StockAssignmentRepository {

  override def findAll(): Seq[StockAssignmentResult] =
    query(OracleStockAssignmentRepository.FindAllSql, "Stock Assignment lookup failed.")(readAssignments)

  override def findDcDetails(): Seq[StockAssignmentDcDetail] =
    query(OracleStockAssignmentRepository.FindDcDetailsSql, "Stock Assignment DC lookup failed.")(readDcDetails)

  private def query[A](sql: String, message: String)(read: ResultSet => A): A = {
    try {
      Using.Manager { use =>
        val connection = use(connectionProvider.openConnection())
        val statement = use(connection.prepareStatement(sql))
        val resultSet = use(statement.executeQuery())
        read(resultSet)
      }.get
    } catch {
      case exception: SQLException =>
        throw new StockAssignmentRepositoryException(message, exception)
      case exception: StockAssignmentRepositoryException =>
        throw exception
    }
  }

  private def readAssignments(resultSet: ResultSet): Seq[StockAssignmentResult] = {
    val assignments = Seq.newBuilder[StockAssignmentResult]
    while (resultSet.next()) {
      assignments += StockAssignmentResult(
        sgCnu = value(resultSet, "SG_CNU"),
        merchGroup = value(resultSet, "MERCH_GROUP_CODE"),
        articleNumber = value(resultSet, "ARTICLE_NO"),
        variant = value(resultSet, "VARIANT_NO"),
        bundle = value(resultSet, "BUNDLE_NO"),
        subsystemNumber = value(resultSet, "SUBSYSTEM_NO"),
        description = value(resultSet, "DESCRIPTION"),
        sortText = value(resultSet, "SORT_TEXT"),
        sizeText = value(resultSet, "SIZE_TEXT"),
        cbb = "",
        bc = "",
        salesForecast = value(resultSet, "SALES_FORECAST"),
        storeQuantity = value(resultSet, "STORE_QUANTITY"),
        assignedStockQuantity = value(resultSet, "ASSIGNED_STOCK_QUANTITY"),
        recalc = value(resultSet, "RECALC"),
        keyDistribution = value(resultSet, "KEY_DISTRIBUTION"),
        quantityGap = value(resultSet, "QUANTITY_GAP"),
        remainingQuantity = value(resultSet, "REMAINING_QUANTITY"),
        ufmStock = value(resultSet, "UFM_STOCK"),
        promotion = value(resultSet, "PROMOTION"),
        ibc = value(resultSet, "IBC"),
        articleExchange = value(resultSet, "ARTICLE_EXCHANGE")
      )
    }
    assignments.result()
  }

  private def readDcDetails(resultSet: ResultSet): Seq[StockAssignmentDcDetail] = {
    val details = Seq.newBuilder[StockAssignmentDcDetail]
    while (resultSet.next()) {
      details += StockAssignmentDcDetail(
        dcNumber = value(resultSet, "DC_NO"),
        dc = value(resultSet, "DC_NAME"),
        bc = "",
        orderFactor = "",
        salesForecast = value(resultSet, "SALES_FORECAST"),
        storeQuantity = value(resultSet, "STORE_QUANTITY"),
        assignedStockQuantity = value(resultSet, "ASSIGNED_STOCK_QUANTITY"),
        quantityGap = value(resultSet, "QUANTITY_GAP"),
        remainingStock = value(resultSet, "REMAINING_QUANTITY"),
        ufmStock = value(resultSet, "UFM_STOCK"),
        averageNnbp = ""
      )
    }
    details.result()
  }

  private def value(resultSet: ResultSet, column: String): String =
    Option(resultSet.getString(column)).getOrElse("")
}

object OracleStockAssignmentRepository {
   
  private[repositories] val FindAllSql =
    """SELECT a.SG_CNU,
      |       mg.MERCH_GROUP_CODE,
      |       a.ARTICLE_NO,
      |       COALESCE(sa.VARIANT_NO, a.VARIANT_NO) AS VARIANT_NO,
      |       COALESCE(sa.BUNDLE_NO, a.BUNDLE_NO) AS BUNDLE_NO,
      |       COALESCE(sa.SUBSYSTEM_NO, a.SUBSYSTEM_NO) AS SUBSYSTEM_NO,
      |       a.DESCRIPTION,
      |       a.SORT_TEXT,
      |       a.SIZE_TEXT,
      |       sa.SALES_FORECAST,
      |       sa.STORE_QUANTITY,
      |       sa.ASSIGNED_STOCK_QUANTITY,
      |       sa.RECALC,
      |       sa.KEY_DISTRIBUTION,
      |       sa.QUANTITY_GAP,
      |       sa.REMAINING_QUANTITY,
      |       sa.UFM_STOCK,
      |       sa.PROMOTION,
      |       sa.IBC,
      |       sa.ARTICLE_EXCHANGE
      |FROM STOCK_ASSIGNMENT sa
      |LEFT JOIN ARTICLE a ON a.ARTICLE_ID = sa.ARTICLE_ID
      |LEFT JOIN MERCH_GROUP mg ON mg.MERCH_GROUP_ID = a.MERCH_GROUP_ID
      |ORDER BY a.ARTICLE_NO, sa.ASSIGNMENT_DAY""".stripMargin

  private[repositories] val FindDcDetailsSql =
    """SELECT dc.DC_NO,
      |       dc.DC_NAME,
      |       sa.SALES_FORECAST,
      |       sa.STORE_QUANTITY,
      |       sa.ASSIGNED_STOCK_QUANTITY,
      |       sa.QUANTITY_GAP,
      |       sa.REMAINING_QUANTITY,
      |       sa.UFM_STOCK
      |FROM STOCK_ASSIGNMENT sa
      |LEFT JOIN DISTRIBUTION_CENTER dc
      |       ON dc.DISTRIBUTION_CENTER_ID = sa.DISTRIBUTION_CENTER_ID
      |ORDER BY dc.DC_NO, sa.ASSIGNMENT_DAY""".stripMargin
}

final class StockAssignmentRepositoryException(message: String, cause: Throwable)
    extends RuntimeException(message, cause)
