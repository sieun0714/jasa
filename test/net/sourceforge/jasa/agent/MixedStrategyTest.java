/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2009 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package net.sourceforge.jasa.agent;

import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import net.sourceforge.jabm.prng.DiscreteProbabilityDistribution;
import net.sourceforge.jasa.agent.strategy.AbstractStrategy;
import net.sourceforge.jasa.agent.strategy.MixedStrategy;
import net.sourceforge.jasa.market.MarketFacade;
import net.sourceforge.jasa.market.auctioneer.Auctioneer;
import net.sourceforge.jasa.market.auctioneer.ClearingHouseAuctioneer;
import net.sourceforge.jasa.sim.PRNGTestSeeds;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MixedStrategyTest extends TestCase {

	TestLearnerStrategy pureStrategy1;

	TestLearnerStrategy pureStrategy2;

	MixedStrategy mixedStrategy;

	DiscreteProbabilityDistribution probabilities;
	
	RandomEngine prng;

	static final int NUM_ROUNDS = 1000;

	static final double STRATEGY1_PROBABILITY = 0.30;

	static final double STRATEGY2_PROBABILITY = 0.70;

	public MixedStrategyTest(String name) {
		super(name);
	}

	public void setUp() {
		
		prng = new MersenneTwister64(PRNGTestSeeds.UNIT_TEST_SEED);
		
		pureStrategy1 = new TestLearnerStrategy();
		pureStrategy1.setQuantity(1);
		pureStrategy1.setBuy(true);

		pureStrategy2 = new TestLearnerStrategy();
		pureStrategy2.setQuantity(1);
		pureStrategy2.setBuy(true);

		probabilities = new DiscreteProbabilityDistribution(prng, 2);
		// probabilities.setSeed(PRNGTestSeeds.UNIT_TEST_SEED);
		probabilities.setProbability(0, STRATEGY1_PROBABILITY);
		probabilities.setProbability(1, STRATEGY2_PROBABILITY);

		mixedStrategy = new MixedStrategy(probabilities, new AbstractStrategy[] {
		    pureStrategy1, pureStrategy2 });

	}

	public void testActionsAndRewards() {
		MarketFacade auction = new MarketFacade(new MersenneTwister64(
				PRNGTestSeeds.UNIT_TEST_SEED));
		Auctioneer auctioneer = new ClearingHouseAuctioneer(auction);
		auction.setAuctioneer(auctioneer);
		auction.setMaximumRounds(NUM_ROUNDS);
		TokenTradingAgent agent = new TokenTradingAgent(10, NUM_ROUNDS,
				auction);
		agent.setStrategy(mixedStrategy);
		pureStrategy1.setAgent(agent);
		pureStrategy2.setAgent(agent);
		auction.register(agent);
		auction.run();
		System.out.println("pureStrategy1 count = " + pureStrategy1.actions);
		System.out.println("pureStrategy2 couint = " + pureStrategy2.actions);
		assertTrue(Math.abs((STRATEGY1_PROBABILITY * NUM_ROUNDS)
				- pureStrategy1.actions) < 0.05 * NUM_ROUNDS);
		assertTrue(Math.abs((STRATEGY2_PROBABILITY * NUM_ROUNDS)
				- pureStrategy2.actions) < 0.05 * NUM_ROUNDS);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(MixedStrategyTest.class);
	}

}
