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

import org.objectweb.asm.Opcodes;

public class DivisionByZeroInstrumentation extends ErrorBranchInstrumenter {

	public DivisionByZeroInstrumentation(ErrorConditionMethodAdapter mv) {
		super(mv);
	}

	@Override
	public void visitInsn(int opcode) {
		// Check *DIV for divisonbyzero
		if (opcode == Opcodes.IDIV) {
			mv.visitInsn(Opcodes.DUP);
			insertBranch(Opcodes.IFNE, "java/lang/ArithmeticException");

		} else if (opcode == Opcodes.FDIV) {
			mv.visitInsn(Opcodes.DUP);
			mv.visitLdcInsn(0F);
			mv.visitInsn(Opcodes.FCMPL);
			insertBranch(Opcodes.IFNE, "java/lang/ArithmeticException");

		} else if (opcode == Opcodes.LDIV || opcode == Opcodes.DDIV) {
			mv.visitInsn(Opcodes.DUP2);
			if (opcode == Opcodes.LDIV) {
				mv.visitLdcInsn(0L);
				mv.visitInsn(Opcodes.LCMP);
			} else {
				mv.visitLdcInsn(0.0);
				mv.visitInsn(Opcodes.DCMPL);
			}
			insertBranch(Opcodes.IFNE, "java/lang/ArithmeticException");
		}
	}
}
