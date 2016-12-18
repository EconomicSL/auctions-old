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
import org.economicsl.auctions.{Fill, Price, Tradable}


class FirstPriceSealedAskReverseAuction(tradable: Tradable) extends DescendingPriceReverseAuction {

  type B = LimitBidOrder with SingleUnit

  def fill(order: B): Option[Fill] = {
    findMatchFor(order, orderBook) map {
      case (_, askOrder) =>
        orderBook = orderBook - askOrder // SIDE EFFECT!
      val price = formPriceUsing(order, askOrder)
        Fill(askOrder, order, price)
    }
  }

  /** Place a `LimitBidOrder with Persistent with SingleUnit` into the `SortedBidOrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `SortedBidOrderBook`
    */
  def place(order: A): Unit = orderBook - order

  protected def findMatchFor(order: B, orderBook: OB): Option[(UUID, A)] = {
    orderBook.headOption filter { case (_, askOrder) => askOrder.limit >= order.limit }
  }

  protected def formPriceUsing(order: B, matchingOrder: A): Price = {
    matchingOrder.limit
  }

  @volatile protected var orderBook: OB = SortedAskOrderBook[A](tradable)

}
