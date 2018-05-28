package org.economicsl.auctions.multiunit

import org.economicsl.auctions.GenAuctionLike
import org.economicsl.auctions.multiunit.orders.{AskOrder, BidOrder}


/** Base trait defining the interface for all `SinglePricePointAuction` instances. */
trait SinglePricePointAuction extends GenAuctionLike[AskOrder, BidOrder]
