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

package uk.ac.liv.util;

/**
 * This encapsulation of Integer can be used in combination with the
 * GenericNumber and GenericDouble classes, for performing weakly-typed
 * arithmetic operations.
 * @author Steve Phelps
 *
 */

public class GenericInteger extends GenericNumber {

  Integer value;

  public GenericInteger( Integer value ) {
    this.value = value;
  }

  public GenericNumber add( GenericNumber other ) {
    if ( other instanceof GenericInteger ) {
      return new GenericInteger( new Integer(value.intValue() + other.intValue()) );
    } else if ( other instanceof GenericDouble ) {
      return new GenericDouble( new Double(value.doubleValue() + other.doubleValue()) );
    } else {
      throw new IllegalArgumentException();
    }
  }

  public GenericNumber multiply( GenericNumber other ) {
    if ( other instanceof GenericInteger ) {
      return new GenericInteger( new Integer(value.intValue() * other.intValue()) );
    } else if ( other instanceof GenericDouble ) {
      return new GenericDouble( new Double(value.doubleValue() * other.doubleValue()) );
    } else {
      throw new IllegalArgumentException();
    }
  }

  public GenericNumber subtract( GenericNumber other ) {
    if ( other instanceof GenericInteger ) {
      return new GenericInteger( new Integer(value.intValue() - other.intValue()) );
    } else if ( other instanceof GenericDouble ) {
      return new GenericDouble( new Double(value.doubleValue() - other.doubleValue()) );
    } else {
      throw new IllegalArgumentException();
    }
  }

  public GenericNumber divide( GenericNumber other ) {
    return opResult( value.doubleValue() / other.doubleValue() );
  }

  protected GenericNumber opResult( double tempResult ) {
    long intResult = Math.round(tempResult);
    if ( intResult == tempResult ) {
      return new GenericInteger( new Integer( (int) intResult) );
    } else {
      return new GenericDouble( new Double(tempResult) );
    }
  }


  public int compareTo( Object other ) {
    if ( other instanceof GenericInteger ) {
      return value.compareTo( ((GenericInteger) other).getValue() );
    } else if ( other instanceof GenericDouble ) {
      double d0 = doubleValue();
      double d1 = ((GenericDouble) other).doubleValue();
      if ( d0 < d1 ) {
        return -1;
      } else if ( d0 > d1 ) {
        return +1;
      } else {
        return 0;
      }
    } else {
      throw new ClassCastException();
    }
  }

  public int intValue() {
    return value.intValue();
  }

  public float floatValue() {
    return value.floatValue();
  }

  public double doubleValue() {
    return value.doubleValue();
  }

  public long longValue() {
    return value.longValue();
  }

  public Integer getValue() {
    return value;
  }

}
