package org.economicsl.auctions.reverse

import org.economicsl.auctions.orderbooks.SortedAskOrderBook
import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, Persistent, Quantity}


trait DescendingAskOrders[B <: LimitBidOrder with Quantity, A <: LimitAskOrder with Persistent with Quantity] {
  this: ReverseAuction[B, A] =>

  type OB = SortedAskOrderBook[A]

  protected def orderBook: OB

}
