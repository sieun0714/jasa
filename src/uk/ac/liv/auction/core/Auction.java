/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2003 Steve Phelps
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

import uk.ac.liv.util.CummulativeStatCounter;

/**
 * The interface used by agents to interact with an auction.
 *
 * @see AuctionImpl
 *
 * @author Steve Phelps
 * @version $Revision$
 *
 */

public interface Auction extends QuoteProvider {

  /**
   * Returns true if the auction is closed.
   */
  public boolean closed();

  /**
   * Close the auction.
   */
  public void close();

  /**
   * Place a new shout in the auction.
   */
  public void newShout( Shout shout ) throws AuctionException;

  /**
   * Remove a shout from the auction.
   */
  public void removeShout( Shout shout );

 /**
  * Return the last shout placed in the auction.
  */
  public Shout getLastShout() throws ShoutsNotVisibleException;

  /**
   * Return the current auctioneer for this auction.
   */
  public Auctioneer getAuctioneer();

  /**
   * Return statistics on yesterday's transaction price.
   */
  public CummulativeStatCounter getPreviousDayTransPriceStats()
      throws DataUnavailableException;

  /**
   * Calculate the number of unaccepted asks >= price.
   * If accepted is true then count the number of accepted asks.
   * If price is negative then count the number of asks < price.
   */
  public int getNumberOfAsks( double price, boolean accepted )
    throws DataUnavailableException;

  /**
   * Calculate the number of unaccepted bids >= price.
   * If accepted is true then count the number of accepted bids.
   * If price is negative then count the number of bids < price.
   */
  public int getNumberOfBids( double price, boolean accepted )
    throws DataUnavailableException;

  /**
   * Report the state of the auction.
   */
  public void printState();

  /**
   * Handle a single clearing operation between two traders
   */
  public void clear( Shout ask, Shout bid, double price );

  /**
   * Get the age of the auction in unspecified units
   */
  public int getAge();

  /**
   * Get the remaining time in the current trading day (period).
   */
  public int getRemainingTime();

  /**
   * Get the number of traders known to be trading in the auction.
   */
  public int getNumberOfTraders();

  /**
   * Find out whether the given shout has resulted in a transaction
   * in the current round of trading.
   */
  public boolean shoutAccepted( Shout shout );

}