/**
 * Copyright (C) 2012-2015, Markus Sprunck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote
 *   products derived from this software without specific prior
 *   written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sw_engineering_candies.fem.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BandMatrixFullTest {

	@Test
	public void v3_times_5x5_SymmetricBandMatrix_CorrectResult() {

		// ARRANGE
		final int rowsNumber = 5;
		final int bandwidth = 5;

		/*
		 * |  10   11    12    -    - |
		 * |  11   13    14   15    - |
		 * |  12   14    16   17   18 |
		 * |  -    15    17   19   20 |
		 * |  -    -     18   20   21 |
		 */
		final BandMatrixFull A = new BandMatrixFull(rowsNumber, bandwidth);
		A.setValue(0, 0, 10.0f);
		A.setValue(0, 1, 11.0f);
		A.setValue(0, 2, 12.0f);
		A.setValue(1, 1, 13.0f);
		A.setValue(1, 2, 14.0f);
		A.setValue(1, 3, 15.0f);
		A.setValue(2, 2, 16.0f);
		A.setValue(2, 3, 17.0f);
		A.setValue(2, 4, 18.0f);
		A.setValue(3, 3, 19.0f);
		A.setValue(3, 4, 20.0f);
		A.setValue(4, 4, 21.0f);

		final Vector b = new Vector(rowsNumber);
		b.setValue(0, 1);
		b.setValue(1, 2);
		b.setValue(2, 3);
		b.setValue(3, 4);
		b.setValue(4, 5);

		// ACT
		final Vector x = new Vector(rowsNumber);
		A.times(b, x);

		// ASSERT
		Assert.assertEquals(68.0f, x.getValue(0), 0.0f);
		Assert.assertEquals(139.0f, x.getValue(1), 0.0f);
		Assert.assertEquals(246.0f, x.getValue(2), 0.0f);
		Assert.assertEquals(257.0f, x.getValue(3), 0.0f);
		Assert.assertEquals(239.0f, x.getValue(4), 0.0f);
	}

	@Test
	public void v3_times_7x5_SymmetricBandMatrix_CorrectResult() {

		// ARRANGE
		final int rowsNumber = 7;
		final int bandwidth = 5;

		/*
		 * |  10   11    12    -     -     -    -  |
		 * |  11   13    14    15    -     -    -  |
		 * |  12   14    16    17    18    -    -  |
		 * |  -    15    17    19    20    21   -  |
		 * |  -    -     18    20    22    23   24 |
		 * |  -    -     -     21    23    25   26 |
		 * |  -    -     -     -     24    26   27 |
		 */
		final BandMatrixFull A = new BandMatrixFull(rowsNumber, bandwidth);
		A.setValue(0, 0, 10.0f);
		A.setValue(0, 1, 11.0f);
		A.setValue(0, 2, 12.0f);
		A.setValue(1, 1, 13.0f);
		A.setValue(1, 2, 14.0f);
		A.setValue(1, 3, 15.0f);
		A.setValue(2, 2, 16.0f);
		A.setValue(2, 3, 17.0f);
		A.setValue(2, 4, 18.0f);
		A.setValue(3, 3, 19.0f);
		A.setValue(3, 4, 20.0f);
		A.setValue(3, 5, 21.0f);
		A.setValue(4, 4, 22.0f);
		A.setValue(4, 5, 23.0f);
		A.setValue(4, 6, 24.0f);
		A.setValue(5, 5, 25.0f);
		A.setValue(5, 6, 26.0f);
		A.setValue(6, 6, 27.0f);

		final Vector b = new Vector(rowsNumber);
		b.setValue(0, 1);
		b.setValue(1, 2);
		b.setValue(2, 3);
		b.setValue(3, 4);
		b.setValue(4, 5);
		b.setValue(5, 6);
		b.setValue(6, 7);

		// ACT
		final Vector x = new Vector(rowsNumber);
		A.times(b, x);

		// ASSERT
		Assert.assertEquals(68.0f, x.getValue(0), 0.0f);
		Assert.assertEquals(139.0f, x.getValue(1), 0.0f);
		Assert.assertEquals(246.0f, x.getValue(2), 0.0f);
		Assert.assertEquals(383.0f, x.getValue(3), 0.0f);
		Assert.assertEquals(550.0f, x.getValue(4), 0.0f);
		Assert.assertEquals(531.0f, x.getValue(5), 0.0f);
		Assert.assertEquals(465.0f, x.getValue(6), 0.0f);
	}

	@Test
	public void times_5x5_SymmetricBandMatrix_CorrectResult() {

		// ARRANGE
		final int rows = 5;
		final int bandwidth = 5;
		final BandMatrixFull A = new BandMatrixFull(rows, bandwidth);
		A.setValue(0, 0, 10.0f);
		A.setValue(0, 1, 11.0f);
		A.setValue(0, 2, 12.0f);
		A.setValue(1, 1, 13.0f);
		A.setValue(1, 2, 14.0f);
		A.setValue(1, 3, 15.0f);
		A.setValue(2, 2, 16.0f);
		A.setValue(2, 3, 17.0f);
		A.setValue(2, 4, 18.0f);
		A.setValue(3, 3, 19.0f);
		A.setValue(3, 4, 20.0f);
		A.setValue(4, 4, 21.0f);

		final Vector b = new Vector(rows);
		b.setValue(0, 1);
		b.setValue(1, 2);
		b.setValue(2, 3);
		b.setValue(3, 4);
		b.setValue(4, 5);

		// ACT
		final Vector x = new Vector(b.getMaxRows());
		A.times(b, x);

		// ASSERT
		Assert.assertEquals(68.0f, x.getValue(0), 0.0);
		Assert.assertEquals(139.0f, x.getValue(1), 0.0);
		Assert.assertEquals(246.0f, x.getValue(2), 0.0);
		Assert.assertEquals(257.0f, x.getValue(3), 0.0);
		Assert.assertEquals(239.0f, x.getValue(4), 0.0);
	}

	@Test
	public void times_7x5_SymmetricBandMatrix_CorrectResult() {

		// ARRANGE
		final int rows = 7;
		final int bandwidth = 5;
		// | 10 11 12  -  -  -  - |
		// | 11 13 14 15  -  -  - |
		// | 12 14 16 17 18  -  - |
		// |  - 15 17 19 20 21  - |
		// |  -  - 18 20 22 23 24 |
		// |  -  -  - 21 23 25 26 |
		// |  -  -  -  - 24 26 27 |
		final BandMatrixFull A = new BandMatrixFull(rows, bandwidth);
		A.setValue(0, 0, 10.0f);
		A.setValue(0, 1, 11.0f);
		A.setValue(0, 2, 12.0f);
		A.setValue(1, 1, 13.0f);
		A.setValue(1, 2, 14.0f);
		A.setValue(1, 3, 15.0f);
		A.setValue(2, 2, 16.0f);
		A.setValue(2, 3, 17.0f);
		A.setValue(2, 4, 18.0f);
		A.setValue(3, 3, 19.0f);
		A.setValue(3, 4, 20.0f);
		A.setValue(3, 5, 21.0f);
		A.setValue(4, 4, 22.0f);
		A.setValue(4, 5, 23.0f);
		A.setValue(4, 6, 24.0f);
		A.setValue(5, 5, 25.0f);
		A.setValue(5, 6, 26.0f);
		A.setValue(6, 6, 27.0f);

		final Vector b = new Vector(rows);
		b.setValue(0, 1);
		b.setValue(1, 2);
		b.setValue(2, 3);
		b.setValue(3, 4);
		b.setValue(4, 5);
		b.setValue(5, 6);
		b.setValue(6, 7);

		// ACT
		final Vector x = new Vector(b.getMaxRows());
		A.times(b, x);

		// ASSERT
		Assert.assertEquals(68.0f, x.getValue(0), 0.0);
		Assert.assertEquals(139.0f, x.getValue(1), 0.0);
		Assert.assertEquals(246.0f, x.getValue(2), 0.0);
		Assert.assertEquals(383.0f, x.getValue(3), 0.0);
		Assert.assertEquals(550.0f, x.getValue(4), 0.0);
		Assert.assertEquals(531.0f, x.getValue(5), 0.0);
		Assert.assertEquals(465.0f, x.getValue(6), 0.0);
	}

	@Test
	public void constructor_7x5_SymmetricBandMatrix_CorrectResult() {

		// ARRANGE
		final int rows = 7;
		final int bandwidth = 5;
		// | 10 11 12  -  -  -  - |
		// | 11 13 14 15  -  -  - |
		// | 12 14 16 17 18  -  - |
		// |  - 15 17 19 20 21  - |
		// |  -  - 18 20 22 23 24 |
		// |  -  -  - 21 23 25 26 |
		// |  -  -  -  - 24 26 27 |
		final BandMatrixFull A = new BandMatrixFull(rows, bandwidth);
		A.setValue(0, 0, 10.0f);
		A.setValue(0, 1, 11.0f);
		A.setValue(0, 2, 12.0f);
		A.setValue(1, 1, 13.0f);
		A.setValue(1, 2, 14.0f);
		A.setValue(1, 3, 15.0f);
		A.setValue(2, 2, 16.0f);
		A.setValue(2, 3, 17.0f);
		A.setValue(2, 4, 18.0f);
		A.setValue(3, 3, 19.0f);
		A.setValue(3, 4, 20.0f);
		A.setValue(3, 5, 21.0f);
		A.setValue(4, 4, 22.0f);
		A.setValue(4, 5, 23.0f);
		A.setValue(4, 6, 24.0f);
		A.setValue(5, 5, 25.0f);
		A.setValue(5, 6, 26.0f);
		A.setValue(6, 6, 27.0f);

		// ACT
		final BandMatrixFull B = new BandMatrixFull(A);

		// ASSERT
		Assert.assertEquals(B.getValue(0, 0), 10.0f, 0.0);
		Assert.assertEquals(B.getValue(0, 1), 11.0f, 0.0);
		Assert.assertEquals(B.getValue(0, 2), 12.0f, 0.0);
		Assert.assertEquals(B.getValue(0, 3), 0.0f, 0.0);
		Assert.assertEquals(B.getValue(0, 4), 0.0f, 0.0);
		Assert.assertEquals(B.getValue(0, 5), 0.0f, 0.0);
		Assert.assertEquals(B.getValue(0, 6), 0.0f, 0.0);

		Assert.assertEquals(B.getValue(1, 0), 11.0f, 0.0);
		Assert.assertEquals(B.getValue(1, 1), 13.0f, 0.0);
		Assert.assertEquals(B.getValue(1, 2), 14.0f, 0.0);
		Assert.assertEquals(B.getValue(1, 3), 15.0f, 0.0);
		Assert.assertEquals(B.getValue(2, 2), 16.0f, 0.0);
		Assert.assertEquals(B.getValue(2, 3), 17.0f, 0.0);
		Assert.assertEquals(B.getValue(2, 4), 18.0f, 0.0);
		Assert.assertEquals(B.getValue(3, 3), 19.0f, 0.0);
		Assert.assertEquals(B.getValue(3, 4), 20.0f, 0.0);
		Assert.assertEquals(B.getValue(3, 5), 21.0f, 0.0);
		Assert.assertEquals(B.getValue(4, 4), 22.0f, 0.0);
		Assert.assertEquals(B.getValue(4, 5), 23.0f, 0.0);
		Assert.assertEquals(B.getValue(4, 6), 24.0f, 0.0);
		Assert.assertEquals(B.getValue(5, 5), 25.0f, 0.0);
		Assert.assertEquals(B.getValue(5, 6), 26.0f, 0.0);
		Assert.assertEquals(B.getValue(6, 6), 27.0f, 0.0);
		Assert.assertEquals(A.getMaxCols(), B.getMaxCols(), 0.0);
		Assert.assertEquals(A.getMaxRows(), B.getMaxRows(), 0.0);
	}

	@Test
	public void toString_5x5_SymmetricBandMatrix_CorrectResult() {

		// ARRANGE
		final int rows = 5;
		final int bandwidth = 5;
		// | 10 11 12  -  - |
		// | 11 13 14 15  - |
		// | 12 14 16 17 18 |
		// |  - 15 17 19 20 |
		// |  -  - 18 20 21 |
		final BandMatrixFull A = new BandMatrixFull(rows, bandwidth);
		A.setValue(0, 0, 10.0f);
		A.setValue(0, 1, 11.0f);
		A.setValue(0, 2, 12.0f);
		A.setValue(1, 1, 13.0f);
		A.setValue(1, 2, 14.0f);
		A.setValue(1, 3, 15.0f);
		A.setValue(2, 2, 16.0f);
		A.setValue(2, 3, 17.0f);
		A.setValue(2, 4, 18.0f);
		A.setValue(3, 3, 19.0f);
		A.setValue(3, 4, 20.0f);
		A.setValue(4, 4, 21.0f);

		// ACT
		final String result = A.toString();

		// ASSERT
		final String expected = "BandMatrixFull [rows=5, cols=5\n" //
				+ "10.0 11.0 12.0 0.0 0.0 \n" //
				+ "11.0 13.0 14.0 15.0 0.0 \n" //
				+ "12.0 14.0 16.0 17.0 18.0 \n" //
				+ "0.0 15.0 17.0 19.0 20.0 \n" //
				+ "0.0 0.0 18.0 20.0 21.0 \n" //
				+ "]";
		Assert.assertEquals(expected, result);
	}

	@Test
	public void times_5x5_diagonalMatrix_CorrectResult() {

		// ARRANGE
		final int rows = 5;
		final int bandwidth = 5;
		// | 10 11 12  -  - |
		// | 11 13 14 15  - |
		// | 12 14 16 17 18 |
		// |  - 15 17 19 20 |
		// |  -  - 18 20 21 |
		final BandMatrixFull A = new BandMatrixFull(rows, bandwidth);
		A.setValue(0, 0, 10.0f);
		A.setValue(0, 1, 11.0f);
		A.setValue(0, 2, 12.0f);
		A.setValue(1, 1, 13.0f);
		A.setValue(1, 2, 14.0f);
		A.setValue(1, 3, 15.0f);
		A.setValue(2, 2, 16.0f);
		A.setValue(2, 3, 17.0f);
		A.setValue(2, 4, 18.0f);
		A.setValue(3, 3, 19.0f);
		A.setValue(3, 4, 20.0f);
		A.setValue(4, 3, 20.0f);
		A.setValue(4, 4, 21.0f);

		final BandMatrixFull B = new BandMatrixFull(rows, 1);
		B.setValue(0, 0, 2.0f);
		B.setValue(1, 1, 2.0f);
		B.setValue(2, 2, 2.0f);
		B.setValue(3, 3, 2.0f);
		B.setValue(4, 4, 2.0f);

		// ACT
		final BandMatrixFull C = new BandMatrixFull(rows, bandwidth);
		A.times(B, C);

		// ASSERT
		for (int i = 0; i < A.getMaxRows(); i++) {
			for (int j = 0; j < A.getMaxRows(); j++) {
				Assert.assertEquals(A.getValue(i, j) * 2.0, C.getValue(i, j), 0.0);
			}
		}
	}

	@Test
	public void setter_5x5_diagonalMatrix_GetCorrectResult() {

		// ARRANGE
		final int rows = 5;
		final BandMatrixFull A = new BandMatrixFull(rows, rows * 2 - 1);

		// ACT
		for (int i = 0; i < A.getMaxRows(); i++) {
			for (int j = 0; j < A.getMaxRows(); j++) {
				final double value = (1 + i) + 0.001 * (j + 1);
				A.setValue(i, j, value);
			}
		}

		// ASSERT
		for (int i = 0; i < A.getMaxRows(); i++) {
			for (int j = 0; j < A.getMaxRows(); j++) {
				final double value = (1 + i) + 0.001 * (j + 1);
				Assert.assertEquals(value, A.getValue(i, j), 0.0);
			}
		}
	}

	@Test
	public void times_5x5_diagonalMatrixConditioning_CorrectResult() {

		// ARRANGE
		final int rows = 5;
		final int bandwidth = 5;
		// | 10 11 12  -  - |
		// | 11 13 14 15  - |
		// | 12 14 16 17 18 |
		// |  - 15 17 19 20 |
		// |  -  - 18 20 21 |
		final BandMatrixFull A = new BandMatrixFull(rows, bandwidth);
		A.setValue(0, 0, 10.0f);
		A.setValue(0, 1, 11.0f);
		A.setValue(0, 2, 12.0f);
		A.setValue(1, 1, 13.0f);
		A.setValue(1, 2, 14.0f);
		A.setValue(1, 3, 15.0f);
		A.setValue(2, 2, 16.0f);
		A.setValue(2, 3, 17.0f);
		A.setValue(2, 4, 18.0f);
		A.setValue(3, 3, 19.0f);
		A.setValue(3, 4, 20.0f);
		A.setValue(4, 3, 20.0f);
		A.setValue(4, 4, 21.0f);

		final BandMatrixFull B = new BandMatrixFull(rows, 1);
		B.setValue(0, 0, 1.0f / Math.sqrt(10f));
		B.setValue(1, 1, 1.0f / Math.sqrt(13f));
		B.setValue(2, 2, 1.0f / Math.sqrt(16f));
		B.setValue(3, 3, 1.0f / Math.sqrt(19f));
		B.setValue(4, 4, 1.0f / Math.sqrt(21f));

		// ACT
		final BandMatrixFull C1 = new BandMatrixFull(rows, bandwidth);
		A.times(B, C1);
		final BandMatrixFull C2 = new BandMatrixFull(rows, bandwidth);
		C1.times(B, C2);

		// ASSERT
		final double delta = 1E-15;
		Assert.assertEquals(1.0f, C2.getValue(0, 0), delta);
		Assert.assertEquals(1.0f, C2.getValue(1, 1), delta);
		Assert.assertEquals(1.0f, C2.getValue(2, 2), delta);
		Assert.assertEquals(1.0f, C2.getValue(3, 3), delta);
		Assert.assertEquals(1.0f, C2.getValue(4, 4), delta);
	}

	private Solver model;

	@Before
	public void setup() {
		final String inputModel = ModelFactory.createEiffelTowerModel(1, 1);
		model = new Solver();
		model.createModel(inputModel);
	}

	@Test
	public void solveConjugateGradient_EiffelTowerModel_SumOfForcesEqualsNull() {

		// ARRANGE
		final BandMatrixFull A = model.stiffnessRearanged;
		final Vector inputForces = model.caluculateInputForcesGravity(10, 20);

		// ACT
		final Vector x_result = BandMatrixFull.solve(A, inputForces, 400);
		final Vector forcesCalculated = new Vector(inputForces.getMaxRows());
		model.stiffness.times(x_result, forcesCalculated);

		// ASSERT
		double sum = 0.0;
		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < forcesCalculated.getMaxRows(); i++) {
			sum += forcesCalculated.getValue(i);
			max = Math.max(max, forcesCalculated.getValue(i));
			min = Math.min(min, forcesCalculated.getValue(i));
		}
		Assert.assertEquals(0.0, sum, 1E-6);
		Assert.assertEquals(2787.995099, max, 1E-6);
		Assert.assertEquals(-4961.661233, min, 1E-6);
	}

}