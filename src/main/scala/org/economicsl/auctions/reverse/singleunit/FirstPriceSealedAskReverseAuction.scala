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
package org.economicsl.auctions.reverse.singleunit

import java.util.UUID

import org.economicsl.auctions.orderbooks.SortedAskOrderBook
import org.economicsl.auctions.orders.{LimitBidOrder, SingleUnit}
import org.economicsl.auctions.reverse.{DescendingAskOrders, SingleUnitReverseAuction}
import org.economicsl.auctions.{Fill, Tradable}


/** Class defining a first-price, sealed-ask, reverse auction mechanism. */
class FirstPriceSealedAskReverseAuction(tradable: Tradable) extends SingleUnitReverseAuction with DescendingAskOrders {

  type B = LimitBidOrder with SingleUnit

  def fill(order: B): Option[Fill[A, B]] = orderBook.headOption match {
    case Some((uuid, askOrder)) if askOrder.limit <= order.limit =>
      orderBook = orderBook - (uuid, askOrder) // SIDE EFFECT!
      Some(Fill(askOrder, order, askOrder.limit))
    case None => None
  }

  /** Place a `LimitAskOrder with Persistent with SingleUnit` into the `SortedAskOrderBook`.
    *
    * @param order a `LimitAskOrder with Persistent with SingleUnit` instance.
    */
  def place(order: A): UUID = {
    val uuid = randomUUID(); orderBook + (uuid, order); uuid
  }

  @volatile protected var orderBook: OB = SortedAskOrderBook[A](tradable)

}
