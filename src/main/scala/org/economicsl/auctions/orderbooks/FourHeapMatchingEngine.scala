import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, SingleUnit, persistent}

import scala.collection.immutable

class FourHeapMatchingEngine {

  type AskOrder = persistent.LimitAskOrder with SingleUnit
  type BidOrder = persistent.LimitBidOrder with SingleUnit

  def get(order: AskOrder): Unit = {
    if (unMatchedAskOrders.contains(order)) {
      unMatchedAskOrders -= order
    } else {
      matchedAskOrders -= order

      val bidOrder = matchedBidOrders.head
      matchedBidOrders -= bidOrder
      unMatchedBidOrders += bidOrder
      assert(matchedAskOrders.size == matchedBidOrders.size)
    }
  }

  def get(order: BidOrder): Unit = {
    if (unMatchedBidOrders.contains(order)) {
      unMatchedBidOrders -= order
    } else {
      matchedBidOrders -= order

      val askOrder = matchedAskOrders.head
      matchedAskOrders -= askOrder
      unMatchedAskOrders += askOrder
      assert(matchedAskOrders.size == matchedBidOrders.size)
    }
  }

  def put(order: AskOrder): Unit = (matchedAskOrders.headOption, unMatchedBidOrders.headOption) match {
    case (Some(askOrder), Some(bidOrder)) if order.limit <= bidOrder.limit && askOrder.limit <= bidOrder.limit =>
      matchedAskOrders += order
      unMatchedBidOrders -= bidOrder
      matchedBidOrders += bidOrder
      assert(matchedAskOrders.size == matchedBidOrders.size)  // todo this should really be a check on quantity!
      assert(unMatchedBidOrders.headOption.forall( o => matchedBidOrders.head.limit >= o.limit ))
    case (None, Some(bidOrder)) if order.limit <= bidOrder.limit =>
      matchedAskOrders += order
      unMatchedBidOrders -= bidOrder
      matchedBidOrders += bidOrder
      assert(matchedAskOrders.size == matchedBidOrders.size)
      assert(unMatchedBidOrders.headOption.forall( o => matchedBidOrders.head.limit >= o.limit ))
    case (Some(askOrder), Some(_)) if order.limit < askOrder.limit =>
      matchedAskOrders -= askOrder
      unMatchedAskOrders += askOrder
      matchedAskOrders += order
      assert(matchedAskOrders.size == matchedBidOrders.size)
      assert(matchedAskOrders.head.limit <= unMatchedAskOrders.head.limit)
    case _ => unMatchedAskOrders += order
  }

  def put(order: BidOrder): Unit = (matchedBidOrders.headOption, unMatchedAskOrders.headOption) match {
    case (Some(bidOrder), Some(askOrder)) if order.limit >= askOrder.limit && bidOrder.limit >= askOrder.limit =>
      matchedBidOrders = matchedBidOrders + order
      unMatchedAskOrders = unMatchedAskOrders - askOrder
      matchedAskOrders = matchedAskOrders + askOrder
      assert(matchedAskOrders.size == matchedBidOrders.size)
      assert(unMatchedAskOrders.headOption.forall( o => matchedAskOrders.head.limit >= o.limit ))
    case (None, Some(askOrder)) if order.limit >= askOrder.limit =>
      matchedBidOrders = matchedBidOrders + order
      unMatchedAskOrders = unMatchedAskOrders - askOrder
      matchedAskOrders = matchedAskOrders + askOrder
      assert(matchedAskOrders.size == matchedBidOrders.size)
      assert(unMatchedAskOrders.headOption.forall( o => matchedAskOrders.head.limit >= o.limit ))
    case (Some(bidOrder), Some(_)) if order.limit > bidOrder.limit =>
      matchedBidOrders = matchedBidOrders - bidOrder
      unMatchedBidOrders = unMatchedBidOrders + bidOrder
      matchedBidOrders = matchedBidOrders + order
      assert(matchedAskOrders.size == matchedBidOrders.size)
      assert(matchedBidOrders.head.limit >= unMatchedBidOrders.head.limit)
    case _ =>
      unMatchedBidOrders = unMatchedBidOrders + order
      assert(matchedBidOrders.head.limit >= unMatchedBidOrders.head.limit)
  }

  /* Note that the heap priority is maximal price: highest priced order should be at the top of the heap! */
  @volatile var matchedAskOrders = immutable.TreeSet.empty[AskOrder](LimitAskOrder.priority)

  /* Note that the heap priority is minimal price: lowest priced order should be at the top of the heap! */
  @volatile var matchedBidOrders = immutable.TreeSet.empty[BidOrder](LimitBidOrder.priority)

  /* Note that the heap priority is minimal price: lowest priced order should be at the top of the heap! */
  @volatile var unMatchedAskOrders = immutable.TreeSet.empty[AskOrder]

  /* Note that the heap priority is maximal price: highest priced order should be at the top of the heap! */
  @volatile var unMatchedBidOrders = immutable.TreeSet.empty[BidOrder]

}
