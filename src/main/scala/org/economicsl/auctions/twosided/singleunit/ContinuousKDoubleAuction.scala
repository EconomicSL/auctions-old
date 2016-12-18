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
package org.economicsl.auctions.twosided.singleunit

import org.economicsl.auctions.orderbooks.{SortedAskOrderBook, SortedBidOrderBook}
import org.economicsl.auctions.orders.Persistent
import org.economicsl.auctions.{Fill, Price, Tradable}


class ContinuousKDoubleAuction(k: Double, val tradable: Tradable) extends ContinuousDoubleAuction {

  type AB = SortedAskOrderBook[A with Persistent]
  type BB = SortedBidOrderBook[B with Persistent]

  def fill(order: A): Option[Fill] = findMatchFor(order) match {
    case Some((askOrder, bidOrder, orderBook)) =>
      bidOrderBook = orderBook  // SIDE EFFECT!
      val price = Price(k * bidOrder.limit.value + (1 - k) * askOrder.limit.value)
      Some(Fill(askOrder, bidOrder, price))
    case None => order match {
      case unmatchedOrder: Persistent => place(unmatchedOrder); None
      case _ => None
    }
  }

  def fill(order: B): Option[Fill] = findMatchFor(order) match {
    case Some((askOrder, bidOrder, orderBook)) =>
      askOrderBook = orderBook  // SIDE EFFECT!
      val price = Price(k * bidOrder.limit.value + (1 - k) * askOrder.limit.value)
      Some(Fill(askOrder, bidOrder, price))
    case None => order match {
      case unmatchedOrder: Persistent => place(unmatchedOrder); None
      case _ => None
    }
  }

  /** Place a `LimitAskOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitAskOrder with Persistent with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `reverseAuction`.
    */
  def place(order: A with Persistent): Unit = {
    askOrderBook = askOrderBook + order
  }

  /** Place a `LimitBidOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `auction`.
    */
  def place(order: B with Persistent): Unit = {
    bidOrderBook = bidOrderBook + order
  }

  protected def findMatchFor(order: A): Option[(A, B with Persistent, BB)] = bidOrderBook.headOption match {
    case Some((_, bidOrder)) if order.limit <= bidOrder.limit => Some(order, bidOrder, bidOrderBook - bidOrder)
    case _ => None
  }

  protected def findMatchFor(order: B): Option[(A with Persistent, B, AB)] = askOrderBook.headOption match {
    case Some((_, askOrder)) if order.limit >= askOrder.limit => Some(askOrder, order, askOrderBook - askOrder)
    case _ => None
  }

  @volatile protected var askOrderBook: AB = SortedAskOrderBook(tradable)

  @volatile protected var bidOrderBook: BB = SortedBidOrderBook(tradable)

}
