package org.economicsl.auctions.singleunit

import org.economicsl.auctions.Price

sealed trait QuotePolicy {
  this: AuctionLike =>
}


/** Mixin trait used to indicate that an `AuctionLike` does not allow quotes. */
trait SealedOrderBook extends QuotePolicy {
  this: AuctionLike =>
}


trait BidAskQuotePolicy extends QuotePolicy {
  this: DoubleAuction =>

  /** Use a partial function to implement a little query language for price quotes. */
  def receive: PartialFunction[QuoteRequest, Option[PriceQuote]] = {
    case BidQuoteRequest => orderBook.unMatchedOrders.bestAskOrder map (askOrder => BidPriceQuote(askOrder.limit))
    case AskQuoteRequest => AskPriceQuote(???)
    case BidAskQuoteRequest => BidAskQuote(???, ???)
  }

}


sealed trait QuoteRequest

object BidQuoteRequest extends QuoteRequest

object AskQuoteRequest extends QuoteRequest

object BidAskQuoteRequest extends QuoteRequest

sealed trait PriceQuote

case class BidPriceQuote(limit: Price) extends PriceQuote

case class AskPriceQuote(limit: Price) extends PriceQuote

case class BidAskQuote(ask: Price, bid: Price) extends PriceQuote {

  val spread: Price = bid - ask

}