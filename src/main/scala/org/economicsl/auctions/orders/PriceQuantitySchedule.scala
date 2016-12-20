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

import org.economicsl.auctions.{Price, Quantity}

import scala.collection.immutable


/** Mixin trait for an `Order`  `Tradable`.*/
sealed trait PriceQuantitySchedule {
  this: Order =>

  type PricePoint = (Price, Quantity)

  /** A schedule is a step-wise specification of an `Order` to buy (or sell) various quantities of a `Tradable` at
    * specific, discrete price-points.
    */
  def schedule: collection.GenIterable[PricePoint]

}


/** Mixin trait for an `Order` for multiple units of a `Tradable`. */
trait SinglePricePoint extends PriceQuantitySchedule {
  this: Order =>

  def limit: Price

  def quantity: Quantity

  val schedule: immutable.Map[Price, Quantity] = immutable.Map(limit -> quantity)

}

/** Companion object for the `SinglePricePoint` trait.
  *
  * Defines a basic ordering for anything that mixes in the `SinglePricePoint` trait.
  */
object SinglePricePoint {

  /** By default, all `Order` instances that mixin `SinglePricePoint` are ordered by `limit` from lowest to highest.
    *
    * @tparam O the sub-type of `Order with SinglePricePoint` that is being ordered.
    * @return and `Ordering` defined over `Order with SinglePricePoint` instances of type `T`.
    * @note if two `Order with SinglePricePoint` instances have the same `limit` price, then the ordering is based on
    *       the unique `issuer` identifier.
    */
  def ordering[O <: Order with SinglePricePoint]: Ordering[O] = Ordering.by(order => order.limit)

}

/** Mixin trait for an `Order` for a single unit of a `Tradable`. */
trait SingleUnit extends SinglePricePoint {
  this: Order =>

  val quantity = Quantity(1)

}
