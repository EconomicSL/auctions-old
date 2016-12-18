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
package org.economicsl.auctions.twosided.singleunit

import java.util.UUID

import com.typesafe.config.ConfigFactory
import org.apache.commons.math3.{distribution, random, stat}
import org.economicsl.auctions.Tradable
import org.economicsl.auctions.trading.RandomTradingAgent

import scala.util.Random


/** Multi-threaded simulation of the continuous k-Double Auction mechanism of Satterthwaite and Williams (JET, 1989). */
object ContinuousKDoubleAuctionSimulation extends App {

  val config = ConfigFactory.load("kDoubleAuctionSimulation.conf")

  // Create something to store simulated prices
  val summaryStatistics = new stat.descriptive.SummaryStatistics()
  val performanceDistribution = new random.EmpiricalDistribution()

  // Create a single source of randomness for simulation in order to minimize indeterminacy
  val seed = config.getLong("seed")
  val prng = new Random(seed)

  // Create a collection of traders each with it own trading rule
  val numberTraders = config.getInt("number-traders")
  val valuations = new distribution.UniformRealDistribution(0, 200)

  val traders = for { i <- 1 to numberTraders } yield RandomTradingAgent(memory=1, valuations.sample(), UUID.randomUUID())


  /** Define the a k-Double Auction. */
  val auction = {

    val tradable = new Tradable {}
    val k = config.getDouble("k")
    new ContinuousKDoubleAuction(k, tradable)

  }

  // simple for loop that actually runs a simulation...
  for { t <- 0 until config.getInt("simulation-length")} {

    traders.par.foreach {
      trader => trader(auction.tradable) match {
        case Left(askOrder) => auction.fill(askOrder).foreach {
          fill => summaryStatistics.addValue(fill.price.value); trader.observe(fill)
        }
        case Right(bidOrder) => auction.fill(bidOrder).foreach {
          fill => summaryStatistics.addValue(fill.price.value); trader.observe(fill)
        }
      }
    }

    //auction.clear()

    println(s"Done with $t steps...")

  }

  /* ...example of a cross sectional computation that is data parallel!
  val averagePerformance = traders.map {
    case Left(sellerTradingRule) => sellerTradingRule.performanceSummary.getMean
    case Right(buyerTradingRule) => buyerTradingRule.performanceSummary.getMean
  }.filterNot ( performance => performance.isNaN )
  performanceDistribution.load(averagePerformance.toArray)
  */

  // ...print to screen for reference...
  println(summaryStatistics.toString)
  //println(performanceDistribution.getSampleStats.toString)

}

