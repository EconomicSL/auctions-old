/*
Copyright 2016 EconomicSL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.economicsl.auctions

import org.economicsl.auctions.orders._
import org.economicsl.auctions.singleunit.orders.{LimitAskOrder, LimitBidOrder}


/** Base trait defining the interface for all `ReverseAuction` instances. */
sealed trait ReverseAuction[B <: BidOrder with PriceQuantitySchedule, A <: AskOrder with PriceQuantitySchedule] {

  def cancel(order: A): Unit

  def clear(): Option[collection.GenIterable[Fill[A, B]]]

  /** Place a `LimitAskOrder  with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitAskOrder  with Quantity` instance to add to the `OrderBook`
    */
  def place(order: A): Unit

}


/** Base trait defining the interface for all `SingleUnitReverseAuction` instances. */
trait SingleUnitReverseAuction extends ReverseAuction[LimitBidOrder with SingleUnit, LimitAskOrder with SingleUnit]


/** Base trait defining the interface for all `SinglePricePointReverseAuction` instances. */
trait SinglePricePointReverseAuction extends ReverseAuction[LimitBidOrder with SinglePricePoint, LimitAskOrder with SinglePricePoint]

