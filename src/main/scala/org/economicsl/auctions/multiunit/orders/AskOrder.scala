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


trait AskOrder extends orders.AskOrder with orders.SinglePricePoint


object AskOrder {

  /** By default, instances of `LimitAskOrder` are ordered based on `limit` price from lowest to highest. */
  implicit def ordering[A <: AskOrder]: Ordering[A] = orders.SinglePricePoint.ordering

}


case class LimitAskOrder(issuer: UUID, limit: Price, quantity: Quantity, tradable: Tradable) extends AskOrder {

  def split(residual: Quantity): (LimitAskOrder, LimitAskOrder) = {
    val matched = quantity - residual
    (this.copy(quantity = matched), this.copy(quantity = residual))
  }

}


case class MarketAskOrder(issuer: UUID, quantity: Quantity, tradable: Tradable) extends AskOrder {

  def split (residual: Quantity): (MarketAskOrder, MarketAskOrder) = {
    val matched = quantity - residual
    (this.copy (quantity = matched), this.copy (quantity = residual) )
  }

}

