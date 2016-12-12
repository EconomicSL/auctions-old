package org.economicsl.auctions.twosided

import org.economicsl.auctions.{Fill, Price, Tradable}
import org.economicsl.auctions.orderbooks.{SortedAskOrderBook, SortedBidOrderBook}
import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, Persistent, SingleUnit}


trait SingleUnitPeriodicDoubleAuction extends PeriodicDoubleAuction{

  type A = LimitAskOrder with Persistent with SingleUnit
  type B = LimitBidOrder with Persistent with SingleUnit

}



object SingleUnitPeriodicDoubleAuction {

  private[this] class DefaultImpl(tradable: Tradable) extends PeriodicDoubleAuction {

    type AB = SortedAskOrderBook[A]
    type BB = SortedBidOrderBook[B]

    def fill(): Option[Iterable[Fill[A, B]]] = {
      val (matchedOrders, residualOrderBooks) = span(askOrderBook, bidOrderBook)
      (askOrderBook, bidOrderBook) = residualOrderBooks

      val ((_, highestAskOrder), (_, lowestBidOrder)) = matchedOrders.last
      val price = Price(0.5 * (highestAskOrder.limit.value + lowestBidOrder.limit.value))
      matchedOrders.map { case ((_, askOrder), (_, bidOrder)) => new Fill[A, B](askOrder, bidOrder, price) }
      
    }

    def span(askOrderBook: AB, bidOrderBook: BB): (MatchedOrders, ResidualOrderBooks) = {
      val zippedOrders = askOrderBook.zip(bidOrderBook)
      val (matchedOrders, unmatchedOrders) = zippedOrders.span { case (askOrder, bidOrder) => askOrder.limit <= bidOrder.limit }

    }

    /** Place a `LimitAskOrder with Persistent with Quantity` into the `OrderBook`.
      *
      * @param order a `LimitAskOrder with Persistent with Quantity` instance to add to the `OrderBook`
      */
    def place(order: A with Persistent): Unit = {
      askOrderBook = askOrderBook + (order.issuer, order)
    }

    /** Place a `LimitBidOrder with Persistent with Quantity` into the `OrderBook`.
      *
      * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`
      */
    def place(order: B with Persistent): Unit = {
      bidOrderBook = bidOrderBook + (order.issuer, order)
    }

    @volatile protected var askOrderBook: SortedAskOrderBook[A] = SortedAskOrderBook(tradable)

    @volatile protected var bidOrderBook: SortedBidOrderBook[B] = SortedBidOrderBook(tradable)

  }


}
