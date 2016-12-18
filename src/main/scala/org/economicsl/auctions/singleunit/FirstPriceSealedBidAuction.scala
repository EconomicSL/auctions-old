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

import org.economicsl.auctions.orderbooks.SortedBidOrderBook
import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, Persistent, SingleUnit}
import org.economicsl.auctions.{Fill, Price, Tradable}


class FirstPriceSealedBidAuction(tradable: Tradable)
  extends AscendingPriceAuction[LimitAskOrder with SingleUnit, LimitBidOrder with Persistent with SingleUnit] {

  type A = LimitAskOrder with SingleUnit
  type B = LimitBidOrder with Persistent with SingleUnit

  def fill(order: A): Option[Fill] = {
    findMatchFor(order, orderBook) map {
      case (_, bidOrder) =>
        orderBook = orderBook - (bidOrder.issuer, bidOrder) // SIDE EFFECT!
        val price = formPriceUsing(order, bidOrder)
        Fill(order, bidOrder, price)
    }
  }

  /** Place a `LimitBidOrder with Persistent with SingleUnit` into the `SortedBidOrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `SortedBidOrderBook`
    */
  def place(order: B): Unit = orderBook - (order.issuer, order)

  protected def findMatchFor(order: A, orderBook: OB): Option[(UUID, B)] = {
    orderBook.headOption filter { case (_, bidOrder) => bidOrder.limit >= order.limit }
  }

  protected def formPriceUsing(order: A, matchingOrder: B): Price = {
    matchingOrder.limit
  }

  @volatile protected var orderBook: OB = SortedBidOrderBook[B](tradable)

}
