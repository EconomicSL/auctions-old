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
package org.economicsl.auctions.orderbooks.singleunit

import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, SingleUnit}

import scala.collection.immutable


class MatchedOrders private (val askOrders: immutable.TreeSet[LimitAskOrder with SingleUnit],
                             val bidOrders: immutable.TreeSet[LimitBidOrder with SingleUnit]) {

  require(askOrders.size == bidOrders.size)

  def + (order: LimitAskOrder with SingleUnit): MatchedOrders = new MatchedOrders(askOrders + order, bidOrders)

  def + (order: LimitBidOrder with SingleUnit): MatchedOrders = new MatchedOrders(askOrders, bidOrders + order)

  def + (orders: (LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit)): MatchedOrders = {
    new MatchedOrders(askOrders + orders._1, bidOrders + orders._2)
  }

  def - (order: LimitAskOrder with SingleUnit): MatchedOrders = new MatchedOrders(askOrders - order, bidOrders)

  def - (order: LimitBidOrder with SingleUnit): MatchedOrders = new MatchedOrders(askOrders, bidOrders - order)

  def - (orders: (LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit)): MatchedOrders = {
    new MatchedOrders(askOrders - orders._1, bidOrders - orders._2)
  }

  def contains(order: LimitAskOrder with SingleUnit): Boolean = askOrders.contains(order)

  def contains(order: LimitBidOrder with SingleUnit): Boolean = bidOrders.contains(order)

  def swap(existing: LimitAskOrder with SingleUnit, incoming: LimitAskOrder with SingleUnit): MatchedOrders = {
    new MatchedOrders(askOrders - existing + incoming, bidOrders)
  }

  def swap(existing: LimitBidOrder with SingleUnit, incoming: LimitBidOrder with SingleUnit): MatchedOrders = {
    new MatchedOrders(askOrders, bidOrders - existing + incoming)
  }

  def map[B](f: ((LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit)) => B): immutable.Set[B] = {
    askOrders.zip(bidOrders).map(f)
  }

}


object MatchedOrders {

  /** Create an instance of `MatchedOrders`.
    *
    * @return
    * @note the heap used to store store the `LimitAskOrder with SingleUnit` instances is ordered from high to low
    *       based on `limit` price; the heap used to store store the `LimitBidOrder with SingleUnit` instances is
    *       ordered from low to high based on `limit` price.
    */
  def apply(): MatchedOrders = {

    val askOrders = immutable.TreeSet.empty[LimitAskOrder with SingleUnit](LimitAskOrder.priority)
    val bidOrders = immutable.TreeSet.empty[LimitBidOrder with SingleUnit](LimitBidOrder.priority)

    new MatchedOrders(askOrders, bidOrders)

  }
}