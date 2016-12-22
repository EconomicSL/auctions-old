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
package org.economicsl.auctions.twosided

import org.economicsl.auctions.orders._
import org.economicsl.auctions.reverse.{ReverseAuction, SinglePricePointReverseAuction, SingleUnitReverseAuction}
import org.economicsl.auctions.{Auction, SinglePricePointAuction, SingleUnitAuction}


/** Base trait defining the interface for all `DoubleAuction` instances.
  *
  * @tparam A the sub-type of `AskOrder with PriceQuantitySchedule` traded via the `DoubleAuction`.
  * @tparam B the sub-type of `BidOrder with PriceQuantitySchedule` traded via the `DoubleAuction`.
  */
sealed trait DoubleAuction[A <: AskOrder with PriceQuantitySchedule, B <: BidOrder with PriceQuantitySchedule] {

  /** Place a `AskOrder with Quantity` into the `OrderBook`.
    *
    * @param order a `AskOrder  with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `reverseAuction`.
    */
  def place(order: A ): Unit

  /** Place a `BidOrder  with Quantity` into the `OrderBook`.
    *
    * @param order a `BidOrder  with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `auction`.
    */
  def place(order: B ): Unit

}


/** Base trait defining the interface for all `SingleUnitDoubleAuction` instances.
  *
  * @tparam A the sub-type of `AskOrder with Quantity` traded via the `SingleUnitDoubleAuction`.
  * @tparam B the sub-type of `BidOrder with Quantity` traded via the `SingleUnitDoubleAuction`.
  * @note A `SingleUnitDoubleAuction` is a composition of a `SingleUnitAuction` and a `SingleUnitReverseAuction`.
  */
trait SingleUnitDoubleAuction[A <: AskOrder with SingleUnit, B <: LimitBidOrder with SingleUnit]
  extends DoubleAuction[A, B]


/** Base trait defining the interface for all `MultiUnitDoubleAuction` instances.
  *
  * @tparam A the sub-type of `AskOrder with Quantity` traded via the `MultiUnitDoubleAuction`.
  * @tparam B the sub-type of `BidOrder with Quantity` traded via the `MultiUnitDoubleAuction`.
  * @note A `MultiUnitDoubleAuction` is a composition of a `SinglePricePointAuction` and a `SinglePricePointReverseAuction`.
  */
trait SinglePricePointDoubleAuction[A <: AskOrder with SinglePricePoint, B <: BidOrder with SinglePricePoint]
  extends DoubleAuction[A, B]
