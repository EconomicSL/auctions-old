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

import java.util.UUID

import org.economicsl.auctions.orderbooks.OrderBook
import org.economicsl.auctions.orders._
import org.economicsl.auctions.Fill


/** Base trait defining the interface for all `DoubleAuction` instances. */
sealed trait DoubleAuction {

  /* Type members will be made progressively tighter in sub-classes... */
  type A <: LimitAskOrder with Quantity
  type AB <: OrderBook[A with Persistent, collection.GenIterable[(UUID, A with Persistent)]]
  type B <: LimitBidOrder with Quantity
  type BB <: OrderBook[B with Persistent, collection.GenIterable[(UUID, B with Persistent)]]

  /** Place a `LimitAskOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitAskOrder with Persistent with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `reverseAuction`.
    */
  def place(order: A with Persistent): Unit

  /** Place a `LimitBidOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `auction`.
    */
  def place(order: B with Persistent): Unit

  protected def askOrderBook: AB

  protected def bidOrderBook: BB

}


/** Base trait defining the interface for all `ContinuousDoubleAuction` types. */
trait ContinuousDoubleAuction extends DoubleAuction {

  def fill(order: A): Option[Fill[A, B with Persistent]]

  def fill(order: B): Option[Fill[A with Persistent, B]]

}


/** Base trait defining the interface for all `PeriodicDoubleAuction` types. */
trait PeriodicDoubleAuction extends DoubleAuction {
  /* Type members will be made progressively tighter in sub-classes... */
  type A <: LimitAskOrder with Persistent with Quantity
  type B <: LimitBidOrder with Persistent with Quantity

  def fill(): Option[Iterable[Fill[A, B]]]

}
