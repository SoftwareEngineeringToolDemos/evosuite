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
package org.evosuite.mock.java.lang;

import org.evosuite.EvoSuite;
import org.evosuite.Properties;
import org.evosuite.SystemTest;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.statistics.RuntimeVariable;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.junit.Assert;
import org.junit.Test;

import com.examples.with.different.packagename.mock.java.lang.HookWithBranch;
import com.examples.with.different.packagename.mock.java.lang.MemorySum;

public class MockRuntimeSystemTest extends SystemTest{

	@Test
	public void testMockMemoryCheck(){
		String targetClass = MemorySum.class.getCanonicalName();

		Properties.TARGET_CLASS = targetClass;		
		Properties.JUNIT_TESTS = true;
		Properties.JUNIT_CHECK = true;
		Properties.REPLACE_CALLS = true;
		Properties.OUTPUT_VARIABLES=""+RuntimeVariable.HadUnstableTests;
		
		EvoSuite evosuite = new EvoSuite();
		String[] command = new String[] { "-generateSuite", "-class", targetClass };
		Object result = evosuite.parseCommandLine(command);

		GeneticAlgorithm<?> ga = getGAFromResult(result);
		TestSuiteChromosome best = (TestSuiteChromosome) ga.getBestIndividual();
		Assert.assertNotNull(best);
		Assert.assertEquals("Non-optimal coverage: ", 1d, best.getCoverage(), 0.001);

		checkUnstable();
	}

	@Test
	public void testCheckShutdownHook(){
		String targetClass = HookWithBranch.class.getCanonicalName();

		Properties.TARGET_CLASS = targetClass;		
		Properties.JUNIT_TESTS = true;
		Properties.JUNIT_CHECK = true;
		Properties.REPLACE_CALLS = true;
		Properties.OUTPUT_VARIABLES=""+RuntimeVariable.HadUnstableTests;
		Properties.MINIMIZE=true;
		
		EvoSuite evosuite = new EvoSuite();
		String[] command = new String[] { "-generateSuite", "-class", targetClass };
		Object result = evosuite.parseCommandLine(command);

		GeneticAlgorithm<?> ga = getGAFromResult(result);
		TestSuiteChromosome best = (TestSuiteChromosome) ga.getBestIndividual();
		Assert.assertNotNull(best);
		Assert.assertEquals("Non-optimal coverage: ", 1d, best.getCoverage(), 0.001);

		checkUnstable();
	}

}
