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

/* The symmetric banded matrix ( '-' indicates zero values):
 *
 * |  a0  a1  a2   -    -  |
 * |  a1  a3  a4  a5    -  |
 * |  a2  a4  a6  a7   a8  |
 * |  -   a5  a7  a9   a10 |
 * |  -   -   a8  a10  a11 |
 *
 * is managed internal in the full band matrix format:
 *
 * |   -   -    a0   a1    a2 |
 * |   -  a1    a3   a4    a5 |
 * |  a2  a4    a6   a7    a8 |
 * |  a5  a7    a9   a10    - |
 * |  a8  a10  a11     -    - |
 *
 * is stored as array:
 *
 * [ -, -, a0, a1, a2,   -, a1, a3, a4, a5,    a2, a4, a6, a7, a8,   a5, a7, a9, a10, -,  a8, a10,  a11, -, - ]
 *
 */
public final class BandMatrixFull {

	private final int rows;

	private final int cols;

	private final double[] values;

	public BandMatrixFull(final int rowsNumber, final int bandwidth) {
		rows = rowsNumber;
		cols = bandwidth;
		values = new double[rows * cols];
	}

	public BandMatrixFull(final BandMatrixFull value) {
		this(value.rows, value.cols);
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				this.values[getIndex(row, col)] = value.values[getIndex(row, col)];
			}
		}
	}

	public void times(final Vector b, final Vector result) {

		// prepare input parameter
		final int rowStart = 0;
		final int rowEnd = rows;
		final int colMaximum = cols;
		final int rowMaximum = rows;
		final int bandwidthMid = colMaximum >> 1;

			// execute band matrix multiplication
			int index = 0;
			int rowOffset = 0;
			double sum = 0.0;
			for (int row = rowStart; row < rowEnd; row++) {
				rowOffset = row * colMaximum;
				sum = 0.0;
				for (int col = 0; col < colMaximum; col++) {
					index = row - bandwidthMid + col;
					if (index < rowMaximum && index >= 0) {
						sum += values[col + rowOffset] * b.values[index];
					}
				}
				result.values[row] = sum;
			}
	}

	public void times(final BandMatrixFull B, final BandMatrixFull result) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = i; j < this.rows; j++) { // just the upper part of the matrix
				for (int k = 0; k < this.rows; k++) {
					result.setValue(i, j, result.getValue(i, j) + this.getValue(i, k) * B.getValue(k, j));
				}
			}
		}
	}

	private int getIndex(final int row, final int col) {
		if (row >= 0 && col >= 0 && row < rows && col < cols) {
			return col + row * cols;
		} else {
			return -1;
		}
	}

	public void setValue(final int row, final int col, final double value) {
		final int indexUpperBand = getIndex(row, (cols >> 1) + col - row);
		if (indexUpperBand >= 0 && indexUpperBand < values.length) {
			values[indexUpperBand] = value;
		}
		if (row < col) {
			final int indexLowerBand = getIndex(col, (cols >> 1) - (col - row));
			if (indexLowerBand >= 0 && indexLowerBand < values.length) {
				values[indexLowerBand] = value;
			}
		}
	}

	public double getValue(final int row, final int col) {
		final int indexUpperBand = getIndex(row, (cols >> 1) + col - row);
		if (indexUpperBand >= 0 && indexUpperBand < values.length) {
			return values[indexUpperBand];
		}
		if (row < col) {
			final int indexLowerBand = getIndex(col, (cols >> 1) - (col - row));
			if (indexLowerBand >= 0 && indexLowerBand < values.length) {
				return values[indexLowerBand];
			}
		}
		return 0.0;
	}

	public static Vector solve(final BandMatrixFull A, final Vector b, final int maxNumberOfIterations) {

		// create local variables
		int i = -1;
		double rsnew = 1.0;
		double alpha = 0.0;
		final int numberOfEquations = b.getMaxRows();
		final Vector Ap = new Vector(numberOfEquations);
		final Vector x = new Vector(numberOfEquations);

		// r = b - A * x
		final Vector r = new Vector(b);
		final Vector temp = new Vector(numberOfEquations);
		A.times(x, temp);
		b.minus(temp, r);

		// p = r
		final Vector p = new Vector(r);

		// rsold = r' * r
		double rsold = r.dotProduct(r);

		while (i++ < maxNumberOfIterations && rsnew > 1e-21) {
			// Ap = A * p
			A.times(p, Ap);

			// alpha = rsold / ( p' * Ap )
			alpha = rsold / p.dotProduct(Ap);

			// x = x + alpha * p
			p.multi(alpha, temp);
			x.plus(temp, x);

			// r = r - alpha * Ap
			Ap.multi(alpha, temp);
			r.minus(temp, r);

			// rsnew = r' * r
			rsnew = r.dotProduct(r);

			// p = r + rsnew / rsold * p
			p.multi(rsnew / rsold, temp);
			r.plus(temp, p);

			// rsold = rsnew
			rsold = rsnew;
		}

		return x;
	}

	public int getMaxRows() {
		return rows;
	}

	public int getMaxCols() {
		return cols;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BandMatrix [rows=" + rows + ", cols=" + cols + "\n");
		for (int row = 0; row < rows /* && row < 10 */; row++) {
			for (int col = 0; col < rows; col++) {
				builder.append(Math.round(1000 * getValue(row, col)) / 1000.0).append(" ");
			}
			builder.append("\n");
		}
		builder.append("]");
		return builder.toString();
	}

}