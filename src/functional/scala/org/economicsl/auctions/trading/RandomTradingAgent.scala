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
package org.economicsl.auctions.trading

import java.util.UUID

import org.apache.commons.math3.stat
import org.economicsl.auctions.{Fill, Price, Tradable}
import org.economicsl.auctions.orders._


case class RandomTradingAgent(memory: Int, reservationValue: Double, uuid: UUID)
  extends ((Tradable) => Either[LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit]) {

  def apply(tradable: Tradable): Either[LimitAskOrder with SingleUnit, LimitBidOrder with SingleUnit] = {
    val mostRecentPrice = prices.getValues.lastOption.getOrElse(100.0)
    if (mostRecentPrice >= reservationValue) {
      val limit = Price((1 + 0.05) * reservationValue)
      Left(sellingStrategy(tradable)(limit))
    } else {
      val limit = Price((1 - 0.05) * reservationValue)
      Right(buyingStrategy(tradable)(limit))
    }
  }

  /** Trading agent should be able to observe stuff from its surroundings. */
  def observe: PartialFunction[Any, Unit] = {
    case message: Fill if message.askOrder.issuer == uuid =>
      val surplus = message.price.value - reservationValue
      performance.addValue(surplus)
      prices.addValue(message.price.value)
    case message: Fill if message.bidOrder.issuer == uuid =>
      val surplus = reservationValue - message.price.value
      performance.addValue(surplus)
      prices.addValue(message.price.value)
    case _ => ???
  }

  /** Trading agent should be able to measure its performance. */
  val performance: stat.descriptive.SummaryStatistics = RandomTradingAgent.summaryStatisticsFactory()

  /** Trading agent should store some information which it uses to make decisions. */
  private[this] val prices = new stat.descriptive.DescriptiveStatistics(memory)

  /* Never pay more that reservation value when buying. */
  private[this] val buyingStrategy: Tradable => Price => LimitBidOrder with Persistent with SingleUnit = {
    p => t => new LimitBidOrder with Persistent with SingleUnit { val issuer = uuid; val limit = p; val tradable = t }
  }

  /* Never accept less than reservation value when selling. */
  private[this] val sellingStrategy: Tradable => Price => LimitAskOrder with Persistent with SingleUnit = {
    p => t => new LimitAskOrder with Persistent with SingleUnit { val issuer = uuid; val limit = p; val tradable = t }
  }

}


object RandomTradingAgent {

  def descriptiveStatisticsFactory(window: Int): stat.descriptive.DescriptiveStatistics = {
    new stat.descriptive.DescriptiveStatistics(window)
  }

  def summaryStatisticsFactory(): stat.descriptive.SummaryStatistics = {
    new stat.descriptive.SummaryStatistics()
  }

}