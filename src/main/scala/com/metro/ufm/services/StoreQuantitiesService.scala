package com.metro.ufm.services
 
import com.metro.ufm.models.{StoreQuantity, StoreQuantityFilter}
import com.metro.ufm.repositories.StoreQuantitiesRepository

class StoreQuantitiesService(
    storeQuantitiesRepository: StoreQuantitiesRepository = new StoreQuantitiesRepository() 
) extends Serializable {

  def search(filter: StoreQuantityFilter): List[StoreQuantity] = {
    storeQuantitiesRepository.findAll()
  }
}