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

package net.sourceforge.jasa.market.auctioneer;

import java.io.Serializable;

import net.sourceforge.jasa.market.Account;
import net.sourceforge.jasa.market.DuplicateShoutException;
import net.sourceforge.jasa.market.IllegalShoutException;
import net.sourceforge.jasa.market.Market;
import net.sourceforge.jasa.market.MarketQuote;
import net.sourceforge.jasa.market.NotAnImprovementOverQuoteException;
import net.sourceforge.jasa.market.Order;
import net.sourceforge.jasa.market.ZeroFundsAccount;
import net.sourceforge.jasa.sim.prng.GlobalPRNG;

import org.apache.log4j.Logger;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;


/**
 * <p>
 * An auctioneer for a double market with market cleared time to time with
 * probability specified by a threshold value.
 * </p>
 * 
 * <p>
 * The clearing operation is performed with a probability specified by a
 * threshold value every time a shout arrives. Shouts must beat the current
 * quote as in <code>
 * ContinuousDoubleAuctioneer</code>. When threshold is 0,
 * the market is only cleared when each round ends and is thus equivalent to a
 * clearing house, while when threshold is 1, the market becomes a continuous
 * double market.
 * </p>
 * 
 * <p>
 * <b>Parameters </b>
 * </p>
 * 
 * <table>
 * 
 * <tr>
 * <td valign=top><i>base </i> <tt>.threshold</tt><br>
 * <font size=-1>0<= double <=1 </font></td>
 * <td valign=top>(the probability the market is cleared when a new shout
 * arrives)</td>
 * <tr>
 * 
 * </table>
 * 
 * @deprecated
 * @author Jinzhong Niu
 * @version $Revision$
 */

public class RandomClearingDoubleAuctioneer extends TransparentAuctioneer
    implements Serializable {

	static Logger logger = Logger.getLogger(RandomClearingDoubleAuctioneer.class);

	protected ZeroFundsAccount account;

	Uniform uniformDistribution;

	private double threshold = 0.5;

	public static final String P_THRESHOLD = "threshold";

	public static final String P_DEF_BASE = "rda";

	public RandomClearingDoubleAuctioneer() {
		this(null);
	}

	public RandomClearingDoubleAuctioneer(Market auction) {
		super(auction);
		account = new ZeroFundsAccount(this);
	}

	protected void initialise() {
		RandomEngine prng = GlobalPRNG.getInstance();
		uniformDistribution = new Uniform(0, 1, prng);
	}

//	public void setup(ParameterDatabase parameters, Parameter base) {
//		super.setup(parameters, base);
//
//		threshold = parameters.getDoubleWithDefault(base.push(P_THRESHOLD),
//		    new Parameter(P_DEF_BASE).push(P_THRESHOLD), threshold);
//		assert (0 <= threshold && threshold <= 1);
//	}

	public void generateQuote() {
		currentQuote = new MarketQuote(askQuote(), bidQuote());
	}

	public Account getAccount() {
		return account;
	}

	public void endOfRoundProcessing() {
		super.endOfRoundProcessing();
		generateQuote();
		clear();
	}

	public void endOfAuctionProcessing() {
		super.endOfAuctionProcessing();
	}

	public void newShoutInternal(Order shout) throws DuplicateShoutException {
		orderBook.newShout(shout);
		double d = uniformDistribution.nextDouble();
		if (d < threshold) {
			generateQuote();
			clear();
		}
	}

	protected void checkShoutValidity(Order shout) throws IllegalShoutException {
		super.checkShoutValidity(shout);
		checkImprovement(shout);
	}

	public void checkImprovement(Order shout) throws IllegalShoutException {
		double quote;
		if (shout.isBid()) {
			quote = bidQuote();
			if (shout.getPrice() < quote) {
				bidNotAnImprovementException();
			}
		} else {
			quote = askQuote();
			if (shout.getPrice() > quote) {
				askNotAnImprovementException();
			}
		}
	}

	protected void askNotAnImprovementException()
	    throws NotAnImprovementOverQuoteException {
		if (askException == null) {
			// Only construct a new exception the once (for improved
			// performance)
			askException = new NotAnImprovementOverQuoteException(DISCLAIMER);
		}
		throw askException;
	}

	protected void bidNotAnImprovementException()
	    throws NotAnImprovementOverQuoteException {
		if (bidException == null) {
			// Only construct a new exception the once (for improved
			// performance)
			bidException = new NotAnImprovementOverQuoteException(DISCLAIMER);
		}
		throw bidException;
	}

	/**
	 * Reusable exceptions for performance
	 */
	protected static NotAnImprovementOverQuoteException askException = null;

	protected static NotAnImprovementOverQuoteException bidException = null;

	protected static final String DISCLAIMER = "This exception was generated in a lazy manner for performance reasons.  Beware misleading stacktraces.";

}