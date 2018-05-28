package org.economicsl.auctions.singleunit.orders

import org.economicsl.auctions.{GenOrder, Price, PriceQuantitySchedule, Quantity}

import scala.collection.immutable


/** Mixin trait for an `Order` for a single unit of a `Tradable`. */
trait SingleUnit extends PriceQuantitySchedule {
  this: GenOrder =>

  def limit: Price

  val quantity = Quantity(1)

  val schedule: immutable.Map[Price, Quantity] = immutable.Map(limit -> quantity)

}


/** Companion object for the `SingleUnit` trait.
  *
  * Defines a basic ordering for anything that mixes in the `SingleUnit` trait.
  */
object SingleUnit {

  /** By default, all `Order` instances that mixin `SingleUnit` are ordered by `limit` from lowest to highest.
    *
    * @tparam O the sub-type of `Order with SingleUnit` that is being ordered.
    * @return and `Ordering` defined over `Order with SingleUnit` instances of type `T`.
    * @note if two `Order with SingleUnit` instances have the same `limit` price, then the ordering is based on the
    *       unique `issuer` identifier.
    */
  def ordering[O <: GenOrder with SingleUnit]: Ordering[O] = Ordering.by(order => (order.limit, order.issuer))

}
