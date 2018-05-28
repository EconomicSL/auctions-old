package org.economicsl.auctions.multiunit

import org.economicsl.auctions.multiunit.orders.{AskOrder, BidOrder}


/** Base trait defining the interface for all `SinglePricePointReverseAuction` instances. */
trait SinglePricePointReverseAuction extends GenReverseAuction[BidOrder, AskOrder]
