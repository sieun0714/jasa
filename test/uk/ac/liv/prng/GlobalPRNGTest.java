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

package uk.ac.liv.prng;

import java.util.HashMap;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.liv.PRNGTestSeeds;
import uk.ac.liv.util.MutableIntWrapper;
import cern.jet.math.Arithmetic;

/**
 * @author Steve Phelps
 * @version $Revision$
 */

public class GlobalPRNGTest extends TestCase {

	/**
	 * @uml.property name="p"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	protected Integer[] p;

	public static final int N = 4;

	public static final int ITERATIONS = 1000000;

	public GlobalPRNGTest(String name) {
		super(name);
	}

	public void setUp() {
		GlobalPRNG.initialiseWithSeed(PRNGTestSeeds.UNIT_TEST_SEED);
		p = new Integer[N];
		for (int i = 0; i < N; i++) {
			p[i] = new Integer(i);
		}
	}

	public void testPermutations() {
		HashMap hist = new HashMap();
		for (int i = 0; i < ITERATIONS; i++) {
			GlobalPRNG.randomPermutation(p);
			Integer[] p1 = (Integer[]) p.clone();
			Permutation perm = new Permutation(p1);
			MutableIntWrapper count = (MutableIntWrapper) hist.get(perm);
			if (count == null) {
				hist.put(perm, new MutableIntWrapper(1));
			} else {
				count.value++;
			}
		}
		int numPerms = 0;
		Iterator i = hist.keySet().iterator();
		while (i.hasNext()) {
			Permutation perm = (Permutation) i.next();
			numPerms++;
		}
		System.out.println("num permutations = " + numPerms);
		assertTrue(numPerms == (int) Arithmetic.factorial(N));
		int target = ITERATIONS / numPerms;
		i = hist.keySet().iterator();
		while (i.hasNext()) {
			Permutation perm = (Permutation) i.next();
			int instances = ((MutableIntWrapper) hist.get(perm)).value;
			System.out.println(perm + ": " + instances + " (" + target + ")");
			assertTrue(Math.abs(instances - target) < 1000);
		}
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(GlobalPRNGTest.class);
	}

}

class Permutation {

	/**
	 * @uml.property name="elements" multiplicity="(0 -1)" dimension="1"
	 */
	Integer[] elements;

	public Permutation(Integer[] elements) {
		this.elements = elements;
	}

	public boolean equals(Object other) {
		Permutation p = (Permutation) other;
		for (int i = 0; i < elements.length; i++) {
			if (!this.elements[i].equals(p.elements[i])) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < elements.length; i++) {
			hash += elements[i].intValue() * i + 1;
		}
		return hash;
	}

	public String toString() {
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < elements.length; i++) {
			out.append(elements[i] + " ");
		}
		return out.toString();
	}

}