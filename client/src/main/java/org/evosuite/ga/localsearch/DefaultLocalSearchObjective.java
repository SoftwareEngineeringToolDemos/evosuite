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
package org.evosuite.ga.localsearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;

/**
 * <p>DefaultLocalSearchObjective class.</p>
 *
 * @author Gordon Fraser
 */
public class DefaultLocalSearchObjective<T extends Chromosome> implements LocalSearchObjective<T>, Serializable {

	private static final long serialVersionUID = -8640106627078837108L;

	private final List<FitnessFunction<? extends Chromosome>> fitnessFunctions = new ArrayList<>();

	// TODO: This assumes we are not doing NSGA-II
	private boolean isMaximization = false;
	
	@Override
	public boolean isDone() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.evosuite.ga.LocalSearchObjective#hasImproved(org.evosuite.ga.Chromosome)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean hasImproved(T individual) {
		throw new UnsupportedOperationException("Not implemented for default objective");
	}
	
	@Override
	public void addFitnessFunction(FitnessFunction<? extends Chromosome> fitness) {
		for(FitnessFunction<? extends Chromosome> ff : fitnessFunctions) {
			if(ff.isMaximizationFunction() != fitness.isMaximizationFunction()) {
				throw new RuntimeException("Local search only supports composition of multiple criteria");
			}
		}
		if(fitness.isMaximizationFunction())
			isMaximization = true;
		else
			isMaximization = false;

		fitnessFunctions.add(fitness);
	}
	
	@Override
	public boolean isMaximizationObjective() {
		return isMaximization;
	}

	/* (non-Javadoc)
	 * @see org.evosuite.ga.LocalSearchObjective#getFitnessFunction()
	 */
	/** {@inheritDoc} */
	@Override
	public List<FitnessFunction<? extends Chromosome>> getFitnessFunctions() {
		return fitnessFunctions;
	}

	/* (non-Javadoc)
	 * @see org.evosuite.ga.LocalSearchObjective#hasChanged(org.evosuite.ga.Chromosome)
	 */
	/** {@inheritDoc} */
	@Override
	public int hasChanged(T individual) {
		throw new UnsupportedOperationException("Not implemented for default objective");
	}

	/* (non-Javadoc)
	 * @see org.evosuite.ga.LocalSearchObjective#hasNotWorsened(org.evosuite.ga.Chromosome)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean hasNotWorsened(T individual) {
		throw new UnsupportedOperationException("Not implemented for default objective");
	}

	@Override
	public void retainPartialSolution(T individual) {
		// Ignore		
	}

}
