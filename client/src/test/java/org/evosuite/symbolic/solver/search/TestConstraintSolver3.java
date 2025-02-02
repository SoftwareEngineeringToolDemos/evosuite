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
package org.evosuite.symbolic.solver.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.evosuite.Properties;
import org.evosuite.Properties.LocalSearchBudgetType;
import org.evosuite.symbolic.expr.Comparator;
import org.evosuite.symbolic.expr.Constraint;
import org.evosuite.symbolic.expr.IntegerConstraint;
import org.evosuite.symbolic.expr.bv.IntegerConstant;
import org.evosuite.symbolic.expr.bv.StringToIntegerCast;
import org.evosuite.symbolic.expr.str.StringVariable;
import org.evosuite.symbolic.solver.SolverEmptyQueryException;
import org.evosuite.symbolic.solver.SolverResult;
import org.evosuite.symbolic.solver.SolverTimeoutException;
import org.evosuite.symbolic.solver.search.EvoSuiteSolver;
import org.junit.Test;

public class TestConstraintSolver3 {

	private static final String INIT_STRING = "125";
	private static final int EXPECTED_INTEGER = 126;

	private static Collection<Constraint<?>> buildConstraintSystem() {

		StringVariable var0 = new StringVariable("var0", INIT_STRING);

		StringToIntegerCast castStr = new StringToIntegerCast(var0, (long) Integer.parseInt(INIT_STRING));

		IntegerConstant const126 = new IntegerConstant(EXPECTED_INTEGER);

		IntegerConstraint constr1 = new IntegerConstraint(castStr, Comparator.EQ, const126);

		return Arrays.<Constraint<?>> asList(constr1);
	}

	@Test
	public void test() throws SolverEmptyQueryException {
		Properties.LOCAL_SEARCH_BUDGET = 100; // 5000000000000L; TODO - ??
		Properties.LOCAL_SEARCH_BUDGET_TYPE = LocalSearchBudgetType.FITNESS_EVALUATIONS;

		Collection<Constraint<?>> constraints = buildConstraintSystem();

		System.out.println("Constraints:");
		for (Constraint<?> c : constraints) {
			System.out.println(c.toString());
		}

		System.out.println("");
		System.out.println("Initial: " + INIT_STRING);

		EvoSuiteSolver solver = new EvoSuiteSolver();
		try {
			SolverResult result = solver.solve(constraints);
			if (result.isUNSAT()) {
				fail("search was unsuccessfull");
			} else {
				Map<String, Object> model = result.getModel();
				Object var0 = model.get("var0");
				System.out.println("Expected: " + EXPECTED_INTEGER);
				System.out.println("Found: " + var0);

				assertEquals(String.valueOf(EXPECTED_INTEGER), var0);
			}
		} catch (SolverTimeoutException e) {
			fail();
		}

	}
}
