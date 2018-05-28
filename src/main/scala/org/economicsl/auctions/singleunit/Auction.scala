package org.economicsl.auctions.singleunit

import org.economicsl.auctions.singleunit.orders.{AskOrder, BidOrder}


/** Base trait defining the interface for all single-unit `Auction` instances. */
trait Auction extends AuctionLike {
  this: QuotePolicy =>

  val askOrdering: Ordering[AskOrder] = AskOrder.naturalOrdering

  val bidOrdering: Ordering[BidOrder] = BidOrder.naturalOrdering

  def clear: Option[Fill]

}
