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
package org.evosuite.testsuite.localsearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.ga.localsearch.LocalSearchBudget;
import org.evosuite.ga.localsearch.LocalSearchObjective;
import org.evosuite.symbolic.BranchCondition;
import org.evosuite.symbolic.ConcolicExecution;
import org.evosuite.symbolic.DSEStats;
import org.evosuite.symbolic.expr.Comparator;
import org.evosuite.symbolic.expr.Constraint;
import org.evosuite.symbolic.expr.Expression;
import org.evosuite.symbolic.expr.Variable;
import org.evosuite.symbolic.solver.SolverCache;
import org.evosuite.symbolic.solver.Solver;
import org.evosuite.symbolic.solver.SolverFactory;
import org.evosuite.symbolic.solver.SolverResult;
import org.evosuite.testcase.statements.Statement;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.statements.PrimitiveStatement;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.evosuite.testsuite.TestSuiteFitnessFunction;
import org.evosuite.utils.Randomness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * TestSuiteDSE class.
 * </p>
 * 
 * @author Gordon Fraser
 */
public class TestSuiteDSE {

	private static final Logger logger = LoggerFactory.getLogger(TestSuiteDSE.class);

	/** Constant <code>nrConstraints=0</code> */
	public static int nrConstraints = 0;

	/** Constant <code>nrSolvedConstraints=0</code> */
	public static int nrSolvedConstraints = 0;
	private int nrCurrConstraints = 0;

	/** Constant <code>success=0</code> */
	public static int success = 0;
	/** Constant <code>failed=0</code> */
	public static int failed = 0;

	private LocalSearchObjective<TestSuiteChromosome> objective;

	// private final TestSuiteFitnessFunction fitness;

	private final Map<TestChromosome, List<BranchCondition>> branchConditions = new HashMap<TestChromosome, List<BranchCondition>>();

	private final Set<BranchCondition> unsolvableBranchConditions = new HashSet<BranchCondition>();

	private final Map<String, Integer> solutionAttempts = new HashMap<String, Integer>();

	private final Collection<TestBranchPair> unsolvedBranchConditions;

	private class TestBranchPair implements Comparable<TestBranchPair> {
		TestChromosome test;
		BranchCondition branch;

		private final double ranking;

		TestBranchPair(TestChromosome test, BranchCondition branchCondition) {
			this.test = test;
			this.branch = branchCondition;
			this.ranking = computeRanking(branchCondition);
		}

		private double computeRanking(BranchCondition condition) {
			int length = 1 + condition.getReachingConstraints().size();

			int totalSize = 0;
			for (Constraint<?> constraint : condition.getReachingConstraints()) {
				totalSize += constraint.getSize();
			}
			double avg_size = (double) totalSize / (double) condition.getReachingConstraints().size();

			double ranking = length * avg_size;
			return ranking;
		}

		@Override
		public int compareTo(TestBranchPair arg0) {
			return Double.compare(this.ranking, arg0.ranking);
		}

	}

	/**
	 * <p>
	 * Constructor for TestSuiteDSE.
	 * </p>
	 * 
	 * @param fitness
	 *            a {@link org.evosuite.testsuite.TestSuiteFitnessFunction}
	 *            object.
	 */
	public TestSuiteDSE(LocalSearchObjective<TestSuiteChromosome> objective) {
		this.objective = objective;
		if (Properties.DSE_RANK_BRANCH_CONDITIONS) {
			this.unsolvedBranchConditions = new PriorityQueue<TestBranchPair>();
		} else {
			this.unsolvedBranchConditions = new ArrayList<TestBranchPair>();
		}

	}

