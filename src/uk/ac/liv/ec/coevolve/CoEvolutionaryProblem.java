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

package uk.ac.liv.ec.coevolve;

import ec.*;

import java.util.Vector;

/**
 * @author Steve Phelps
 */

public interface CoEvolutionaryProblem  {


  public void evaluate( EvolutionState state,
				  Vector[] group,  // the individuals to evaluate together
				  int threadnum);


}