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
package org.evosuite.instrumentation.error;

import org.evosuite.EvoSuite;
import org.evosuite.Properties;
import org.evosuite.SystemTest;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.strategy.TestGenerationStrategy;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.junit.Assert;
import org.junit.Test;

import com.examples.with.different.packagename.errorbranch.DoubleDivisionByZero;
import com.examples.with.different.packagename.errorbranch.FloatDivisionByZero;
import com.examples.with.different.packagename.errorbranch.IntDivisionByZero;
import com.examples.with.different.packagename.errorbranch.LongDivisionByZero;

public class TestDivisionByZeroInstrumentation extends SystemTest {
	
	@Test
	public void testIntDivisionWithoutErrorBranches() {
		
		EvoSuite evosuite = new EvoSuite();

		String targetClass = IntDivisionByZero.class.getCanonicalName();

		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuite", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		GeneticAlgorithm<?> ga = getGAFromResult(result);
		TestSuiteChromosome best = (TestSuiteChromosome) ga.getBestIndividual();

		int goals = TestGenerationStrategy.getFitnessFactories().get(0).getCoverageGoals().size(); // assuming single fitness function
		Assert.assertEquals("Wrong number of goals: ", 2, goals);
		Assert.assertEquals("Non-optimal coverage: ", 1d, best.getCoverage(), 0.001);
	}
	
	@Test
	public void testIntDivisionWithErrorBranches() {
		
		EvoSuite evosuite = new EvoSuite();

		String targetClass = IntDivisionByZero.class.getCanonicalName();

		Properties.TARGET_CLASS = targetClass;
		Properties.ERROR_BRANCHES = true;

		String[] command = new String[] { "-generateSuite", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);

		GeneticAlgorithm<?> ga = getGAFromResult(result);

		TestSuiteChromosome best = (TestSuiteChromosome) ga.getBestIndividual();

		int goals = TestGenerationStrategy.getFitnessFactories().get(0).getCoverageGoals().size(); // assuming single fitness function
		// 6: 2 regular branches, 2 for overflow, 2 for division by zero
		// one of the overflow branches is infeasible
		Assert.assertTrue("Wrong number of goals: " + goals, goals > 4);
		Assert.assertTrue("Non-optimal coverage: ", best.getCoverage() >= 5d/6d);
	}
	
	@Test
	public void testFloatDivisionWithErrorBranches() {
		
		EvoSuite evosuite = new EvoSuite();

		String targetClass = FloatDivisionByZero.class.getCanonicalName();

		Properties.TARGET_CLASS = targetClass;
		Properties.ERROR_BRANCHES = true;

		String[] command = new String[] { "-generateSuite", "-class", targetClass  };

		Object result = evosuite.parseCommandLine(command);

				GeneticAlgorithm<?> ga = getGAFromResult(result);

		TestSuiteChromosome best = (TestSuiteChromosome) ga.getBestIndividual();

		int goals = TestGenerationStrategy.getFitnessFactories().get(0).getCoverageGoals().size(); // assuming single fitness function
		// 6: 
		Assert.assertTrue("Wrong number of goals: " + goals, goals > 4);
		Assert.assertEquals("Non-optimal coverage: ", 5d/6d, best.getCoverage(), 0.001);
	}
	
	@Test
	public void testDoubleDivisionWithErrorBranches() {
		
		EvoSuite evosuite = new EvoSuite();

		String targetClass = DoubleDivisionByZero.class.getCanonicalName();

		Properties.TARGET_CLASS = targetClass;
		Properties.ERROR_BRANCHES = true;

		String[] command = new String[] { "-generateSuite", "-class", targetClass  };

		Object result = evosuite.parseCommandLine(command);

				GeneticAlgorithm<?> ga = getGAFromResult(result);

		TestSuiteChromosome best = (TestSuiteChromosome) ga.getBestIndividual();

		int goals = TestGenerationStrategy.getFitnessFactories().get(0).getCoverageGoals().size(); // assuming single fitness function
		// 6: 
		Assert.assertTrue("Wrong number of goals: " + goals, goals > 4);
		Assert.assertEquals("Non-optimal coverage: ", 5d/6d, best.getCoverage(), 0.001);
	}
	
	@Test
	public void testLongDivisionWithErrorBranches() {
		
		EvoSuite evosuite = new EvoSuite();

		String targetClass = LongDivisionByZero.class.getCanonicalName();

		Properties.TARGET_CLASS = targetClass;
		Properties.ERROR_BRANCHES = true;

		String[] command = new String[] { "-generateSuite", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);

				GeneticAlgorithm<?> ga = getGAFromResult(result);

		TestSuiteChromosome best = (TestSuiteChromosome) ga.getBestIndividual();

		int goals = TestGenerationStrategy.getFitnessFactories().get(0).getCoverageGoals().size(); // assuming single fitness function
		// 6: 
		Assert.assertTrue("Wrong number of goals: " + goals, goals > 4);
		Assert.assertEquals("Non-optimal coverage: ", 5d/6d, best.getCoverage(), 0.001);
	}
}