	/**
	 * Iterate over path constraints to identify those which map to branches
	 * that are only covered one way
	 */
	private void calculateUncoveredBranches() {
		unsolvedBranchConditions.clear();

		if (Properties.DSE_NEGATE_ALL_CONDITIONS == true) {

			for (TestChromosome testChromosome : branchConditions.keySet()) {
				for (BranchCondition branchCondition : branchConditions.get(testChromosome)) {
					if (!unsolvableBranchConditions.contains(branchCondition)) {
						unsolvedBranchConditions.add(new TestBranchPair(testChromosome, branchCondition));
					}
				}
			}
		} else {
			Map<String, Map<Comparator, Set<TestBranchPair>>> solvedConstraints = new HashMap<String, Map<Comparator, Set<TestBranchPair>>>();
			for (TestChromosome test : branchConditions.keySet()) {
				for (BranchCondition branch : branchConditions.get(test)) {

					if (unsolvableBranchConditions.contains(branch))
						continue;

					String index = getBranchIndex(branch);
					if (!solvedConstraints.containsKey(index))
						solvedConstraints.put(index, new HashMap<Comparator, Set<TestBranchPair>>());

					Constraint<?> c = branch.getLocalConstraint();

					if (!solvedConstraints.get(index).containsKey(c.getComparator()))
						solvedConstraints.get(index).put(c.getComparator(), new HashSet<TestBranchPair>());

					solvedConstraints.get(index).get(c.getComparator()).add(new TestBranchPair(test, branch));
				}
			}

			for (String index : solvedConstraints.keySet()) {
				if (solvedConstraints.get(index).size() == 1) {
					Set<TestBranchPair> branches = solvedConstraints.get(index).values().iterator().next();
					unsolvedBranchConditions.addAll(branches);
				}
			}
			logger.info("Update set of unsolved branch conditions to " + unsolvedBranchConditions.size());

			if (Properties.DSE_RANK_BRANCH_CONDITIONS == false) {
				Randomness.shuffle((ArrayList<TestBranchPair>) unsolvedBranchConditions);
			}
		}
	}

	/**
	 * Calculate and store path constraints for an individual
	 * 
	 * @param test
	 */
	private void updatePathConstraints(TestChromosome test) {
		List<BranchCondition> branches = ConcolicExecution.getSymbolicPath(test);
		branchConditions.put(test, branches);
	}

	/**
	 * Create path constraints for all tests in a test suite
	 * 
	 * @param testSuite
	 */
	private void createPathConstraints(TestSuiteChromosome testSuite) {

		for (TestChromosome test : testSuite.getTestChromosomes()) {
			updatePathConstraints(test);
		}
		calculateUncoveredBranches();
	}

	private String getBranchIndex(BranchCondition branch) {
		return branch.getFullName() + branch.getInstructionIndex();
	}

	/**
	 * Get a new candidate for negation
	 * 
	 * @return
	 */
	private TestBranchPair getNextBranchCondition() {
		TestBranchPair pair;
		pair = getNextTestBranchPair();

		if (Properties.DSE_NEGATE_ALL_CONDITIONS == true) {
			return pair;
		}

		String index = getBranchIndex(pair.branch);
		if (!unsolvedBranchConditions.isEmpty()) {
			while (solutionAttempts.containsKey(index)
					&& solutionAttempts.get(index) >= Properties.CONSTRAINT_SOLUTION_ATTEMPTS
					&& !unsolvedBranchConditions.isEmpty()) {
				logger.info("Reached maximum number of attempts for branch " + index);
				pair = getNextTestBranchPair();
				index = getBranchIndex(pair.branch);
			}
		}

		if (!solutionAttempts.containsKey(index))
			solutionAttempts.put(index, 1);
		else
			solutionAttempts.put(index, solutionAttempts.get(index) + 1);

		return pair;
	}

	private TestBranchPair getNextTestBranchPair() {
		TestBranchPair pair;
		if (Properties.DSE_RANK_BRANCH_CONDITIONS) {
			pair = ((PriorityQueue<TestBranchPair>) unsolvedBranchConditions).poll();
		} else {
			pair = ((ArrayList<TestBranchPair>) unsolvedBranchConditions).remove(0);
		}
		return pair;
	}

	/**
	 * Check if there are further candidates for negation
	 * 
	 * @return
	 */
	private boolean hasNextBranchCondition() {
		return !unsolvedBranchConditions.isEmpty();
	}

