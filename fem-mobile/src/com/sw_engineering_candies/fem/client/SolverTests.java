package com.sw_engineering_candies.fem.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SolverTests {

	@Test
	public void solve_cantileveredBeam10Nodes() {
		System.out.println("\nsolve_cantileveredBeam10Nodes");

		// ARRANGE
		final Solver model = new Solver();
		model.setYoungsModulus(210000);
		model.setPoissonRatio(0.28);
		model.setThickness(50.0);
		final int length = 100;
		final int hight = 20;
		final int cols = 10;
		final int rows = 5;
		final double force = 1234;
		final String inputModel = ModelFactory.createDefaultModel(length, hight, cols, rows, force, false).toString();
		model.createModel(inputModel);

		// ACT
		model.solve(null);
//		model.getJSON();

		// ASSERT
		assertEquals(0.05030161207750177, model.getSolutionNodeDisplacementY(cols * rows), 0.00000001);
	}


}
