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

import org.economicsl.auctions.orders.{LimitAskOrder, LimitBidOrder, Quantity}


case class Fill(askOrder: LimitAskOrder with Quantity, bidOrder: LimitBidOrder with Quantity, price: Price) {

  val quantity: Long = math.min(askOrder.quantity, bidOrder.quantity)

}

