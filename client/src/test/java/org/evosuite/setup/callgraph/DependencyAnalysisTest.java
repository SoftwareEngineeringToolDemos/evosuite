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
package org.evosuite.setup.callgraph;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.evosuite.Properties;
import org.evosuite.Properties.Criterion;
import org.evosuite.classpath.ClassPathHandler;
import org.evosuite.setup.DependencyAnalysis;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DependencyAnalysisTest {

	@BeforeClass
	public static void initialize() {
		Properties.TARGET_CLASS = "com.examples.with.different.packagename.context.complex.EntryPointsClass";
		Properties.CRITERION = new Criterion[1];
		Properties.CRITERION[0]=Criterion.IBRANCH;
		List<String> classpath = new ArrayList<>();
		String cp = System.getProperty("user.dir") + "/target/test-classes";
		classpath.add(cp);
		ClassPathHandler.getInstance().addElementToTargetProjectClassPath(cp);
		try {
			DependencyAnalysis
					.analyzeClass(
							"com.examples.with.different.packagename.context.complex.EntryPointsClass",
							classpath);
		} catch (ClassNotFoundException | RuntimeException e) {
			Assert.fail(e.toString());
		}
	}

	@Test
	public void test1levelContext() {
		String context1 = DependencyAnalysis
				.getCallGraph()
				.getAllContextsFromTargetClass(
						"com.examples.with.different.packagename.context.complex.SubClass",
						"checkFiftneen(I)Z").toString();

		assertEquals(
				context1,
				"[com.examples.with.different.packagename.context.complex.EntryPointsClass:dosmt(ILjava/lang/String;D)V com.examples.with.different.packagename.context.complex.SubClass:checkFiftneen(I)Z]");
	}
	
	/**
	 * test level 2 context masked by an abtract class
	 */
	@Test
	public void test2levelContext() {
		String context2 = DependencyAnalysis
				.getCallGraph()
				.getAllContextsFromTargetClass(
						"com.examples.with.different.packagename.context.complex.SubClass",
						"bla(I)Z").toString(); 

		assertEquals(
				context2,
				"[com.examples.with.different.packagename.context.complex.EntryPointsClass:dosmt(ILjava/lang/String;D)V com.examples.with.different.packagename.context.complex.SubClass:checkFiftneen(I)Z com.examples.with.different.packagename.context.complex.SubClass:bla(I)Z]");
	}

	/**
	 * test level 3 context masked by an abstract class and an interface
	 */
	@Test
	public void test3levelContext() {
		String context2 = DependencyAnalysis
				.getCallGraph()
				.getAllContextsFromTargetClass(
						"com.examples.with.different.packagename.context.complex.SubSubClass",
						"innermethod(I)Z").toString(); 
		assertEquals(
				context2,
				"[com.examples.with.different.packagename.context.complex.EntryPointsClass:dosmt(ILjava/lang/String;D)V com.examples.with.different.packagename.context.complex.SubClass:checkFiftneen(I)Z com.examples.with.different.packagename.context.complex.SubClass:bla(I)Z com.examples.with.different.packagename.context.complex.SubSubClass:innermethod(I)Z]");
	}
	
	@Test
	public void testContextInParamethers() {
		String context2 = DependencyAnalysis
				.getCallGraph()
				.getAllContextsFromTargetClass(
						"com.examples.with.different.packagename.context.complex.ParameterObject",
						"isEnabled()Z").toString();
		assertEquals(
				context2,
				"[com.examples.with.different.packagename.context.complex.EntryPointsClass:doObj(Lcom/examples/with/different/packagename/context/complex/AParameterObject;)V com.examples.with.different.packagename.context.complex.ParameterObject:isEnabled()Z]");
	}
	
	//

}