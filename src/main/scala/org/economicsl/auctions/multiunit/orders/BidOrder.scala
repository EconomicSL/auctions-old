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
package org.economicsl.auctions.orders.multiunit

import java.util.UUID

import org.economicsl.auctions.{Price, Quantity, Tradable, orders}


trait BidOrder extends orders.BidOrder with orders.SinglePricePoint


object BidOrder {

  /** By default, instances of `LimitBidOrder` are ordered based on `limit` price from lowest to highest. */
  implicit def ordering[B <: BidOrder]: Ordering[B] = orders.SinglePricePoint.ordering.reverse

}


case class LimitBidOrder(issuer: UUID, limit: Price, quantity: Quantity, tradable: Tradable) extends BidOrder {

  def split(residual: Quantity): (LimitBidOrder, LimitBidOrder) = {
    val matched = quantity - residual
    (this.copy(quantity = matched), this.copy(quantity = residual))
  }

}


case class MarketBidOrder(issuer: UUID, quantity: Quantity, tradable: Tradable) extends BidOrder {

  val limit: Price = Price.MaxValue

  def split (residual: Quantity): (MarketBidOrder, MarketBidOrder) = {
    val matched = quantity - residual
    (this.copy (quantity = matched), this.copy (quantity = residual) )
  }

}

