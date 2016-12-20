package org.economicsl.auctions.reverse

import org.economicsl.auctions.orderbooks.SortedAskOrderBook
import org.economicsl.auctions.orders.{AskOrder, BidOrder, Persistent, PriceQuantitySchedule}


trait DescendingAskOrders[B <: BidOrder with PriceQuantitySchedule, A <: AskOrder with Persistent with PriceQuantitySchedule] {
  this: ReverseAuction[B, A] =>

  type OB = SortedAskOrderBook[A]

  protected def orderBook: OB

}
