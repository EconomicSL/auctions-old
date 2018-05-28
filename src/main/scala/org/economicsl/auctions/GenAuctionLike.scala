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


/** Base trait defining the interface for all `Auction` instances. */
trait GenAuctionLike[A <: GenAskOrder with PriceQuantitySchedule, B <: GenBidOrder with PriceQuantitySchedule] {

  /** Remove a `GenAskOrder with PriceQuantitySchedule` from the `OrderBook`.
    *
    * @param order a `GenAskOrder with PriceQuantitySchedule` instance to remove from the `OrderBook`
    */
  def cancel(order: A): Unit

  /** Remove a `GenBidOrder with PriceQuantitySchedule` from the `OrderBook`.
    *
    * @param order a `GenBidOrder with PriceQuantitySchedule` instance to remove from the `OrderBook`
    */
  def cancel(order: B): Unit

  /** Place a `GenAskOrder with PriceQuantitySchedule` into the `OrderBook`.
    *
    * @param order a `GenAskOrder with PriceQuantitySchedule` instance to add to the `OrderBook`
    */
  def place(order: A): Unit

  /** Place a `GenBidOrder with PriceQuantitySchedule` into the `OrderBook`.
    *
    * @param order a `GenBidOrder with PriceQuantitySchedule` instance to add to the `OrderBook`
    */
  def place(order: B): Unit

}
