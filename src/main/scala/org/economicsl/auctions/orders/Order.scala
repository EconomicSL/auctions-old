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

import java.util.UUID

import org.economicsl.auctions.Tradable


/** Base trait defining an order for a particular tradable good. */
sealed trait Order {

  def issuer: UUID

  def isPersistent: Boolean

  def tradable: Tradable

}


trait AskOrder extends Order


trait BidOrder extends Order