package org.economicsl.auctions

import java.util.UUID


trait UUIDProvider {

  def randomUUID(): UUID = UUID.randomUUID()

}

