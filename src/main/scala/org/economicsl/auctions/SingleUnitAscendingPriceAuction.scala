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
package org.economicsl.auctions

import java.util.UUID

import org.economicsl.auctions.orderbooks.SortedBidOrderBook
import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, Persistent, SingleUnit}


/** Class defining a single-unit ascending price auction.
  *
  * @tparam A a sub-type of `LimitAskOrder with SingleUnit`
  * @tparam B a sub-type of `LimitBidOrder with Persistent with SingleUnit`.
  */
trait SingleUnitAscendingPriceAuction[A <: LimitAskOrder with SingleUnit, B <: LimitBidOrder with Persistent with SingleUnit]
  extends SingleUnitAuction[A, B] with AscendingBidOrders[A, B]


object SingleUnitAscendingPriceAuction {

  /** Create an instance of a `SingleUnitAscendingPriceAuction`.
    *
    * @param matchingRule rule used to match a `LimitAskOrder` with a `LimitBidOrder` taken from a `SortedBidOrderBook`.
    * @param pricingRule rule used to form the transaction price from the `LimitAskOrder` and the matched `LimitBidOrder`.
    * @param tradable all `LimitBidOrder` instances stored in the `SortedOrderBook` should be for the same `Tradable`.
    * @param ordering ordering used to maintain the ordering of the `SortedBidOrderBook`.
    * @tparam A a sub-type of `LimitAskOrder with SingleUnit`
    * @tparam B a sub-type of `LimitBidOrder with Persistent with SingleUnit`.
    */
  def apply[A <: LimitAskOrder with SingleUnit, B <: LimitBidOrder with Persistent with SingleUnit]
           (matchingRule: (A, SortedBidOrderBook[B]) => Option[(UUID, B)],
            pricingRule: (A, B) => Price,
            tradable: Tradable)
           (implicit ordering: Ordering[(UUID, B)])
           : SingleUnitAscendingPriceAuction[A, B] = {
    new DefaultImpl[A, B](matchingRule, pricingRule, tradable)(ordering)
  }


  /** Private implementation of a single-unit ascending price auction.
    *
    * @param matchingRule rule used to match a `LimitAskOrder` with a `LimitBidOrder` taken from a `SortedBidOrderBook`.
    * @param pricingRule rule used to form the transaction price from the `LimitAskOrder` and the matched `LimitBidOrder`.
    * @param tradable all `LimitBidOrder` instances stored in the `SortedOrderBook` should be for the same `Tradable`.
    * @param ordering ordering used to maintain the ordering of the `SortedBidOrderBook`.
    * @tparam A a sub-type of `LimitAskOrder with SingleUnit`
    * @tparam B a sub-type of `LimitBidOrder with Persistent with SingleUnit`.
    */
  private[this] class DefaultImpl[A <: LimitAskOrder with SingleUnit, B <: LimitBidOrder with Persistent with SingleUnit]
                                 (matchingRule: (A, SortedBidOrderBook[B]) => Option[(UUID, B)],
                                  pricingRule: (A, B) => Price,
                                  val tradable: Tradable)
                                 (implicit ordering: Ordering[(UUID, B)])
    extends SingleUnitAscendingPriceAuction[A, B] {

    def fill(order: A): Option[Fill] = findMatchFor(order, orderBook) map {
      case (_, bidOrder) =>
        orderBook = orderBook - (bidOrder.issuer, bidOrder) // SIDE EFFECT!
        val price = formPriceUsing(order, bidOrder)
        Fill(order, bidOrder, price)
    }

    /** Place a `LimitBidOrder with Persistent with Quantity` into the `OrderBook`.
      *
      * @param order a `LimitBidOrder with Persistent with Quantity` instance to add to the `OrderBook`
      */
    def place(order: B): Unit = orderBook + (order.issuer, order)

    protected def findMatchFor(order: A, orderBook: OB): Option[(UUID, B)] = matchingRule(order, orderBook)

    protected def formPriceUsing(order: A, matchingOrder: B): Price = pricingRule(order, matchingOrder)

    @volatile protected var orderBook: OB = SortedBidOrderBook[B](tradable)(ordering)

  }

}
