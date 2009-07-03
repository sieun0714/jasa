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

package net.sourceforge.jasa.replication.electricity;

import net.sourceforge.jasa.agent.AbstractTradingAgent;
import net.sourceforge.jasa.agent.TradingStrategy;
import net.sourceforge.jasa.agent.strategy.FixedQuantityStrategy;
import net.sourceforge.jasa.event.MarketEvent;
import net.sourceforge.jasa.market.Market;


/**
 * <p>
 * An adaptive trader, trading in a simulated Elecitricty market. Agents of this
 * type have a fixed generating capacity, and they trade units equal to their
 * capacity in each round of the market.
 * </p>
 * 
 * <p>
 * For further details, see:
 * </p>
 * <p>
 * "Market Power and Efficiency in a Computational Electricity Market with
 * Discriminatory Double-Auction Pricing" Nicolaisen, J.; Petrov, V.; and
 * Tesfatsion, L. in IEEE Trans. on Evol. Computation, Vol. 5, No. 5. 2001
 * </p>
 * 
 * <p>
 * This code was written by Steve Phelps in an attempt to replicate the results
 * in the above paper. This work was carried out independently from the original
 * authors. Any corrections to this code are welcome.
 * </p>
 * 
 * <p>
 * <b>Parameters</b><br>
 * </p>
 * <table>
 * 
 * <tr>
 * <td valign=top><i>base</i><tt>.capacity</tt><br>
 * <font size=-1>int >= 0</font></td>
 * <td valign=top>(the generating capacity of the agent)</td>
 * <tr>
 * 
 * </table>
 * 
 * @author Steve Phelps
 * @version $Revision$
 */

public class ElectricityTrader extends AbstractTradingAgent {

	/**
	 * The capacity of this trader in MWh
	 */
	protected int capacity;

	/**
	 * The fixed costs for this trader.
	 */
	protected double fixedCosts;

	static final String P_CAPACITY = "capacity";

	static final String P_FIXED_COSTS = "fixedcosts";

	public ElectricityTrader(int capacity, double privateValue,
	    double fixedCosts, boolean isSeller, TradingStrategy strategy) {
		super(0, 0, privateValue, isSeller, strategy);
		this.capacity = capacity;
		this.fixedCosts = fixedCosts;
		initialise();
	}

	public ElectricityTrader(int capacity, double privateValue,
	    double fixedCosts, boolean isSeller) {
		super(0, 0, privateValue, isSeller);
		this.capacity = capacity;
		this.fixedCosts = fixedCosts;
		initialise();
	}

	public ElectricityTrader() {
		this(0, 0, 0, false);
	}

//	public void setup(ParameterDatabase parameters, Parameter base) {
//		super.setup(parameters, base);
//		capacity = parameters.getInt(base.push(P_CAPACITY), null);
//		fixedCosts = parameters.getDoubleWithDefault(base.push(P_FIXED_COSTS),
//		    null, 0);
//		initialise();
//	}

	public void initialise() {
		super.initialise();
		if (strategy instanceof FixedQuantityStrategy) {
			((FixedQuantityStrategy) strategy).setQuantity(capacity);
		}
	}

	public void requestShout(Market auction) {
		super.requestShout(auction);
		lastProfit = 0;
	}

	//
	// public void informOfSeller( Auction market, Shout winningShout,
	// TradingAgent seller, double price, int quantity ) {
	// super.informOfSeller(market, winningShout, seller, price, quantity);
	// if ( ((ElectricityTrader) seller).acceptDeal(market, price, quantity) )
	// {
	// purchaseFrom(market, (ElectricityTrader) seller, quantity, price);
	// }
	// }

	public boolean acceptDeal(Market auction, double price, int quantity) {
		assert isSeller;
		return price >= valuer.determineValue(auction);
	}

	public int getCapacity() {
		return capacity;
	}

	public double getLastProfit() {
		return lastProfit;
	}

	public double equilibriumProfits(Market auction, double equilibriumPrice,
	    int quantity) {
		double surplus = 0;
		if (isSeller) {
			surplus = equilibriumPrice - getValuation(auction);
		} else {
			surplus = getValuation(auction) - equilibriumPrice;
		}
		// TODO
		if (surplus < 0) {
			surplus = 0;
		}
		return auction.getAge() * quantity * surplus;
	}

	public boolean active() {
		return true;
	}

	public void endOfDay(MarketEvent event) {
		// reset();
	}

	public String toString() {
		return "(" + getClass() + " id:" + id + " capacity:" + capacity
		    + " valuer:" + valuer + " fixedCosts:" + fixedCosts + " profits:"
		    + profits + " isSeller:" + isSeller + " lastProfit:" + lastProfit
		    + " strategy:" + strategy + ")";
	}

}