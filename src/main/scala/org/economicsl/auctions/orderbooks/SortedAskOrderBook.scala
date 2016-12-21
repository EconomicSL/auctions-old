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
package org.economicsl.auctions.orderbooks

import java.util.UUID

import org.economicsl.auctions.Tradable
import org.economicsl.auctions.orders.AskOrder

import scala.collection.immutable


class SortedAskOrderBook[A <: AskOrder] private (initialOrders: immutable.TreeSet[(UUID, A)], tradable: Tradable)
                                                (implicit ordering: Ordering[(UUID, A)])
  extends OrderBook[A, immutable.TreeSet[(UUID, A)]] {

  def + (uuid: UUID, order: A): SortedAskOrderBook[A] = {
    new SortedAskOrderBook(existingOrders + ((uuid, order)), tradable)(ordering)
  }

  def - (uuid: UUID, order: A): SortedAskOrderBook[A] = {
    new SortedAskOrderBook(existingOrders - ((uuid, order)), tradable)(ordering)
  }

  def headOption: Option[(UUID, A)] = existingOrders.headOption

  protected val existingOrders: immutable.TreeSet[(UUID, A)] = initialOrders

}


object SortedAskOrderBook {

  def apply[A <: AskOrder ](tradable: Tradable)(implicit ordering: Ordering[(UUID, A)]): SortedAskOrderBook[A] = {
    new SortedAskOrderBook[A](immutable.TreeSet.empty[(UUID, A)], tradable)(ordering)
  }

}
