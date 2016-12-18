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

import java.util.UUID

import org.economicsl.auctions.orderbooks.{SortedAskOrderBook, SortedBidOrderBook}
import org.economicsl.auctions.orders.Persistent
import org.economicsl.auctions.{Fill, Price, Tradable, UUIDProvider}


class ContinuousKDoubleAuction(k: Double, tradable: Tradable) extends ContinuousDoubleAuction with UUIDProvider {

  type AB = SortedAskOrderBook[A with Persistent]
  type BB = SortedBidOrderBook[B with Persistent]

  def fill(order: A): Either[Option[UUID], Fill[A, B with Persistent]] = findMatchFor(order) match {
    case Some((askOrder, bidOrder, orderBook)) =>
      bidOrderBook = orderBook  // SIDE EFFECT!
      val price = Price(k * bidOrder.limit.value + (1 - k) * askOrder.limit.value)
      Right(Fill(askOrder, bidOrder, price))
    case None => order match {
      case unmatchedOrder: Persistent => Left(Some(place(unmatchedOrder)))
      case _ => Left(None)
    }
  }

  def fill(order: B): Either[Option[UUID], Fill[A with Persistent, B]] = findMatchFor(order) match {
    case Some((askOrder, bidOrder, orderBook)) =>
      askOrderBook = orderBook  // SIDE EFFECT!
      val price = Price(k * bidOrder.limit.value + (1 - k) * askOrder.limit.value)
      Right(Fill(askOrder, bidOrder, price))
    case None => order match {
      case unmatchedOrder: Persistent => Left(Some(place(unmatchedOrder)))
      case _ => Left(None)
    }
  }

  /** Place a `LimitAskOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitAskOrder with Persistent with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `reverseAuction`.
    */
  def place(order: A with Persistent): UUID = {
    val uuid = randomUUID(); askOrderBook = askOrderBook + (uuid, order); uuid
  }

  /** Place a `LimitBidOrder with Persistent with Quantity` into the `OrderBook`.
    *
    * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`
    * @note default implementation simply forwards the `order` to the `place` method of the `auction`.
    */
  def place(order: B with Persistent): UUID = {
    val uuid = randomUUID(); bidOrderBook = bidOrderBook + (uuid, order); uuid
  }

  protected def findMatchFor(order: A): Option[(A, B with Persistent, BB)] = bidOrderBook.headOption match {
    case Some((uuid, bidOrder)) if order.limit <= bidOrder.limit => Some(order, bidOrder, bidOrderBook - (uuid, bidOrder))
    case _ => None
  }

  protected def findMatchFor(order: B): Option[(A with Persistent, B, AB)] = askOrderBook.headOption match {
    case Some((uuid, askOrder)) if order.limit >= askOrder.limit => Some(askOrder, order, askOrderBook - (uuid, askOrder))
    case _ => None
  }

  @volatile protected var askOrderBook: AB = SortedAskOrderBook(tradable)

  @volatile protected var bidOrderBook: BB = SortedBidOrderBook(tradable)

}
