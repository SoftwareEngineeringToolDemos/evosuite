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
package org.evosuite.symbolic.solver;

import java.util.HashMap;
import java.util.Map;

public class SolverResult {

	private enum SolverResultType {
		SAT, UNSAT
	};

	private final SolverResultType resultType;

	private final Map<String, Object> model;

	private SolverResult(SolverResultType t, Map<String, Object> model) {
		this.resultType = t;
		this.model = model;
	}

	public static SolverResult newUNSAT() {
		return new SolverResult(SolverResultType.UNSAT, null);
	}

	public static SolverResult newSAT(Map<String, Object> values) {
		return new SolverResult(SolverResultType.SAT, values);
	}

	public boolean isSAT() {
		return resultType.equals(SolverResultType.SAT);
	}

	public boolean containsVariable(String var_name) {
		if (!resultType.equals(SolverResultType.SAT)) {
			throw new IllegalStateException("This method should not be called with a non-SAT result");
		}
		return model.containsKey(var_name);
	}

	public Object getValue(String var_name) {
		if (!resultType.equals(SolverResultType.SAT)) {
			throw new IllegalStateException("This method should not be called with a non-SAT result");
		}
		return model.get(var_name);
	}

	public Map<String, Object> getModel() {
		HashMap<String, Object> newModel = new HashMap<String, Object>(model);
		return newModel;
	}

	public boolean isUNSAT() {
		return resultType.equals(SolverResultType.UNSAT);

	}

}
