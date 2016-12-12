package org.economicsl.auctions.twosided

import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, MultiUnit, Persistent}


trait MultiUnitPeriodicDoubleAuction extends PeriodicDoubleAuction {

  type A = LimitAskOrder with Persistent with MultiUnit
  type B = LimitBidOrder with Persistent with MultiUnit

}
