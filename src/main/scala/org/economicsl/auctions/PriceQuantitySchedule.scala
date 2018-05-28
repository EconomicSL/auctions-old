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

import scala.collection.immutable


/** Mixin trait for an `Order`  `Tradable`.*/
trait PriceQuantitySchedule {
  this: GenOrder =>

  type PricePoint = (Price, Quantity)

  /** A schedule is a step-wise specification of an `Order` to buy (or sell) various quantities of a `Tradable` at
    * specific, discrete price-points.
    */
  def schedule: collection.GenIterable[PricePoint]

}


