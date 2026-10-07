package com.metro.ufm.services

import com.metro.ufm.models.Supplier
import com.metro.ufm.repositories.SupplierRepository

final class SupplierService(repository: SupplierRepository) {
  def search(number: String, name: String): Seq[Supplier] = {
    val numberQuery = normalize(number)
    val nameQuery = normalize(name).replace("%", "")

    repository.findAll().filter { supplier =>
      (numberQuery.isEmpty || supplier.number.toLowerCase.contains(numberQuery)) &&
        (nameQuery.isEmpty || supplier.name.toLowerCase.contains(nameQuery))
    }
  }

  private def normalize(value: String): String = Option(value).getOrElse("").trim.toLowerCase
}
