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


sealed trait LimitBidOrder extends BidOrder with LimitPrice {
  this: Quantity =>

}


trait MultiUnitLimitBidOrder extends LimitBidOrder with MultiUnit {
  type O <: MultiUnitLimitBidOrder
}


object LimitBidOrder {

  /** By default, instances of `LimitBidOrder` are ordered based on `limit` price from highest to lowest */
  implicit def ordering[B <: LimitBidOrder]: Ordering[(UUID, B)] = LimitPrice.ordering.reverse

  /** The highest priority `LimitBidOrder` is the one with the highest `limit` price. */
  def priority[B <: LimitBidOrder]: Ordering[(UUID, B)] = LimitPrice.ordering

}