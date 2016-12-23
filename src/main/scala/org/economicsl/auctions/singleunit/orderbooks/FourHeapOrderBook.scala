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
package org.economicsl.auctions.singleunit.orderbooks

import org.economicsl.auctions.singleunit.orders.{AskOrder, BidOrder}


case class FourHeapOrderBook private (matchedOrders: MatchedOrders, unMatchedOrders: UnMatchedOrders) {

  def - (order: AskOrder): FourHeapOrderBook = {
    if (unMatchedOrders.contains(order)) {
      new FourHeapOrderBook(matchedOrders, unMatchedOrders - order)
    } else {
      val bidOrder = matchedOrders.bidOrders.head
      new FourHeapOrderBook(matchedOrders - (order, bidOrder), unMatchedOrders + bidOrder)
    }
  }

  def - (order: BidOrder): FourHeapOrderBook = {
    if (unMatchedOrders.contains(order)) {
      new FourHeapOrderBook(matchedOrders, unMatchedOrders - order)
    } else {
      val askOrder = matchedOrders.askOrders.head
      new FourHeapOrderBook(matchedOrders - (askOrder, order), unMatchedOrders + askOrder)
    }
  }

  def + (order: AskOrder): FourHeapOrderBook = {
    (matchedOrders.askOrders.headOption, unMatchedOrders.bidOrders.headOption) match {
      case (Some(askOrder), Some(bidOrder)) if order.limit <= bidOrder.limit && askOrder.limit <= bidOrder.limit =>
        new FourHeapOrderBook(matchedOrders + (order, bidOrder), unMatchedOrders - bidOrder)
      case (None, Some(bidOrder)) if order.limit <= bidOrder.limit =>
        new FourHeapOrderBook(matchedOrders + (order, bidOrder), unMatchedOrders - bidOrder)
      case (Some(askOrder), Some(_)) if order.limit < askOrder.limit =>
        new FourHeapOrderBook(matchedOrders.replace(askOrder, order), unMatchedOrders + askOrder)
      case _ =>
        new FourHeapOrderBook(matchedOrders, unMatchedOrders + order)
    }
  }

  def + (order: BidOrder): FourHeapOrderBook = {
    (matchedOrders.bidOrders.headOption, unMatchedOrders.askOrders.headOption) match {
      case (Some(bidOrder), Some(askOrder)) if order.limit >= askOrder.limit && bidOrder.limit >= askOrder.limit =>
        new FourHeapOrderBook(matchedOrders + (askOrder, order), unMatchedOrders - askOrder)
      case (None, Some(askOrder)) if order.limit >= askOrder.limit =>
        new FourHeapOrderBook(matchedOrders + (askOrder, order), unMatchedOrders - askOrder)
      case (Some(bidOrder), Some(_)) if order.limit > bidOrder.limit =>
        new FourHeapOrderBook(matchedOrders.replace(bidOrder, order), unMatchedOrders + bidOrder)
      case _ =>
        new FourHeapOrderBook(matchedOrders, unMatchedOrders + order)
    }
  }

}


object FourHeapOrderBook {

  def apply(askOrdering: Ordering[AskOrder], bidOrdering: Ordering[BidOrder]): FourHeapOrderBook = {
    val matchedOrders = MatchedOrders(askOrdering.reverse, bidOrdering.reverse)
    val unMatchedOrders = UnMatchedOrders(askOrdering, bidOrdering)
    new FourHeapOrderBook(matchedOrders, unMatchedOrders)
  }

}
