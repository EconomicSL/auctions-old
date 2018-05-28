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
package org.economicsl

import scala.language.implicitConversions


package object auctions {

  /** Base trait used to differentiate tradable goods from non-tradable goods. */
  trait Tradable

  /** Value class representing a numeric price. */
  case class Price(value: Double) extends AnyVal


  /** Companion object for the `Price` value class. */
  object Price {

    /** Default ordering for `Price` instances is low to high based on the underlying value. */
    implicit val ordering: Ordering[Price] = PriceOrdering

    implicit def mkOrderingOps(lhs: Price): PriceOrdering.Ops = PriceOrdering.mkOrderingOps(lhs)

    val MaxValue = Price(Double.PositiveInfinity)

    val MinValue = Price(0.0)

  }


  /** Default ordering for `Price` instances is low to high based on the underlying value. */
  object PriceOrdering extends Ordering[Price] {

    /** Instances of `Price` are compared using their underlying values.
      *
      * @param p1 some `Price` instance.
      * @param p2 another `Price` instance.
      * @return -1 if `p1` is less than `p2`, 0 if `p1` equals `p2`, 1 otherwise.
      */
    def compare(p1: Price, p2: Price): Int = p1.value compare p2.value

  }

  case class Quantity(value: Double) extends AnyVal

  object Quantity {

    implicit def mkOrderingOps(lhs: Quantity): QuantityOrdering.Ops = QuantityOrdering.mkOrderingOps(lhs)

  }


  /** Default ordering for `Quantity` instances is low to high based on the underlying value. */
  object QuantityOrdering extends Ordering[Quantity] {

    /** Instances of `Quantity` are compared using their underlying values.
      *
      * @param q1 some `Quantity` instance.
      * @param q2 another `Quantity` instance.
      * @return -1 if `q1` is less than `q2`, 0 if `q1` equals `q2`, 1 otherwise.
      */
    def compare(q1: Quantity, q2: Quantity): Int = q1.value compare q2.value

  }

}
