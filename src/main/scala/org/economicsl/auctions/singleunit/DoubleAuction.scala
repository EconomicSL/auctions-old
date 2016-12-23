package org.economicsl.auctions.singleunit

import org.economicsl.auctions.GenAskOrder
import org.economicsl.auctions.singleunit.orders.LimitBidOrder

/** Base trait defining the interface for all `SingleUnitDoubleAuction` instances.
  *
  * @tparam A the sub-type of `AskOrder with Quantity` traded via the `SingleUnitDoubleAuction`.
  * @tparam B the sub-type of `BidOrder with Quantity` traded via the `SingleUnitDoubleAuction`.
  * @note A `SingleUnitDoubleAuction` is a composition of a `SingleUnitAuction` and a `SingleUnitReverseAuction`.
  */
trait SingleUnitDoubleAuction[A <: GenAskOrder with SingleUnit, B <: LimitBidOrder with SingleUnit]
  extends DoubleAuction[A, B]
