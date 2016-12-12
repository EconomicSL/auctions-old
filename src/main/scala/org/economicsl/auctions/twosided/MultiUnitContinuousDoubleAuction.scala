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

import org.economicsl.auctions.orders.{MultiUnitLimitAskOrder, MultiUnitLimitBidOrder, Persistent}

import scala.annotation.tailrec
import scala.collection.immutable


/** Base trait defining the interface for all multi-unit `ContinuousDoubleAuction` types. */
trait MultiUnitContinuousDoubleAuction extends ContinuousDoubleAuction {

  type A = MultiUnitLimitAskOrder
  type B = MultiUnitLimitBidOrder

  protected def findMatchFor(order: A): (immutable.Queue[(A, B with Persistent)], Option[A], BB) = {

    type MatchedOrders = immutable.Queue[(A, B with Persistent)]

    @tailrec def accumulate(matches: MatchedOrders, order: A, orderBook: BB): (MatchedOrders, Option[A], BB) = {
      orderBook.headOption match {
        case None =>
          (matches, Some(order), orderBook)
        case Some((_, bidOrder)) if bidOrder.limit < order.limit =>
          (matches, Some(order), orderBook)
        case Some((_, bidOrder)) if bidOrder.limit >= order.limit && bidOrder.quantity == order.quantity =>
          (matches.enqueue((order, bidOrder)), None, orderBook)
        case Some((_, bidOrder)) if bidOrder.limit >= order.limit && bidOrder.quantity > order.quantity =>
          val unmatchedQuantity = bidOrder.quantity - order.quantity
          val (matchedBidOrder, unmatchedBidOrder) = bidOrder.split(unmatchedQuantity)
          (matches.enqueue((order, matchedBidOrder)), None, orderBook + unmatchedBidOrder)
        case Some((_, bidOrder)) if bidOrder.limit >= order.limit && bidOrder.quantity < order.quantity =>
          val unmatchedQuantity = order.quantity - bidOrder.quantity
          val (matchedAskOrder, unmatchedAskOrder) = order.split(unmatchedQuantity)
          accumulate(matches.enqueue((matchedAskOrder, bidOrder)), unmatchedAskOrder, orderBook - bidOrder)
      }
    }

    accumulate(immutable.Queue.empty[(A, B with Persistent)], order, bidOrderBook)
  }

  protected def findMatchFor(order: B): (immutable.Queue[(A with Persistent, B)], Option[B], AB) = {

    type MatchedOrders = immutable.Queue[(A with Persistent, B)]

    @tailrec def accumulate(matches: MatchedOrders, order: B, orderBook: AB): (MatchedOrders, Option[B], AB) = {
      orderBook.headOption match {
        case None =>
          (matches, Some(order), orderBook)
        case Some((_, askOrder)) if askOrder.limit > order.limit =>
          (matches, Some(order), orderBook)
        case Some((_, askOrder)) if askOrder.limit <= order.limit && askOrder.quantity == order.quantity =>
          (matches.enqueue((askOrder, order)), None, orderBook)
        case Some((_, askOrder)) if askOrder.limit <= order.limit && askOrder.quantity > order.quantity =>
          val unmatchedQuantity = askOrder.quantity - order.quantity
          val (matchedAskOrder, unmatchedAskOrder) = askOrder.split(unmatchedQuantity)
          (matches.enqueue((matchedAskOrder, order)), None, orderBook + unmatchedAskOrder)
        case Some((_, askOrder)) if askOrder.limit <= order.limit && askOrder.quantity < order.quantity =>
          val unmatchedQuantity = order.quantity - askOrder.quantity
          val (matchedBidOrder, unmatchedBidOrder) = order.split(unmatchedQuantity)
          accumulate(matches.enqueue((askOrder, matchedBidOrder)), unmatchedBidOrder, orderBook - askOrder)
      }
    }

    accumulate(immutable.Queue.empty[(A with Persistent, B)], order, askOrderBook)
  }

}
