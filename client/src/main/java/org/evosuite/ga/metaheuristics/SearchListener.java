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
package org.evosuite.ga.metaheuristics;

import org.evosuite.ga.Chromosome;


/**
 * A listener that can be attached to the search
 *
 * @author Gordon Fraser
 */
public interface SearchListener {

	/**
	 * Called when a new search is started
	 *
	 * @param algorithm a {@link org.evosuite.ga.metaheuristics.GeneticAlgorithm} object.
	 */
	public void searchStarted(GeneticAlgorithm<?> algorithm);

	/**
	 * Called after each iteration of the search
	 *
	 * @param algorithm a {@link org.evosuite.ga.metaheuristics.GeneticAlgorithm} object.
	 */
	public void iteration(GeneticAlgorithm<?> algorithm);

	/**
	 * Called after the last iteration
	 *
	 * @param algorithm a {@link org.evosuite.ga.metaheuristics.GeneticAlgorithm} object.
	 */
	public void searchFinished(GeneticAlgorithm<?> algorithm);

	/**
	 * Called after every single fitness evaluation
	 *
	 * @param individual a {@link org.evosuite.ga.Chromosome} object.
	 */
	public void fitnessEvaluation(Chromosome individual);

	/**
	 * Called before a chromosome is mutated
	 *
	 * @param individual a {@link org.evosuite.ga.Chromosome} object.
	 */
	public void modification(Chromosome individual);

}
