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

import org.economicsl.auctions.orderbooks.singleunit.FourHeapOrderBook
import org.economicsl.auctions.orders.{LimitBidOrder, SingleUnit}
import org.economicsl.auctions.pricing.MPlusOnePriceRule
import org.economicsl.auctions.{SingleUnitAuction, SingleUnitFill, Tradable}

import scala.collection.immutable


class SecondPriceSealedBidAuction(tradable: Tradable) extends SingleUnitAuction {

  def cancel(order: LimitBidOrder with SingleUnit): Unit = { orderBook -= order }

  def clear(): Option[immutable.Set[SingleUnitFill]] = MPlusOnePriceRule(orderBook) match {
    case Some(price) =>
      val fills = orderBook.matchedOrders map { case (askOrder, bidOrder) => SingleUnitFill(askOrder, bidOrder, price) }
      orderBook = FourHeapOrderBook()  // ???
      Some(fills)
    case None => None
  }

  def place(order: LimitBidOrder with SingleUnit): Unit = { orderBook += order }

  @volatile protected var orderBook = FourHeapOrderBook()

}
