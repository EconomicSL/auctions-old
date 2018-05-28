package org.economicsl.auctions.singleunit

import org.economicsl.auctions.singleunit.orders.{AskOrder, BidOrder}


/** Base trait for all single-unit double auctions. */
trait DoubleAuction extends AuctionLike {

  val askOrdering: Ordering[AskOrder] = AskOrder.naturalOrdering

  val bidOrdering: Ordering[BidOrder] = BidOrder.naturalOrdering

}
