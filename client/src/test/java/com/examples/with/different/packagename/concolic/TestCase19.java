/**
 * Copyright (C) 2010-2015 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser Public License as published by the
 * Free Software Foundation, either version 3.0 of the License, or (at your
 * option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License along
 * with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.examples.with.different.packagename.concolic;

import static com.examples.with.different.packagename.concolic.Assertions.checkEquals;


public class TestCase19 {

	public static final float FLOAT_VALUE = (float) Math.E;

	public static final double DOUBLE_VALUE = Math.PI;

	/**
	 * @param args
	 */
	public static void test(float float0, double double0) {
		{
			// test getExponent(float,float)
			float float1 = FLOAT_VALUE;
			int int0 = Math.round(float0);
			int int1 = Math.round(float1);
			checkEquals(int0, int1);
		}
		{
			// test getExponent(double,double)
			double double1 = DOUBLE_VALUE;
			long long0 = Math.round(double0);
			long long1 = Math.round(double1);
			checkEquals(long0, long1);
		}
	}

}
