package org.economicsl.auctions.singleunit

import org.economicsl.auctions.singleunit.pricing.UniformPricingRule

import scala.collection.immutable


class PeriodicSealedBidDoubleAuction(pricingRule: UniformPricingRule) extends DoubleAuction {

  def clear(): Option[immutable.Set[Fill]] = pricingRule(orderBook) match {
    case Some(price) => Some(orderBook.matchedOrders.zipped.map { case (askOrder, bidOrder) => Fill(askOrder, bidOrder, price) })
    case None => None
  }

}
