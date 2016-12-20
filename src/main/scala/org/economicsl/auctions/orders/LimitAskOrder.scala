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
package org.economicsl.auctions.orders


trait LimitAskOrder extends AskOrder {
  this: SinglePricePoint =>
}


object LimitAskOrder {

  /** By default, instances of `LimitAskOrder` are ordered based on `limit` price from lowest to highest. */
  implicit def ordering[A <: LimitAskOrder with SinglePricePoint]: Ordering[A] = SinglePricePoint.ordering

  /** The highest priority `LimitAskOrder` is the one with the lowest `limit` price. */
  def priority[A <: LimitAskOrder with SinglePricePoint]: Ordering[A] = SinglePricePoint.ordering.reverse


}