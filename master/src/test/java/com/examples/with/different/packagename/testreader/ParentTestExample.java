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
package com.examples.with.different.packagename.testreader;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.examples.with.different.packagename.testreader.TestExample.MockingBird;


public class ParentTestExample {
	protected static Integer value = 0;

	static {
		value = 5;
	}

	@BeforeClass
	public static void initializeOtherValue() {
		value = Integer.MAX_VALUE;
	}
	
	@BeforeClass
	public static void someInitialization() {
		value = 7;
	}
	
	protected String needed = null;
	
	public ParentTestExample(){
		needed = "break free!";
	}

	@Before
	public void otherSetup() {
		value = 3;
	}

	@Before
	public void setupNeeded() {
		needed = "escape";
	}

//	@Ignore
	@Test
	public void test01() {
		MockingBird bird = MockingBird.create(needed);
		bird.executeCmd(value);
	}
}
