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

import org.economicsl.auctions.orders._


sealed trait Fill[+A <: AskOrder with PriceQuantitySchedule, +B <: BidOrder with PriceQuantitySchedule ] {

  def askOrder: A

  def bidOrder: B

  def price: Price

  def quantity: Quantity

}


case class SingleUnitFill(askOrder: LimitAskOrder with SingleUnit, bidOrder: LimitBidOrder with SingleUnit, price: Price)
  extends Fill[LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit] {

  val quantity: Quantity = Quantity(1.0)

}


