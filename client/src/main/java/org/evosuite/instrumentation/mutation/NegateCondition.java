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
/**
 * 
 */
package org.evosuite.instrumentation.mutation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.evosuite.coverage.mutation.Mutation;
import org.evosuite.coverage.mutation.MutationPool;
import org.evosuite.graphs.cfg.BytecodeInstruction;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Frame;


/**
 * <p>NegateCondition class.</p>
 *
 * @author Gordon Fraser
 */
public class NegateCondition implements MutationOperator {

	private static Map<Integer, Integer> opcodeMap = new HashMap<Integer, Integer>();

	static {
		opcodeMap.put(Opcodes.IF_ACMPEQ, Opcodes.IF_ACMPNE);
		opcodeMap.put(Opcodes.IF_ACMPNE, Opcodes.IF_ACMPEQ);
		opcodeMap.put(Opcodes.IF_ICMPEQ, Opcodes.IF_ICMPNE);
		opcodeMap.put(Opcodes.IF_ICMPGE, Opcodes.IF_ICMPLT);
		opcodeMap.put(Opcodes.IF_ICMPGT, Opcodes.IF_ICMPLE);
		opcodeMap.put(Opcodes.IF_ICMPLE, Opcodes.IF_ICMPGT);
		opcodeMap.put(Opcodes.IF_ICMPLT, Opcodes.IF_ICMPGE);
		opcodeMap.put(Opcodes.IF_ICMPNE, Opcodes.IF_ICMPEQ);
		opcodeMap.put(Opcodes.IFEQ, Opcodes.IFNE);
		opcodeMap.put(Opcodes.IFGE, Opcodes.IFLT);
		opcodeMap.put(Opcodes.IFGT, Opcodes.IFLE);
		opcodeMap.put(Opcodes.IFLE, Opcodes.IFGT);
		opcodeMap.put(Opcodes.IFLT, Opcodes.IFGE);
		opcodeMap.put(Opcodes.IFNE, Opcodes.IFEQ);
		opcodeMap.put(Opcodes.IFNONNULL, Opcodes.IFNULL);
		opcodeMap.put(Opcodes.IFNULL, Opcodes.IFNONNULL);
	}

	/* (non-Javadoc)
	 * @see org.evosuite.cfg.instrumentation.MutationOperator#apply(org.objectweb.asm.tree.MethodNode, java.lang.String, java.lang.String, org.evosuite.cfg.BytecodeInstruction)
	 */
	/** {@inheritDoc} */
	@Override
	public List<Mutation> apply(MethodNode mn, String className, String methodName,
	        BytecodeInstruction instruction, Frame frame) {

		List<Mutation> mutations = new LinkedList<Mutation>();

		JumpInsnNode node = (JumpInsnNode) instruction.getASMNode();
		LabelNode target = node.label;

		// insert mutation into bytecode with conditional
		JumpInsnNode mutation = new JumpInsnNode(getOpposite(node.getOpcode()), target);
		// insert mutation into pool
		Mutation mutationObject = MutationPool.addMutation(className,
		                                                   methodName,
		                                                   "NegateCondition",
		                                                   instruction,
		                                                   mutation,
		                                                   Mutation.getDefaultInfectionDistance());

		mutations.add(mutationObject);
		return mutations;
	}

	private static int getOpposite(int opcode) {
		return opcodeMap.get(opcode);
	}

	/* (non-Javadoc)
	 * @see org.evosuite.cfg.instrumentation.mutation.MutationOperator#isApplicable(org.evosuite.cfg.BytecodeInstruction)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean isApplicable(BytecodeInstruction instruction) {
		return instruction.isBranch();
	}

}
