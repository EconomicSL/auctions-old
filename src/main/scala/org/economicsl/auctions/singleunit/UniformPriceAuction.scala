package org.economicsl.auctions.singleunit

import org.economicsl.auctions.Tradable
import org.economicsl.auctions.singleunit.pricing.{UniformPrice, UniformPricingRule}

import scala.collection.immutable


abstract class UniformPriceAuction(protected val pricingRule: UniformPricingRule, val tradable: Tradable)
  extends Auction with UniformPrice {

  def clear: Option[Fill] = {
    pricingRule(orderBook) map (p => orderBook.matchedOrders.zipped map { case (ask, bid) => Fill(ask, bid, p) })
  }

}
