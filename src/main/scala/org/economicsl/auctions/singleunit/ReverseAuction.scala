package org.economicsl.auctions.singleunit

import org.economicsl.auctions.singleunit.orders.{LimitAskOrder, LimitBidOrder}

/** Base trait defining the interface for all `SingleUnitReverseAuction` instances. */
trait SingleUnitReverseAuction extends ReverseAuction[LimitBidOrder with SingleUnit, LimitAskOrder with SingleUnit]
