/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2004 Steve Phelps
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

package uk.ac.liv.auction.core;


/**
 * An auctioneer for a k-double-auction with continuous clearing.
 * The clearing operation is performed every time a shout arrives.
 * Shouts must beat the current quote in order to be accepted.
 *
 * @author Steve Phelps
 * @version $Revision$
 */

public class KContinuousDoubleAuctioneer extends KAuctioneer {
  
  protected double lowestAsk;
  
  protected double highestBid;

  public KContinuousDoubleAuctioneer() {
    this(null, 0);
  }

  public KContinuousDoubleAuctioneer( double k ) {
    this(null, k);
  }

  public KContinuousDoubleAuctioneer( Auction auction, double k ) {
    this(auction, new DiscriminatoryPricingPolicy(k));
  }

  public KContinuousDoubleAuctioneer( Auction auction, KPricingPolicy policy ) {
    super(auction, policy);
  }

  public KContinuousDoubleAuctioneer( Auction auction ) {
    this(auction, 0);
  }


  public void generateQuote() {
    currentQuote = new MarketQuote(askQuote(), bidQuote());
  }

  public void endOfRoundProcessing() {
    lowestAsk = Double.POSITIVE_INFINITY;
    highestBid = Double.NEGATIVE_INFINITY;    
  }

  public void endOfAuctionProcessing() {
    // do nothing
  }

  public boolean shoutsVisible() {
    return true;
  }

  public void newShout( Shout shout ) throws IllegalShoutException {
    checkImprovement(shout);
    super.newShout(shout);
    clear();
    generateQuote();
  }

  public void checkImprovement( Shout shout ) throws IllegalShoutException {
    double quote;
    if ( shout.isBid() ) {
      quote = bidQuote();
      if ( shout.getPrice() < quote ) {
        bidNotAnImprovementException();
      }
      highestBid = shout.getPrice();
    } else {
      quote = askQuote();
      if ( shout.getPrice() > quote ) {
        askNotAnImprovementException();
      }
      lowestAsk = shout.getPrice();
    }
  }
  
  protected void initialise() {
    super.initialise();
    lowestAsk = Double.POSITIVE_INFINITY;
    highestBid = Double.NEGATIVE_INFINITY;
  }
  
  protected double bidQuote() {
    return highestBid;
  }
  
  protected double askQuote() {
    return lowestAsk;
  }

  private void askNotAnImprovementException()
      throws NotAnImprovementOverQuoteException {
    if ( askException == null ) {
      // Only construct a new exception the once (for improved performance)
      askException = new NotAnImprovementOverQuoteException(DISCLAIMER);
    }
    throw askException;
  }

  private void bidNotAnImprovementException()
      throws NotAnImprovementOverQuoteException {
    if ( bidException == null ) {
      // Only construct a new exception the once (for improved performance)
      bidException = new NotAnImprovementOverQuoteException(DISCLAIMER);
    }
    throw bidException;
  }

  /**
   * Reusable exceptions for performance
   */
  private static NotAnImprovementOverQuoteException askException = null;
  private static NotAnImprovementOverQuoteException bidException = null;


  private static final String DISCLAIMER = "This exception was generated in a lazy manner for performance reasons.  Beware misleading stacktraces.";
}
