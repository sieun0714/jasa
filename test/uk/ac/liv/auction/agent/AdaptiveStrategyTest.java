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

package uk.ac.liv.auction.agent;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.liv.auction.core.Auctioneer;
import uk.ac.liv.auction.core.ClearingHouseAuctioneer;
import uk.ac.liv.auction.core.RandomRobinAuction;
import uk.ac.liv.auction.zi.ZITraderAgent;

public class AdaptiveStrategyTest extends TestCase {

	/**
	 * @uml.property name="strategy"
	 * @uml.associationEnd
	 */
	TestLearnerStrategy strategy;

	static final int NUM_ROUNDS = 10;

	public AdaptiveStrategyTest(String name) {
		super(name);
	}

	public void setUp() {
		strategy = new TestLearnerStrategy();
		strategy.setQuantity(1);
	}

	public void testActionsAndRewards() {
		RandomRobinAuction auction = new RandomRobinAuction("test auction");
		Auctioneer auctioneer = new ClearingHouseAuctioneer(auction);
		auction.setAuctioneer(auctioneer);
		auction.setMaximumRounds(NUM_ROUNDS);
		ZITraderAgent agent = new ZITraderAgent(10, 100, false);
		agent.setStrategy(strategy);
		auction.register(agent);
		auction.run();
		System.out
		    .println("AdaptiveStrategyTest: Testing reward/action cycle count");
		System.out.println("Number of actions = " + strategy.actions);
		System.out.println("Number of rewards = " + strategy.rewards);
		System.out.println("done.");
		assertTrue(strategy.actions == NUM_ROUNDS);
		assertTrue(strategy.rewards == NUM_ROUNDS);
	}

	public void testReset() {
		System.out.println("AdaptiveStrategyTest: Testing reset()");
		testActionsAndRewards();
		strategy.actions = 0;
		strategy.rewards = 0;
		strategy.reset();
		testActionsAndRewards();
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(AdaptiveStrategyTest.class);
	}

}
