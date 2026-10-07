package com.metro.ufm.repositories

import com.metro.ufm.config.OracleDatabaseSettings
import com.metro.ufm.database.OracleConnectionProvider
import org.scalatest.funsuite.AnyFunSuite

final class OracleSupplierRepositoryIntegrationSpec extends AnyFunSuite {
  test("returns and maps the five seeded Oracle suppliers") {
    val settings = OracleDatabaseSettings.load()
    assume(
      settings.validationErrors.isEmpty,
      "Oracle integration test canceled: configure Oracle JDBC URL, username, and password first."
    )

    val repository = new OracleSupplierRepository(new OracleConnectionProvider(settings))
    val suppliers = repository.findAll()
    val suppliersByNumber = suppliers.map(supplier => supplier.number -> supplier.name).toMap

    assert(suppliers.size == 5)
    assert(suppliersByNumber.get("1049").contains("YEX BV"))
    assert(suppliersByNumber.get("1053").contains("TENFOOD BV"))
    assert(suppliersByNumber.get("1068").contains("PHU IMPORT EXPORT"))
  }
}
