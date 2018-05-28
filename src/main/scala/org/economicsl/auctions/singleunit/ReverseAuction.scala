package org.economicsl.auctions.singleunit

import org.economicsl.auctions.singleunit.orders.{AskOrder, BidOrder}


/** Base trait defining the interface for all single-unit `ReverseAuction` instances. */
trait ReverseAuction extends AuctionLike {

  val askOrdering: Ordering[AskOrder] = AskOrder.naturalOrdering.reverse

  val bidOrdering: Ordering[BidOrder] = BidOrder.naturalOrdering.reverse

}
