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
package com.examples.with.different.packagename.junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PassingFooTest {
	
	@Test
	public void test1() {
		Foo foo = new Foo();
		int result = foo.add(10, 10);
		assertEquals(20,result);
	}

	@Test
	public void test2() {
		Foo foo = new Foo();
		int result = foo.add(10, 30);
		assertEquals(40,result);
	}

	@Test
	public void test3() {
		Foo foo = new Foo();
		int result = foo.add(40, 10);
		assertEquals(50,result);
	}

	
}
