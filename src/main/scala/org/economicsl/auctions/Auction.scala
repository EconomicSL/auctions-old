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
sealed trait Auction[A <: LimitAskOrder with Quantity, B <: LimitBidOrder with Persistent with Quantity] {

  type OB <: OrderBook[B, collection.GenIterable[(UUID, B)]]

  def fill(order: A): Option[Fill]

  def place(order: B): Unit

  protected def orderBook: OB

}


trait SingleUnitAuction[A <: LimitAskOrder with SingleUnit, B <: LimitBidOrder with Persistent with SingleUnit]
  extends Auction[A, B]


trait MultiUnitAuction[A <: LimitAskOrder with MultiUnit, B <: LimitBidOrder with Persistent with MultiUnit]
  extends Auction[A, B]
