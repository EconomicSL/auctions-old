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
package org.economicsl.auctions.singleunit

import java.util.UUID

import org.economicsl.auctions.orders.{LimitAskOrder, SingleUnit}
import org.economicsl.auctions._
import org.economicsl.auctions.orderbooks.SortedBidOrderBook


/** Class defining a first-price, sealed-bid auction mechanism. */
class FirstPriceSealedBidAuction(tradable: Tradable) extends SingleUnitAuction with AscendingBidOrders {

  type A = LimitAskOrder with SingleUnit

  def fill(order: A): Option[Fill[A, B]] = orderBook.headOption match {
    case Some((uuid, bidOrder)) if bidOrder.limit >= order.limit =>
      orderBook = orderBook - (uuid, bidOrder)
      Some(Fill(order, bidOrder, bidOrder.limit))
    case None => None
  }

  /** Place a `LimitBidOrder with Persistent with SingleUnit` into the `SortedBidOrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with SingleUnit` instance.
    * @return a `UUID` that identifies the `order`.
    */
  def place(order: B): UUID = {
    val uuid = randomUUID(); orderBook + (uuid, order); uuid
  }

  @volatile protected var orderBook: OB = SortedBidOrderBook[B](tradable)

}
