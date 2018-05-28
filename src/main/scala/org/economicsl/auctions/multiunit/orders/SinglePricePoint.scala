package org.economicsl.auctions.multiunit.orders

import org.economicsl.auctions.{GenOrder, Price, PriceQuantitySchedule, Quantity}

import scala.collection.immutable


/** Mixin trait for an `Order` for multiple units of a `Tradable`. */
trait SinglePricePoint extends PriceQuantitySchedule {
  this: GenOrder =>

  def limit: Price

  def quantity: Quantity

  def split(residual: Quantity): (GenOrder with SinglePricePoint, GenOrder with SinglePricePoint)

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
  def ordering[O <: GenOrder with SinglePricePoint]: Ordering[O] = Ordering.by(order => (order.limit, order.issuer))

}
