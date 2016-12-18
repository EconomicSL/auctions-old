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
sealed trait Auction {

  /* Type members will be made progressively tighter in sub-classes... */
  type A <: LimitAskOrder with Quantity
  type B <: LimitBidOrder with Persistent with Quantity
  type OB <: OrderBook[B, collection.GenIterable[(UUID, B)]]

  /** Place a `LimitBidOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`.
    * @return a `UUID` that identifies the `order`.
    */
  def place(order: B): UUID

  /** Underlying `OrderBook` used to store the `LimitBidOrder with Persistent` instances. */
  protected def orderBook: OB

  protected def randomUUID(): UUID = UUID.randomUUID()

}


/** Base trait defining the interface for all `SingleUnitAuction` instances. */
trait SingleUnitAuction extends Auction {

  type A <: LimitAskOrder with SingleUnit
  type B = LimitBidOrder with Persistent with SingleUnit

  def fill(order: A): Option[Fill[A, B]]

}


/** Base trait defining the interface for all `MultiUnitAuction` instances. */
trait MultiUnitAuction extends Auction {

  type A <: LimitAskOrder with MultiUnit
  type B = LimitBidOrder with Persistent with MultiUnit

}
