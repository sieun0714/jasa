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

package net.sourceforge.jasa.market;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import net.sourceforge.jasa.agent.MockTrader;
import net.sourceforge.jasa.market.DuplicateShoutException;
import net.sourceforge.jasa.market.FourHeapOrderBook;
import net.sourceforge.jasa.market.Order;

import org.apache.commons.collections.buffer.PriorityBuffer;


/**
 * @author Steve Phelps
 * @version $Revision$
 */

public class FourHeapTest extends TestCase {

	/**
	 * @uml.property name="orderBook"
	 * @uml.associationEnd
	 */
	TestShoutEngine shoutEngine;

	/**
	 * @uml.property name="testTrader"
	 * @uml.associationEnd
	 */
	MockTrader testTrader;

	/**
	 * @uml.property name="randGenerator"
	 */
	Random randGenerator;

	public FourHeapTest(String name) {
		super(name);
	}

	public void setUp() {
		shoutEngine = new TestShoutEngine();
		testTrader = new MockTrader(this, 0, 0);
		randGenerator = new Random();
//		org.apache.log4j.BasicConfigurator.configure();
	}

	public Order randomShout() {
		int quantity = randGenerator.nextInt(50);
		double price = randGenerator.nextDouble() * 100;
		boolean isBid = randGenerator.nextBoolean();
		return new Order(testTrader, quantity, price, isBid);
	}

	public void testRandom() {

		int matches = 0;

		try {

			Order testRemoveShout = null, testRemoveShout2 = null;

			for (int round = 0; round < 700; round++) {

				if (testRemoveShout != null) {
					shoutEngine.removeShout(testRemoveShout);
					shoutEngine.removeShout(testRemoveShout2);
				}

				for (int shout = 0; shout < 200; shout++) {
					shoutEngine.newShout(randomShout());
				}

				shoutEngine.newShout(testRemoveShout = randomShout());
				testRemoveShout2 = (Order) testRemoveShout.clone();
				testRemoveShout2 = new Order(testRemoveShout.getAgent(),
				    testRemoveShout.getQuantity(), testRemoveShout.getPrice(),
				    !testRemoveShout.isBid());
				shoutEngine.newShout(testRemoveShout2);

				if ((round & 0x01) > 0) {
					continue;
				}

				List matched = shoutEngine.getMatchedShouts();
				Iterator i = matched.iterator();
				while (i.hasNext()) {
					matches++;
					Order bid = (Order) i.next();
					Order ask = (Order) i.next();
					assertTrue(bid.isBid());
					assertTrue(ask.isAsk());
					assertTrue(bid.getPrice() >= ask.getPrice());
					// System.out.print(bid + "/" + ask + " ");
				}
				// System.out.println("");
			}

		} catch (Exception e) {
			shoutEngine.printState();
			e.printStackTrace();
			fail();
		}

		System.out.println("Matches = " + matches);

	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(FourHeapTest.class);
	}

}

class TestShoutEngine extends FourHeapOrderBook {

	protected void preRemovalProcessing() {
		checkBalanced();
	}

	protected void postRemovalProcessing() {
		checkBalanced();
	}

	protected void checkBalanced() {

		int nS = countQty(sIn);
		int nB = countQty(bIn);
		if (nS != nB) {
			printState();
			throw new Error("shout heaps not balanced nS=" + nS + " nB=" + nB);
		}

		Order bInTop = getLowestMatchedBid();
		Order sInTop = getHighestMatchedAsk();
		Order bOutTop = getHighestUnmatchedBid();
		Order sOutTop = getLowestUnmatchedAsk();

		checkBalanced(bInTop, bOutTop, "bIn >= bOut");
		checkBalanced(sOutTop, sInTop, "sOut >= sIn");
		checkBalanced(sOutTop, bOutTop, "sOut >= bOut");
		checkBalanced(bInTop, sInTop, "bIn >= sIn");
	}

	protected void checkBalanced(Order s1, Order s2, String condition) {
		if (!((s1 == null || s2 == null) || s1.getPrice() >= s2.getPrice())) {
			printState();
			System.out.println("shout1 = " + s1);
			System.out.println("shout2 = " + s2);
			throw new Error("Heaps not balanced! - " + condition);
		}
	}

	public static int countQty(PriorityBuffer heap) {
		Iterator i = heap.iterator();
		int qty = 0;
		while (i.hasNext()) {
			Order s = (Order) i.next();
			qty += s.getQuantity();
		}
		return qty;
	}

	public void newShout(Order shout) throws DuplicateShoutException {
		if (shout.isAsk()) {
			newAsk(shout);
		} else {
			newBid(shout);
		}
	}

}