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
package org.economicsl.auctions.reverse.singleunit

import java.util.UUID

import org.economicsl.auctions.orderbooks.SortedAskOrderBook
import org.economicsl.auctions.orders.persistent.Persistent
import org.economicsl.auctions.orders.{AskOrder, BidOrder, SingleUnit}
import org.economicsl.auctions.reverse.{DescendingAskOrders, SingleUnitReverseAuction}
import org.economicsl.auctions.{Fill, Price, Quantity, Tradable}


/** Trait defining the interface for a single-unit descending price reverse auction.
  *
  * @tparam B a sub-type of `BidOrder with SingleUnit`.
  * @tparam A a sub-type of `AskOrder with Persistent with SingleUnit`
  */
trait DescendingPriceReverseAuction[B <: BidOrder with SingleUnit, A <: AskOrder with Persistent with SingleUnit]
  extends SingleUnitReverseAuction[B, A] with DescendingAskOrders[B, A]


object DescendingPriceReverseAuction {

  /** Create an instance of a `SingleUnitDescendingPriceReverseAuction`.
    *
    * @param matchingRule rule used to match a `BidOrder` with a `AskOrder` taken from a `SortedAskOrderBook`.
    * @param pricingRule rule used to form the transaction price from the `BidOrder` and the matched `AskOrder`.
    * @param tradable all `AskOrder` instances stored in the `SortedAskOrderBook` should be for the same `Tradable`.
    * @param ordering ordering used to maintain the ordering of the `SortedAskOrderBook`.
    * @tparam B a sub-type of `BidOrder with SingleUnit`.
    * @tparam A a sub-type of `AskOrder with Persistent with SingleUnit`
    */
  def apply[B <: BidOrder with SingleUnit, A <: AskOrder with Persistent with SingleUnit]
           (matchingRule: (B, SortedAskOrderBook[A]) => Option[(UUID, A)],
            pricingRule: (B, A) => Price,
            tradable: Tradable)
           (implicit ordering: Ordering[(UUID, A)])
           : DescendingPriceReverseAuction[B, A] = {
    new DefaultImpl[B, A](matchingRule, pricingRule, tradable)(ordering)
  }


  /** Default implementation of a single-unit descending price reverse auction.
    *
    * @param matchingRule rule used to match a `BidOrder` with a `AskOrder` taken from a `SortedAskOrderBook`.
    * @param pricingRule rule used to form the transaction price from the `BidOrder` and the matched `AskOrder`.
    * @param tradable all `AskOrder` instances stored in the `SortedAskOrderBook` should be for the same `Tradable`.
    * @param ordering ordering used to maintain the ordering of the `SortedAskOrderBook`.
    * @tparam B a sub-type of `BidOrder with SingleUnit`.
    * @tparam A a sub-type of `AskOrder with Persistent with SingleUnit`
    */
  private[this] class DefaultImpl[B <: BidOrder with SingleUnit, A <: AskOrder with Persistent with SingleUnit]
                                 (matchingRule: (B, SortedAskOrderBook[A]) => Option[(UUID, A)],
                                  pricingRule: (B, A) => Price,
                                  val tradable: Tradable)
                                 (implicit ordering: Ordering[(UUID, A)])
    extends DescendingPriceReverseAuction[B, A] {

    def fill(order: B): Option[Fill] = findMatchFor(order, orderBook) map {
      case (_, askOrder) =>
        orderBook = orderBook - (askOrder.issuer, askOrder) // SIDE EFFECT!
      val price = formPriceUsing(order, askOrder)
        Fill(askOrder, order, price, Quantity(1))
    }

    /** Place a `AskOrder with Persistent with SingleUnit` into the `SortedAskOrderBook`.
      *
      * @param order a `AskOrder with Persistent with SingleUnit` instance to add to the `SortedAskOrderBook`
      */
    def place(order: A): Unit = orderBook + (order.issuer, order)

    protected def findMatchFor(order: B, orderBook: OB): Option[(UUID, A)] = matchingRule(order, orderBook)

    protected def formPriceUsing(order: B, matchingOrder: A): Price = pricingRule(order, matchingOrder)

    @volatile protected var orderBook: OB = SortedAskOrderBook[A](tradable)(ordering)

  }
}
