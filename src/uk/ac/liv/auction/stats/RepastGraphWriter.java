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

package uk.ac.liv.auction.stats;

import java.util.Iterator;

import ec.util.Parameter;
import ec.util.ParameterDatabase;

import uk.ac.liv.auction.RepastMarketSimulation;
import uk.ac.liv.util.Parameterizable;
import uk.ac.liv.util.io.DataWriter;

/**
 * @author Steve Phelps
 * @version $Revision$
 */

public class RepastGraphWriter implements DataWriter, Parameterizable {

	protected RepastGraphSequence graphSequence;

	protected String name;

	public static final String P_NAME = "name";

	public void setup(ParameterDatabase parameters, Parameter base) {
		name = parameters.getString(base.push(P_NAME), null);
		graphSequence = new RepastGraphSequence(name);
		RepastMarketSimulation.getModelSingleton().addGraphSequence(graphSequence);
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void flush() {
		// TODO Auto-generated method stub

	}

	public void newData(boolean data) {
		// TODO Auto-generated method stub

	}

	public void newData(double data) {
		graphSequence.newData(data);
	}

	public void newData(Double data) {
		graphSequence.newData(data.doubleValue());
	}

	public void newData(Integer data) {
		graphSequence.newData(data.doubleValue());
	}

	public void newData(Long data) {
		graphSequence.newData(data.doubleValue());
	}

	public void newData(String data) {

	}

	public void newData(float data) {
		// TODO Auto-generated method stub

	}

	public void newData(int data) {
		// TODO Auto-generated method stub

	}

	public void newData(Iterator i) {
		// TODO Auto-generated method stub

	}

	public void newData(long data) {
		// TODO Auto-generated method stub

	}

	public void newData(Object data) {
		// TODO Auto-generated method stub

	}

	public void newData(Object[] data) {
		// TODO Auto-generated method stub

	}

	public RepastGraphSequence getGraphSequence() {
		return graphSequence;
	}
}
