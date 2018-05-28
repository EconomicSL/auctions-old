package org.economicsl.auctions.multiunit

import org.economicsl.auctions.multiunit.orders.{AskOrder, BidOrder}


trait SinglePricePointDoubleAuction[A <: AskOrder, B <: BidOrder] extends GenDoubleAuction[A, B]
