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

import org.economicsl.auctions.{Fill, Price, Tradable}
import org.economicsl.auctions.orderbooks.{SortedAskOrderBook, SortedBidOrderBook}
import org.economicsl.auctions.orders.Persistent

import scala.collection.immutable


class MultiUnitContinuousKDoubleAuction(k: Double, tradable: Tradable) extends MultiUnitContinuousDoubleAuction {

  type AB = SortedAskOrderBook[A with Persistent]
  type BB = SortedBidOrderBook[B with Persistent]

  def fill(order: A): Option[immutable.Queue[Fill[A, B with Persistent]]] = {
    val (matchedOrders, residualOrder, orderBook) = findMatchFor(order)
    residualOrder.foreach { case askOrder: Persistent => place(askOrder) }  // POSSIBLE SIDE EFFECT!
    if (matchedOrders.nonEmpty) {
      bidOrderBook = orderBook // SIDE EFFECT!
      Some(matchedOrders.map { case (askOrder, bidOrder) => Fill(askOrder, bidOrder, getPrice(askOrder, bidOrder)) })
    } else {
      order match {
        case unmatchedOrder: Persistent => place(unmatchedOrder); None
        case _ => None
      }
    }
  }

  def fill(order: B): Option[immutable.Queue[Fill[A with Persistent, B]]] = {
    val (matchedOrders, residualOrder, orderBook) = findMatchFor(order)
    residualOrder.foreach { case bidOrder: Persistent => place(bidOrder) }  // POSSIBLE SIDE EFFECT!
    if (matchedOrders.nonEmpty) {
      askOrderBook = orderBook // SIDE EFFECT!
      Some(matchedOrders.map { case (askOrder, bidOrder) => Fill(askOrder, bidOrder, getPrice(askOrder, bidOrder)) })
    } else {
      order match {
        case unmatchedOrder: Persistent => place(unmatchedOrder); None
        case _ => None
      }
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

  @volatile protected var askOrderBook: AB = SortedAskOrderBook(tradable)

  @volatile protected var bidOrderBook: BB = SortedBidOrderBook(tradable)

  private[this] def getPrice(askOrder: A, bidOrder: B): Price = {
    Price(k * bidOrder.limit.value + (1 - k) * askOrder.limit.value)
  }

}
