package org.economicsl.auctions.orderbooks

import java.util.UUID

import org.economicsl.auctions.SingleUnit
import org.economicsl.auctions.singleunit.orders.LimitAskOrder

import scala.collection.immutable


class FourHeapOrderBookSpec {

  def randomUUID(): UUID = UUID.randomUUID()

  val askOrders: immutable.Set[LimitAskOrder with SingleUnit] = {
    immutable.Set(LimitAskOrder(randomUUID(), )
  }

}
