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
package org.evosuite.junit.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.evosuite.junit.FooTestClassLoader;
import org.evosuite.junit.JUnitResult;
import org.junit.Test;


public class JUnitExecutorTest {

	@Test
	public void testJUnitFoo() {
		JUnitExecutor executor = new JUnitExecutor();
		Class<?> fooTestClass = new FooTestClassLoader().loadFooTestClass();
		JUnitResult result = executor.execute(fooTestClass);
		assertNotNull(result);
		assertFalse(result.wasSuccessful());
	}

}
