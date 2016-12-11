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
package org.economicsl.auctions.reverse

import java.util.UUID

import org.economicsl.auctions.Fill
import org.economicsl.auctions.orderbooks.OrderBook
import org.economicsl.auctions.orders._


/** Base trait defining the interface for all `ReverseAuction` instances. */
sealed trait ReverseAuction {

  type B <: LimitBidOrder with Quantity
  type A <: LimitAskOrder with Persistent with Quantity
  type OB <: OrderBook[A, collection.GenIterable[(UUID, A)]]

  def fill(order: B): Option[Fill[A, B]]

  /** Place a `LimitAskOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitAskOrder with Persistent with Quantity` instance to add to the `OrderBook`
    */
  def place(order: A): Unit

}


/** Base trait defining the interface for all `SingleUnitReverseAuction` instances. */
trait SingleUnitReverseAuction extends ReverseAuction {

  type B <: LimitBidOrder with SingleUnit
  type A = LimitAskOrder with Persistent with SingleUnit

}


/** Base trait defining the interface for all `MultiUnitReverseAuction` instances. */
trait MultiUnitReverseAuction extends ReverseAuction {

  type B <: LimitBidOrder with MultiUnit
  type A = LimitAskOrder with Persistent with MultiUnit

}

