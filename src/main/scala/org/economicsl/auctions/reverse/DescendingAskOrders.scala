package org.economicsl.auctions.reverse

import org.economicsl.auctions.orderbooks.SortedAskOrderBook
import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, Persistent, Quantity}


trait DescendingAskOrders {
  this: ReverseAuction =>

  type OB = SortedAskOrderBook[A]

  protected def orderBook: OB

}
