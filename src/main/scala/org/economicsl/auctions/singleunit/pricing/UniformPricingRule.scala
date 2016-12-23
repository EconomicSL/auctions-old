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
package org.economicsl.auctions.pricing

import org.economicsl.auctions.Price
import org.economicsl.auctions.singleunit.orderbooks.FourHeapOrderBook


trait UniformPricingRule extends ((FourHeapOrderBook) => Option[Price])


/** The Mth price rule sets the price to be that of the Mth highest priced `Order with SingleUnit`. */
object MthPriceRule extends UniformPricingRule {

  def apply(orderBook: FourHeapOrderBook): Option[Price] = {
    (orderBook.unMatchedOrders.askOrders.headOption , orderBook.matchedOrders.bidOrders.headOption) match {
      case (Some(askOrder), Some(bidOrder)) => Some(if (askOrder.limit <= bidOrder.limit) askOrder.limit else bidOrder.limit)
      case (None, Some(bidOrder)) => Some(bidOrder.limit)
      case _ => None
    }
  }

}


/** The (M+1)st price rule sets the price to be that of the (M+1)th highest priced `Order with SingleUnit`. */
object MPlusOnePriceRule extends UniformPricingRule {

  def apply(orderBook: FourHeapOrderBook): Option[Price] = {
    (orderBook.matchedOrders.askOrders.headOption , orderBook.unMatchedOrders.bidOrders.headOption) match {
      case (Some(askOrder), Some(bidOrder)) => Some(if (askOrder.limit >= bidOrder.limit) askOrder.limit else bidOrder.limit)
      case (Some(askOrder), None) => Some(askOrder.limit)
      case _ => None
    }
  }

}