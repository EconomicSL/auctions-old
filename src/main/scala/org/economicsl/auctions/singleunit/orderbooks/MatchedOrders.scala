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
package org.economicsl.auctions.singleunit.orderbooks

import org.economicsl.auctions.singleunit.orders.{AskOrder, BidOrder}

import scala.collection.immutable


class MatchedOrders private (val askOrders: immutable.TreeSet[AskOrder], val bidOrders: immutable.TreeSet[BidOrder]) {

  require(askOrders.size == bidOrders.size)

  def + (orders: (AskOrder, BidOrder)): MatchedOrders = {
    new MatchedOrders(askOrders + orders._1, bidOrders + orders._2)
  }

  def - (orders: (AskOrder, BidOrder)): MatchedOrders = {
    new MatchedOrders(askOrders - orders._1, bidOrders - orders._2)
  }

  def contains(order: AskOrder): Boolean = askOrders.contains(order)

  def contains(order: BidOrder): Boolean = bidOrders.contains(order)

  def replace(existing: AskOrder, incoming: AskOrder): MatchedOrders = {
    new MatchedOrders(askOrders - existing + incoming, bidOrders)
  }

  def replace(existing: BidOrder, incoming: BidOrder): MatchedOrders = {
    new MatchedOrders(askOrders, bidOrders - existing + incoming)
  }

}


object MatchedOrders {

  /** Create an instance of `MatchedOrders`.
    *
    * @param askOrdering
    * @param bidOrdering
    * @return
    * @note the heap used to store store the `AskOrder` instances is ordered from high to low
    *       based on `limit` price; the heap used to store store the `BidOrder` instances is
    *       ordered from low to high based on `limit` price.
    */
  def apply(askOrdering: Ordering[AskOrder], bidOrdering: Ordering[BidOrder]): MatchedOrders = {

    new MatchedOrders(immutable.TreeSet.empty[AskOrder](askOrdering), immutable.TreeSet.empty[BidOrder](bidOrdering))

  }
}