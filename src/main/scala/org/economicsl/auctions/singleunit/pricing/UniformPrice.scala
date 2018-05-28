package org.economicsl.auctions.singleunit.pricing

import org.economicsl.auctions.singleunit.AuctionLike


trait UniformPrice {
  this: AuctionLike =>

  protected def pricingRule: UniformPricingRule

}