	/**
	 * Generate new constraint and ask solver for solution
	 * 
	 * @param condition
	 * @param test
	 * @return
	 */
	// @SuppressWarnings("rawtypes")
	// @SuppressWarnings("rawtypes")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TestCase negateCondition(Set<Constraint<?>> reachingConstraints, Constraint<?> localConstraint,
			TestCase test) {
		List<Constraint<?>> constraints = new LinkedList<Constraint<?>>();
		constraints.addAll(reachingConstraints);

		Constraint<?> targetConstraint = localConstraint.negate();
		constraints.add(targetConstraint);
		if (!targetConstraint.isSolveable()) {
			logger.info("Found unsolvable constraint: " + targetConstraint);
			// TODO: This is usually the case when the same variable is used for
			// several parameters of a method
			// Could we treat this as a special case?
			return null;
		}

		int size = constraints.size();
		/*
		 * int counter = 0; for (Constraint cnstr : constraints) { logger.debug(
		 * "Cnstr " + (counter++) + " : " + cnstr + " dist: " +
		 * DistanceEstimator.getDistance(constraints)); }
		 */
		if (size > 0) {
			logger.debug("Calculating cone of influence for " + size + " constraints");
			constraints = reduce(constraints);
			logger.info("Reduced constraints from " + size + " to " + constraints.size());
			// for (Constraint<?> c : constraints) {
			// logger.info(c.toString());
			// }
		}

		nrCurrConstraints = constraints.size();
		nrConstraints += nrCurrConstraints;

		logger.info("Applying local search");
		Solver solver = SolverFactory.getInstance().buildNewSolver();
		DSEStats.reportNewConstraints(constraints);

		long startSolvingTime = System.currentTimeMillis();
		SolverCache solverCache = SolverCache.getInstance();
		SolverResult solverResult = solverCache.solve(solver, constraints);
		long estimatedSolvingTime = System.currentTimeMillis() - startSolvingTime;
		DSEStats.reportNewSolvingTime(estimatedSolvingTime);

		if (solverResult == null) {
			logger.info("Found no solution");
			/* Timeout, parseException, error, trivialSolution, etc. */
			return null;

		} else if (solverResult.isUNSAT()) {

			logger.info("Found UNSAT solution");
			DSEStats.reportNewUNSAT();
			return null;

		} else {

			Map<String, Object> model = solverResult.getModel();
			DSEStats.reportNewSAT();

			TestCase newTest = test.clone();

			for (Object key : model.keySet()) {
				Object val = model.get(key);
				if (val != null) {
					logger.info("New value: " + key + ": " + val);
					if (val instanceof Long) {
						Long value = (Long) val;
						String name = ((String) key).replace("__SYM", "");
						// logger.warn("New long value for " + name + " is " +
						// value);
						PrimitiveStatement p = getStatement(newTest, name);
						if (p.getValue().getClass().equals(Character.class))
							p.setValue((char) value.intValue());
						else if (p.getValue().getClass().equals(Long.class))
							p.setValue(value);
						else if (p.getValue().getClass().equals(Integer.class))
							p.setValue(value.intValue());
						else if (p.getValue().getClass().equals(Short.class))
							p.setValue(value.shortValue());
						else if (p.getValue().getClass().equals(Boolean.class))
							p.setValue(value.intValue() > 0);
						else if (p.getValue().getClass().equals(Byte.class))
							p.setValue(value.byteValue() > 0);
						else
							logger.warn("New value is of an unsupported type: " + p.getValue().getClass() + val);
					} else if (val instanceof String) {
						String name = ((String) key).replace("__SYM", "");
						PrimitiveStatement p = getStatement(newTest, name);
						// logger.warn("New string value for " + name + " is " +
						// val);
						assert(p != null) : "Could not find variable " + name + " in test: " + newTest.toCode()
								+ " / Orig test: " + test.toCode() + ", seed: " + Randomness.getSeed();
						if (p.getValue().getClass().equals(Character.class))
							p.setValue((char) Integer.parseInt(val.toString()));
						else
							p.setValue(val.toString());
					} else if (val instanceof Double) {
						Double value = (Double) val;
						String name = ((String) key).replace("__SYM", "");
						PrimitiveStatement p = getStatement(newTest, name);
						// logger.warn("New double value for " + name + " is " +
						// value);
						assert(p != null) : "Could not find variable " + name + " in test: " + newTest.toCode()
								+ " / Orig test: " + test.toCode() + ", seed: " + Randomness.getSeed();

						if (p.getValue().getClass().equals(Double.class))
							p.setValue(value);
						else if (p.getValue().getClass().equals(Float.class))
							p.setValue(value.floatValue());
						else
							logger.warn("New value is of an unsupported type: " + val);
					} else {
						logger.debug("New value is of an unsupported type: " + val);
					}
				} else {
					logger.debug("New value is null");

				}
			}
			return newTest;
		}

	}

	/**
	 * Get the statement that defines this variable
	 * 
	 * @param test
	 * @param name
	 * @return
	 */
	private PrimitiveStatement<?> getStatement(TestCase test, String name) {
		for (Statement statement : test) {

			if (statement instanceof PrimitiveStatement<?>) {
				if (statement.getReturnValue().getName().equals(name))
					return (PrimitiveStatement<?>) statement;
			}
		}
		return null;
	}

	/**
	 * Apply cone of influence reduction to constraints with respect to the last
	 * constraint in the list
	 * 
	 * @param constraints
	 * @return
	 */
	private List<Constraint<?>> reduce(List<Constraint<?>> constraints) {

		Constraint<?> target = constraints.get(constraints.size() - 1);
		Set<Variable<?>> dependencies = getVariables(target);

		LinkedList<Constraint<?>> coi = new LinkedList<Constraint<?>>();
		if (dependencies.size() <= 0)
			return coi;

		coi.add(target);

		for (int i = constraints.size() - 2; i >= 0; i--) {
			Constraint<?> constraint = constraints.get(i);
			Set<Variable<?>> variables = getVariables(constraint);
			for (Variable<?> var : dependencies) {
				if (variables.contains(var)) {
					dependencies.addAll(variables);
					coi.addFirst(constraint);
					break;
				}
			}
		}
		return coi;
	}

	/**
	 * Determine the set of variable referenced by this constraint
	 * 
	 * @param constraint
	 * @return
	 */
	private Set<Variable<?>> getVariables(Constraint<?> constraint) {
		Set<Variable<?>> variables = new HashSet<Variable<?>>();
		getVariables(constraint.getLeftOperand(), variables);
		getVariables(constraint.getRightOperand(), variables);
		return variables;
	}

	/**
	 * Recursively determine constraints in expression
	 * 
	 * @param expr
	 *            a {@link org.evosuite.symbolic.expr.Expression} object.
	 * @param variables
	 *            a {@link java.util.Set} object.
	 */
	public static void getVariables(Expression<?> expr, Set<Variable<?>> variables) {
		variables.addAll(expr.getVariables());
	}

	private double getFitness(TestSuiteChromosome suite) {
		for (FitnessFunction<? extends Chromosome> ff : objective.getFitnessFunctions()) {
			TestSuiteFitnessFunction tff = (TestSuiteFitnessFunction) ff;
			tff.getFitness(suite);
		}
		return suite.getFitness();
	}

	/**
	 * Attempt to negate individual branches until budget is used up, or there
	 * are no further branches to negate
	 * 
	 * @param individual
	 */
	public boolean applyDSE(TestSuiteChromosome individual) {
		logger.info("[DSE] Current test suite: " + individual.toString());

		boolean wasSuccess = false;
		// expansion already happens as part of LS
		// TestSuiteChromosome expandedTests = expandTestSuite(individual);
		TestSuiteChromosome expandedTests = individual.clone();
		createPathConstraints(expandedTests);
		// fitness.getFitness(expandedTests);

		double originalFitness = getFitness(individual);

		while (hasNextBranchCondition() && !LocalSearchBudget.getInstance().isFinished()) {
			logger.info("Branches remaining: " + unsolvedBranchConditions.size());

			TestBranchPair next = getNextBranchCondition();
			BranchCondition branch = next.branch;

			TestCase newTest = negateCondition(branch.getReachingConstraints(), branch.getLocalConstraint(),
					next.test.getTestCase());

			if (newTest != null) {
				logger.info("Found new test: " + newTest.toCode());
				TestChromosome newTestChromosome = new TestChromosome();
				newTestChromosome.setTestCase(newTest);
				expandedTests.addTest(newTestChromosome);

				if (Properties.DSE_KEEP_ALL_TESTS) {
					updatePathConstraints(newTestChromosome);
					calculateUncoveredBranches();
					individual.addTest(newTest);
					wasSuccess = true;
				} else {

					if (getFitness(expandedTests) < originalFitness) {
						logger.info("New test improves fitness to {}", getFitness(expandedTests));
						DSEStats.reportNewTestUseful();
						wasSuccess = true;

						// no need to clone so we can keep executionresult
						updatePathConstraints(newTestChromosome);
						calculateUncoveredBranches(newTestChromosome);
						individual.addTest(newTest);
						originalFitness = getFitness(expandedTests);
						// TODO: Cancel on fitness 0 - would need to know if
						// ZeroFitness is a stopping condition
					} else {
						logger.info("New test does not improve fitness");
						DSEStats.reportNewTestUnuseful();
						expandedTests.deleteTest(newTest);
					}
				}
				success++;
			} else {
				unsolvableBranchConditions.add(branch);
				failed++;
				logger.info("Failed to find new test.");
			}
		}
		logger.info("Finished DSE");
		getFitness(individual); // Ensure fitness values are up to date.
		LocalSearchBudget.getInstance().countLocalSearchOnTestSuite();

		return wasSuccess;
	}

	private void calculateUncoveredBranches(TestChromosome newTestChromosome) {

		if (Properties.DSE_NEGATE_ALL_CONDITIONS == true) {
			for (BranchCondition branchCondition : branchConditions.get(newTestChromosome)) {
				if (!unsolvableBranchConditions.contains(branchCondition)) {
					unsolvedBranchConditions.add(new TestBranchPair(newTestChromosome, branchCondition));
				}
			}
		} else {
			calculateUncoveredBranches();
		}
	}

}
