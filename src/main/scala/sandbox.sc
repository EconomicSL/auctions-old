import java.util.UUID

import org.economicsl.auctions.{Price, Tradable}
import org.economicsl.auctions.orders._
import org.economicsl.auctions.orders.singleunit.LimitBidOrder
import org.economicsl.auctions.singleunit.orderbooks.FourHeapOrderBook
import org.economicsl.auctions.singleunit.orders.{LimitAskOrder, LimitBidOrder}



val tradable = new Tradable {}

var matchingEngine = FourHeapOrderBook()

matchingEngine += LimitAskOrder(UUID.randomUUID(), Price(10), tradable)

matchingEngine.matchedOrders.askOrders
matchingEngine.matchedOrders.bidOrders

matchingEngine.unMatchedOrders.askOrders
matchingEngine.unMatchedOrders.bidOrders

matchingEngine += LimitBidOrder(UUID.randomUUID(), Price(12), tradable)

matchingEngine.matchedOrders.askOrders
matchingEngine.matchedOrders.bidOrders

matchingEngine.unMatchedOrders.askOrders
matchingEngine.unMatchedOrders.bidOrders

matchingEngine += LimitBidOrder(UUID.randomUUID(), Price(8), tradable)

matchingEngine.matchedOrders.askOrders
matchingEngine.matchedOrders.bidOrders

matchingEngine.unMatchedOrders.askOrders
matchingEngine.unMatchedOrders.bidOrders

matchingEngine += LimitAskOrder(UUID.randomUUID(), Price(5), tradable)

matchingEngine.matchedOrders.askOrders
matchingEngine.matchedOrders.bidOrders

matchingEngine.unMatchedOrders.askOrders
matchingEngine.unMatchedOrders.bidOrders

matchingEngine += LimitAskOrder(UUID.randomUUID(), Price(3), tradable)

matchingEngine.matchedOrders.askOrders
matchingEngine.matchedOrders.bidOrders

matchingEngine.unMatchedOrders.askOrders
matchingEngine.unMatchedOrders.bidOrders

matchingEngine += LimitBidOrder(UUID.randomUUID(), Price(9), tradable)

matchingEngine.matchedOrders.askOrders
matchingEngine.matchedOrders.bidOrders

matchingEngine.unMatchedOrders.askOrders
matchingEngine.unMatchedOrders.bidOrders

matchingEngine += LimitAskOrder(UUID.randomUUID(), Price(4), tradable)

matchingEngine.matchedOrders.askOrders
matchingEngine.matchedOrders.bidOrders

matchingEngine.unMatchedOrders.askOrders
matchingEngine.unMatchedOrders.bidOrders

