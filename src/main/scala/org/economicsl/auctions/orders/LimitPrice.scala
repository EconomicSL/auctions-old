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

import org.economicsl.auctions.Price


/** Mixin trait defining a limit price for a `Tradable`. */
trait LimitPrice {
  this: Order =>

  def limit: Price

}


/** Companion object for the `LimitPrice` trait.
  *
  * Defines a basic ordering for all `Order with LimitPrice` instances.
  */
object LimitPrice {

  /** By default, all `Order` instances that mixin `LimitPrice` are ordered by `limit` from lowest to highest.
    *
    * @tparam O the sub-type of `Order with LimitPrice` that is being ordered.
    * @return and `Ordering` defined over `Order with LimitPrice` instances of type `T`.
    * @note if two `Order with LimitPrice` instances have the same `limit` price, then the ordering is based on the
    *       unique `issuer` identifier.
    */
  def ordering[O <: Order with LimitPrice]: Ordering[(UUID, O)] = Ordering.by { case (_, order) => order.limit }

}