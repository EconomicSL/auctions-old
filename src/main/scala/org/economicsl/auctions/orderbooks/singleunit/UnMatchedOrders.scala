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


class UnMatchedOrders private(val askOrders: immutable.TreeSet[LimitAskOrder with SingleUnit],
                              val bidOrders: immutable.TreeSet[LimitBidOrder with SingleUnit]) {

  def + (order: LimitAskOrder with SingleUnit): UnMatchedOrders = new UnMatchedOrders(askOrders + order, bidOrders)

  def + (order: LimitBidOrder with SingleUnit): UnMatchedOrders = new UnMatchedOrders(askOrders, bidOrders + order)

  def + (orders: (LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit)): UnMatchedOrders = {
    new UnMatchedOrders(askOrders + orders._1, bidOrders + orders._2)
  }

  def - (order: LimitAskOrder with SingleUnit): UnMatchedOrders = new UnMatchedOrders(askOrders - order, bidOrders)

  def - (order: LimitBidOrder with SingleUnit): UnMatchedOrders = new UnMatchedOrders(askOrders, bidOrders - order)

  def - (orders: (LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit)): UnMatchedOrders = {
    new UnMatchedOrders(askOrders - orders._1, bidOrders - orders._2)
  }

  def contains(order: LimitAskOrder with SingleUnit): Boolean = askOrders.contains(order)

  def contains(order: LimitBidOrder with SingleUnit): Boolean = bidOrders.contains(order)

}


object UnMatchedOrders {

  /** Create an instance of `UnMatchedOrders`.
    *
    * @return
    * @note the heap used to store store the `LimitAskOrder with SingleUnit` instances is ordered from low to high
    *       based on `limit` price; the heap used to store store the `LimitBidOrder with SingleUnit` instances is
    *       ordered from high to low based on `limit` price.
    */
  def apply(): UnMatchedOrders = {

    val askOrders = immutable.TreeSet.empty[LimitAskOrder with SingleUnit]
    val bidOrders = immutable.TreeSet.empty[LimitBidOrder with SingleUnit]

    new UnMatchedOrders(askOrders, bidOrders)

  }

}