package org.economicsl.auctions.singleunit

import org.economicsl.auctions.{GenAuction, SingleUnit}
import org.economicsl.auctions.singleunit.orderbooks.FourHeapOrderBook
import org.economicsl.auctions.singleunit.orders.{LimitAskOrder, LimitBidOrder}

/** Base trait defining the interface for all `SingleUnitAuction` instances. */
trait SingleUnitAuction extends GenAuction[LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit] {

  protected def orderBook: FourHeapOrderBook

}
