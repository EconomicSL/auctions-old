package org.economicsl.auctions.orderbooks

import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, SingleUnit}

import scala.collection.immutable


case class FourHeapOrderBook private (matchedAskOrders: immutable.TreeSet[LimitAskOrder with SingleUnit],
                                      matchedBidOrders: immutable.TreeSet[LimitBidOrder with SingleUnit],
                                      unMatchedAskOrders: immutable.TreeSet[LimitAskOrder with SingleUnit],
                                      unMatchedBidOrders: immutable.TreeSet[LimitBidOrder with SingleUnit]) {

  require(matchedAskOrders.size == matchedBidOrders.size)

  def - (order: LimitAskOrder with SingleUnit): FourHeapOrderBook = {
    if (unMatchedAskOrders.contains(order)) {
      new FourHeapOrderBook(matchedAskOrders, matchedBidOrders, unMatchedAskOrders - order, unMatchedBidOrders)
    } else {
      val bidOrder = matchedBidOrders.head
      new FourHeapOrderBook(matchedAskOrders - order, matchedBidOrders - bidOrder, unMatchedAskOrders, unMatchedBidOrders + bidOrder)
    }
  }

  def - (order: LimitBidOrder with SingleUnit): FourHeapOrderBook = {
    if (unMatchedBidOrders.contains(order)) {
      new FourHeapOrderBook(matchedAskOrders, matchedBidOrders, unMatchedAskOrders, unMatchedBidOrders - order)
    } else {
      val askOrder = matchedAskOrders.head
      new FourHeapOrderBook(matchedAskOrders - askOrder, matchedBidOrders - order, unMatchedAskOrders + askOrder, unMatchedBidOrders)
    }
  }

  def + (order: LimitAskOrder with SingleUnit): FourHeapOrderBook = {
    (matchedAskOrders.headOption, unMatchedBidOrders.headOption) match {
      case (Some(askOrder), Some(bidOrder)) if order.limit <= bidOrder.limit && askOrder.limit <= bidOrder.limit =>
        new FourHeapOrderBook(matchedAskOrders + order, matchedBidOrders + bidOrder, unMatchedAskOrders, unMatchedBidOrders - bidOrder)
      case (None, Some(bidOrder)) if order.limit <= bidOrder.limit =>
        new FourHeapOrderBook(matchedAskOrders + order, matchedBidOrders + bidOrder, unMatchedAskOrders, unMatchedBidOrders - bidOrder)
      case (Some(askOrder), Some(_)) if order.limit < askOrder.limit =>
        new FourHeapOrderBook(matchedAskOrders - askOrder + order, matchedBidOrders, unMatchedAskOrders + askOrder, unMatchedBidOrders)
      case _ =>
        new FourHeapOrderBook(matchedAskOrders, matchedBidOrders, unMatchedAskOrders + order, unMatchedBidOrders)
    }
  }

  def + (order: LimitBidOrder with SingleUnit): FourHeapOrderBook = {
    (matchedBidOrders.headOption, unMatchedAskOrders.headOption) match {
      case (Some(bidOrder), Some(askOrder)) if order.limit >= askOrder.limit && bidOrder.limit >= askOrder.limit =>
        new FourHeapOrderBook(matchedAskOrders + askOrder, matchedBidOrders + order, unMatchedAskOrders - askOrder, unMatchedBidOrders)
      case (None, Some(askOrder)) if order.limit >= askOrder.limit =>
        new FourHeapOrderBook(matchedAskOrders + askOrder, matchedBidOrders + order, unMatchedAskOrders - askOrder, unMatchedBidOrders)
      case (Some(bidOrder), Some(_)) if order.limit > bidOrder.limit =>
        new FourHeapOrderBook(matchedAskOrders, matchedBidOrders - bidOrder + order, unMatchedAskOrders, unMatchedBidOrders + bidOrder)
      case _ =>
        new FourHeapOrderBook(matchedAskOrders, matchedBidOrders, unMatchedAskOrders, unMatchedBidOrders + order)
    }
  }

}


object FourHeapOrderBook {

  def apply(): FourHeapOrderBook = {

    /* Note that the heap priority is maximal price: highest priced order should be at the top of the heap! */
    val matchedAskOrders = immutable.TreeSet.empty[LimitAskOrder with SingleUnit](LimitAskOrder.priority)

    /* Note that the heap priority is minimal price: lowest priced order should be at the top of the heap! */
    val matchedBidOrders = immutable.TreeSet.empty[LimitBidOrder with SingleUnit](LimitBidOrder.priority)

    val unMatchedAskOrders = immutable.TreeSet.empty[LimitAskOrder with SingleUnit]
    val unMatchedBidOrders = immutable.TreeSet.empty[LimitBidOrder with SingleUnit]

    new FourHeapOrderBook(matchedAskOrders, matchedBidOrders, unMatchedAskOrders, unMatchedBidOrders)
  }

}
