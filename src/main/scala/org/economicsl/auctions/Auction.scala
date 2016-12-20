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

import java.util.UUID

import org.economicsl.auctions.orderbooks.OrderBook
import org.economicsl.auctions.orders._


/** Base trait defining the interface for all `Auction` instances. */
sealed trait Auction[A <: AskOrder with PriceQuantitySchedule, B <: BidOrder with Persistent with PriceQuantitySchedule] {

  type OB <: OrderBook[B, collection.GenIterable[(UUID, B)]]

  def fill(order: A): Option[Fill]

  /** Place a `LimitBidOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`
    */
  def place(order: B): Unit

  protected def findMatchFor(order: A, orderBook: OB): Option[(UUID, B)]

  protected def formPriceUsing(order: A, matchingOrder: B): Price

  /** Underlying `OrderBook` used to store the `LimitBidOrder with Persistent` instances. */
  protected def orderBook: OB

}


/** Base trait defining the interface for all `SingleUnitAuction` instances. */
trait SingleUnitAuction[A <: AskOrder with SingleUnit, B <: BidOrder with Persistent with SingleUnit]
  extends Auction[A, B]


/** Base trait defining the interface for all `MultiUnitAuction` instances. */
trait SinglePricePointAuction[A <: AskOrder with SinglePricePoint, B <: BidOrder with Persistent with SinglePricePoint]
  extends Auction[A, B]
