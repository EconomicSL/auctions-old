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

import org.economicsl.auctions.orderbooks.SortedBidOrderBook
import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, Persistent, SingleUnit}


class FirstPriceSealedBidAuction(tradable: Tradable)
  extends SingleUnitAscendingPriceAuction[LimitAskOrder with SingleUnit, LimitBidOrder with Persistent with SingleUnit] {

  def fill(order: LimitAskOrder with SingleUnit): Option[Fill[LimitAskOrder with SingleUnit, LimitBidOrder with Persistent with SingleUnit]] = {
    findMatchFor(order, orderBook) map {
      case (_, bidOrder) =>
        orderBook = orderBook - (bidOrder.issuer, bidOrder) // SIDE EFFECT!
        val price = formPriceUsing(order, bidOrder)
        Fill(order, bidOrder, price)
    }
  }

  /** Place a `LimitBidOrder with Persistent with SingleUnit` into the `OrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`
    */
  def place(order: LimitBidOrder with Persistent with SingleUnit): Unit = orderBook - (order.issuer, order)

  protected def findMatchFor(order: LimitAskOrder with SingleUnit, orderBook: OB): Option[(UUID, LimitBidOrder with Persistent with SingleUnit)] = {
    orderBook.headOption filter { case (_, bidOrder) => bidOrder.limit >= order.limit }
  }

  protected def formPriceUsing(order: LimitAskOrder with SingleUnit, matchingOrder: LimitBidOrder with Persistent with SingleUnit): Price = {
    matchingOrder.limit
  }

  @volatile protected var orderBook: OB = SortedBidOrderBook(tradable)

}
