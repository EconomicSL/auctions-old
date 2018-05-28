package org.economicsl.auctions.singleunit

import org.economicsl.auctions.{GenAuctionLike, Tradable}
import org.economicsl.auctions.singleunit.orderbooks.FourHeapOrderBook
import org.economicsl.auctions.singleunit.orders.{AskOrder, BidOrder}



trait AuctionLike extends GenAuctionLike[AskOrder, BidOrder] {

  def askOrdering: Ordering[AskOrder]

  def bidOrdering: Ordering[BidOrder]

  def tradable: Tradable

  /** Remove an `AskOrder` from the `OrderBook`.
    *
    * @param order an `AskOrder` instance to remove from the `OrderBook`
    */
  def cancel(order: AskOrder): Unit = {
    orderBook -= order
  }

  /** Remove a `BidOrder` from the `OrderBook`.
    *
    * @param order a `BidOrder` instance to remove from the `OrderBook`
    */
  def cancel(order: BidOrder): Unit = {
    orderBook -= order
  }

  /** Place a `AskOrder` into the `OrderBook`.
    *
    * @param order a `AskOrder` instance to add to the `OrderBook`
    */
  def place(order: AskOrder): Unit = {
    require(order.tradable == tradable)
    orderBook += order
  }

  /** Place a `BidOrder` into the `OrderBook`.
    *
    * @param order a `BidOrder` instance to add to the `OrderBook`
    */
  def place(order: BidOrder): Unit = {
    require(order.tradable == tradable)
    orderBook += order
  }

  @volatile protected var orderBook: FourHeapOrderBook = FourHeapOrderBook.empty(askOrdering, bidOrdering, tradable)

}
