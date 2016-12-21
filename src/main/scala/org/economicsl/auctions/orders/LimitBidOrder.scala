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
package org.economicsl.auctions.orders

import java.util.UUID

import org.economicsl.auctions.{Price, Quantity, Tradable}


trait LimitBidOrder extends BidOrder {
  this: SinglePricePoint =>
}

object LimitBidOrder {

  /** By default, instances of `LimitBidOrder` are ordered based on `limit` price from highest to lowest */
  implicit def ordering[B <: LimitBidOrder with SinglePricePoint]: Ordering[B] = SinglePricePoint.ordering.reverse

  /** The highest priority `LimitBidOrder` is the one with the highest `limit` price. */
  def priority[B <: LimitBidOrder with SinglePricePoint]: Ordering[B] = SinglePricePoint.ordering

  def apply(issuer: UUID, limit: Price, quantity: Quantity, tradable: Tradable): LimitBidOrder with SinglePricePoint = {
    SinglePricePointImpl(issuer, limit, quantity, tradable)
  }

  def apply(issuer: UUID, limit: Price, tradable: Tradable): LimitBidOrder with SingleUnit = {
    SingleUnitImpl(issuer, limit, tradable)
  }

  private[this] case class SinglePricePointImpl(issuer: UUID, limit: Price, quantity: Quantity, tradable: Tradable)
    extends LimitBidOrder with SinglePricePoint

  private[this] case class SingleUnitImpl(issuer: UUID, limit: Price, tradable: Tradable)
    extends LimitBidOrder with SingleUnit

}