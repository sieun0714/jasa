/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2002 Steve Phelps
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


import uk.ac.liv.auction.stats.*;

import uk.ac.liv.util.IdAllocator;

import java.io.PrintStream;
import java.io.OutputStream;

import java.util.Observable;


/**
 * An abstract implementation of Auction that provides basic
 * logging facilities and an optional popup GUI console.
 */

public abstract class AuctionImpl extends Observable implements Auction {

  /**
   * The name of this auction.
   */
  String name;

  /**
   * Used to assign unique ids to each instance.
   */
  static IdAllocator idAllocator = new IdAllocator();

  /**
   * A unique id for this auction.  It's main use is in debugging.
   */
  int id;

  /**
   * PrintStream for log output.
   */
  PrintStream logOut = System.out;

  /**
   * The last shout placed in the auction.
   */
  Shout lastShout;

  /**
   * Flag indicating whether the auction is currently closed.
   */
  boolean closed;

  /**
   * Optional graphical console
   */
  AuctionConsoleFrame guiConsole = null;

  /**
   * The plugable bidding logic to use for this auction, e.g. AscendingAuctioneer
   */
  Auctioneer auctioneer = null;

  /**
   * Helper class for logging to CSV files
   */
  MarketDataLogger logger = null;


  public AuctionImpl( String name, MarketDataLogger logger ) {
    id = idAllocator.nextId();
    if ( name != null ) {
      this.name = name;
    } else {
      this.name = "Auction " + id;
    }
    this.logger = logger;
    //initialise();
  }

  public AuctionImpl( String name ) {
    this(name, new CSVMarketDataLogger());
  }

  public AuctionImpl() {
    this(null);
  }

  protected void initialise() {
    lastShout = null;
    closed = false;
  }

  public void reset() {
    initialise();
  }

  public void setAuctioneer( Auctioneer auctioneer ) {
    this.auctioneer = auctioneer;
    auctioneer.setAuction(this);
  }

  public boolean closed() {
    return closed;
  }

  /**
   * Close the auction.
   */
  public void close() {
    closed = true;
  }


  public Shout getLastShout() {
    return lastShout;
  }

  /**
   * Assign a data logger
   */
  public void setMarketDataLogger( MarketDataLogger logger ) {
    this.logger = logger;
  }

  /**
   * Get the current data logger
   */
  public MarketDataLogger getMarketDataLogger() {
    return logger;
  }

  /**
   * Assign a PrintStream for generic logging
   */
  public void setLogOutput( PrintStream logOut ) {
    this.logOut = logOut;
  }

  /**
   * Activate a graphical console for monitoring and controlling
   * the progress of the auction.  Activation of the console
   * may significantly impact the time performance of the auction.
   */
  public void activateGUIConsole() {
    guiConsole = new AuctionConsoleFrame(this,name);
    guiConsole.activate();
    // Add the console as an observer so that it will be informed
    // of state changes when we call notifyObservers().
    addObserver(guiConsole);
  }

  /**
   * Deactivate the graphical console.
   */
  public void deactivateGUIConsole() {
    guiConsole.deactivate();
    deleteObserver(guiConsole);
    guiConsole = null;
  }

  public MarketQuote getQuote() {
    return auctioneer.getQuote();
  }

  public void removeShout( Shout shout ) {
    // Remove this shout and all of its children.
    for( Shout s = shout; s != null; s = s.getChild() ) {
      auctioneer.removeShout(s);
    }
    shout.makeChildless();
  }

  public void printState() {
    auctioneer.printState();
  }



  public String toString() {
    return "(Auction id:" + id + ")";
  }


}