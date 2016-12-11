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
import org.economicsl.auctions.reverse.{MultiUnitReverseAuction, ReverseAuction, SingleUnitReverseAuction}
import org.economicsl.auctions.{Auction, MultiUnitAuction, SingleUnitAuction}


/** Base trait defining the interface for all `DoubleAuction` instances.
  *
  * @note A `DoubleAuction` is a composition of a `Auction` and a `ReverseAuction`.
  */
sealed trait DoubleAuction {

  type X <: LimitAskOrder with Quantity
  type Y <: LimitBidOrder with Quantity

  /** Place a `LimitAskOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitAskOrder with Persistent with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `reverseAuction`.
    */
  def place(order: X with Persistent): Unit = reverseAuction.place(order)

  /** Place a `LimitBidOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `auction`.
    */
  def place(order: Y with Persistent): Unit = auction.place(order)

  /** The underlying `Auction` mechanism used to fill `LimitAskOrder` instances. */
  protected def auction: Auction { type A = X; type B = Y with Persistent }

  /** The underlying `ReverseAuction` mechanism used to fill `LimitBidOrder` instances. */
  protected def reverseAuction: ReverseAuction { type B = Y; type A = X with Persistent }

}


/** Base trait defining the interface for all `SingleUnitDoubleAuction` instances.
  *
  * @note A `SingleUnitDoubleAuction` is a composition of a `SingleUnitAuction` and a `SingleUnitReverseAuction`.
  */
trait SingleUnitDoubleAuction extends DoubleAuction {

  /** The underlying `SingleAuction` mechanism used to fill `LimitAskOrder` instances. */
  protected def auction: SingleUnitAuction { type A = X; type B = Y with Persistent }

  /** The underlying `SingleUnitReverseAuction` mechanism used to fill `LimitBidOrder` instances. */
  protected def reverseAuction: SingleUnitReverseAuction { type B = Y; type A = X with Persistent }

}


/** Base trait defining the interface for all `MultiUnitDoubleAuction` instances.
  *
  * @note A `MultiUnitDoubleAuction` is a composition of a `MultiUnitAuction` and a `MultiUnitReverseAuction`.
  */
trait MultiUnitDoubleAuction extends DoubleAuction {

  /** The underlying `MultiUnitAuction` mechanism used to fill `LimitAskOrder` instances. */
  protected def auction: MultiUnitAuction { type A = X; type B = Y with Persistent }

  /** The underlying `MultiUnitReverseAuction` mechanism used to fill `LimitBidOrder` instances. */
  protected def reverseAuction: MultiUnitReverseAuction { type B = Y; type A = X with Persistent }

}
