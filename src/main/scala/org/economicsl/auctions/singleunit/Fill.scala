package org.economicsl.auctions.singleunit

import org.economicsl.auctions.singleunit.orders.{AskOrder, BidOrder}
import org.economicsl.auctions.{GenFill, Price, Quantity}


case class Fill(askOrder: AskOrder, bidOrder: BidOrder, price: Price) extends GenFill[AskOrder, BidOrder] {

  val quantity: Quantity = Quantity(1.0)

}
