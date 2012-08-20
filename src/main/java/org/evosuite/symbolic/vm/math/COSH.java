package org.evosuite.symbolic.vm.math;

import org.evosuite.symbolic.expr.Operator;
import org.evosuite.symbolic.expr.RealExpression;
import org.evosuite.symbolic.expr.RealUnaryExpression;
import org.evosuite.symbolic.vm.SymbolicEnvironment;


public final class COSH extends MathFunction_D2D {

	private static final String COSH = "cosh";

	public COSH(SymbolicEnvironment env) {
		super(env, COSH);
	}

	@Override
	protected RealExpression executeFunction(double res) {
		Operator op = Operator.COSH;
		return new RealUnaryExpression(realExpression, op, res);
	}

}