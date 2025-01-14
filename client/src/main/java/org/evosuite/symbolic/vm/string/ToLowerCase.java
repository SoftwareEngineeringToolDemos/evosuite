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
package org.evosuite.symbolic.vm.string;

import org.evosuite.symbolic.expr.Operator;
import org.evosuite.symbolic.expr.str.StringUnaryExpression;
import org.evosuite.symbolic.expr.str.StringValue;
import org.evosuite.symbolic.vm.NonNullReference;
import org.evosuite.symbolic.vm.SymbolicEnvironment;
import org.evosuite.symbolic.vm.SymbolicFunction;
import org.evosuite.symbolic.vm.SymbolicHeap;

public final class ToLowerCase extends SymbolicFunction {

	private static final String TO_LOWER_CASE = "toLowerCase";

	public ToLowerCase(SymbolicEnvironment env) {
		super(env, Types.JAVA_LANG_STRING, TO_LOWER_CASE, Types.TO_STR_DESCRIPTOR);
	}

	@Override
	public Object executeFunction() {

		// object receiver
		NonNullReference symb_str = this.getSymbReceiver();
		String conc_str = (String) this.getConcReceiver();

		// return value
		String conc_ret_val = (String) this.getConcRetVal();
		NonNullReference symb_ret_val = (NonNullReference) this.getSymbRetVal();

		StringValue string_expr = env.heap.getField(Types.JAVA_LANG_STRING,
				SymbolicHeap.$STRING_VALUE, conc_str, symb_str, conc_str);
		StringUnaryExpression symb_value = new StringUnaryExpression(
				string_expr, Operator.TOLOWERCASE, conc_ret_val);

		env.heap.putField(Types.JAVA_LANG_STRING, SymbolicHeap.$STRING_VALUE,
				conc_ret_val, symb_ret_val, symb_value);

		return this.getSymbRetVal();
	}

}
