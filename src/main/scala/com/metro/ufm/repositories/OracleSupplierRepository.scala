package com.metro.ufm.repositories

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import scala.util.Using

import com.metro.ufm.database.OracleConnectionProvider
import com.metro.ufm.models.Supplier

final class OracleSupplierRepository(
  connectionProvider: OracleConnectionProvider
) extends SupplierRepository {

  override def findAll(): Seq[Supplier] = {
    try {
      Using.Manager { use =>
        val connection = use(connectionProvider.openConnection())
        val statement = use(connection.prepareStatement(OracleSupplierRepository.FindAllSql))
        val resultSet = use(statement.executeQuery())

        collectSuppliers(resultSet)
      }.get
    } catch {
      case exception: SQLException =>
        throw new SupplierRepositoryException(
          "Supplier lookup failed while reading the Oracle SUPPLIER table.",
          exception
        )
      case exception: SupplierRepositoryException =>
        throw exception
    }
  }

  private def collectSuppliers(resultSet: ResultSet): Seq[Supplier] = {
    val suppliers = Seq.newBuilder[Supplier]
    while (resultSet.next()) {
      suppliers += Supplier(
        number = resultSet.getString("SUPPLIER_NO"),
        name = resultSet.getString("SUPPLIER_NAME")
      )
    }
    suppliers.result()
  }
}

object OracleSupplierRepository {
  private[repositories] val FindAllSql =
    "SELECT SUPPLIER_NO, SUPPLIER_NAME FROM SUPPLIER ORDER BY SUPPLIER_NO"
}

final class SupplierRepositoryException(message: String, cause: Throwable)
    extends RuntimeException(message, cause)
