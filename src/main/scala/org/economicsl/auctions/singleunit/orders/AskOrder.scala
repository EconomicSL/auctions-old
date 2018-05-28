
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
package org.economicsl.auctions.singleunit.orders

import java.util.UUID

import org.economicsl.auctions.{GenAskOrder, Price, Tradable}


sealed trait AskOrder extends GenAskOrder with SingleUnit


object AskOrder {

  /** By default, instances of `SingleUnitAskOrder` are ordered based on `limit` price from lowest to highest. */
  implicit val naturalOrdering: Ordering[AskOrder] = SingleUnit.ordering

}


case class LimitAskOrder(issuer: UUID, limit: Price, tradable: Tradable) extends AskOrder


case class MarketAskOrder(issuer: UUID, tradable: Tradable) extends AskOrder {

  val limit: Price = Price.MinValue

}

