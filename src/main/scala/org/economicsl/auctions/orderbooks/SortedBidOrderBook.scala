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
import org.economicsl.auctions.orders.{BidOrder, Persistent}

import scala.collection.immutable


class SortedBidOrderBook[B <: BidOrder with Persistent] private (initialOrders: immutable.TreeSet[(UUID, B)],
                                                                 tradable: Tradable)
                                                                (implicit ordering: Ordering[(UUID, B)])
  extends OrderBook[B, immutable.TreeSet[(UUID, B)]] {

  def + (issuer: UUID, order: B): SortedBidOrderBook[B] = {
    new SortedBidOrderBook(existingOrders + ((issuer, order)), tradable)(ordering)
  }

  def - (issuer: UUID, order: B): SortedBidOrderBook[B] = {
    new SortedBidOrderBook(existingOrders - ((issuer, order)), tradable)(ordering)
  }

  protected val existingOrders: immutable.TreeSet[(UUID, B)] = initialOrders

}


object SortedBidOrderBook {

  def apply[B <: BidOrder with Persistent]
           (tradable: Tradable)
           (implicit ordering: Ordering[(UUID, B)])
           : SortedBidOrderBook[B] = {
    new SortedBidOrderBook[B](immutable.TreeSet.empty[(UUID, B)], tradable)(ordering)
  }

}
