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


package uk.ac.liv.util;

/**
 * Miscalleneous mathematical functions.
 *
 * @author Steve Phelps
 */

public class MathUtil {

  /**
   * Calculate the square of x.
   */
  public static double squared( double x ) {
    return x*x;
  }

  /**
   * Calculate the difference of the squares of x and y.
   */
  public static double diffSq( double x, double y ) {
    return squared(x) - squared(y);
  }

  /**
   * Returns true if the difference between x and y is less than error.
   */
  public static boolean approxEqual( double x, double y, double error ) {
    return Math.abs(x-y) <= error;
  }

}