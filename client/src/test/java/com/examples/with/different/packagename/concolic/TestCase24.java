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

public class TestCase24 {

	public static final String STRING_VALUE = "Togliere sta roba";

	/**
	 * @param args
	 */
	public static void test(String string0) {

		String string1 = STRING_VALUE;
		{
			String string2 = string0.trim();
			String string3 = string1.trim();
			int int0 = string2.length();
			int int1 = string3.length();
			checkEquals(int0, int1);
		}
		{
			String string2 = string0.toLowerCase();
			String string3 = string1.toLowerCase();
			int int0 = string2.length();
			int int1 = string3.length();
			checkEquals(int0, int1);
		}
		{
			String string2 = string0.toUpperCase();
			String string3 = string1.toUpperCase();
			int int0 = string2.length();
			int int1 = string3.length();
			checkEquals(int0, int1);
		}

	}

}
