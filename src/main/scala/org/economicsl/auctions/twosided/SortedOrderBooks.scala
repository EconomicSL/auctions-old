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
package org.economicsl.auctions.twosided

import org.economicsl.auctions.orderbooks.{SortedAskOrderBook, SortedBidOrderBook}
import org.economicsl.auctions.orders.Persistent


/** Mixin trait defining a partial interface `DoubleAuction`. */
trait SortedOrderBooks {
  this: DoubleAuction =>

  type AB = SortedAskOrderBook[A with Persistent]
  type BB = SortedBidOrderBook[B with Persistent]

  protected def askOrderBook: AB

  protected def bidOrderBook: BB


}
