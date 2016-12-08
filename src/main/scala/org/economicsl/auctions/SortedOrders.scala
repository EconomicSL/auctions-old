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

import org.economicsl.auctions.orderbooks.{SortedAskOrderBook, SortedBidOrderBook}
import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, Persistent, Quantity}


trait AscendingBidOrders[A <: LimitAskOrder with Quantity, B <: LimitBidOrder with Persistent with Quantity] {
  this: Auction[A, B] =>

  type OB = SortedBidOrderBook[B]

  protected def orderBook: OB

}


trait DescendingAskOrders[B <: LimitBidOrder with Quantity, A <: LimitAskOrder with Persistent with Quantity] {
  this: ReverseAuction[B, A] =>

  type OB = SortedAskOrderBook[A]

  protected def orderBook: OB

}