/**
 * Copyright (C) 2012-2013, Markus Sprunck
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
 *
 * 
 */

package com.sw_engineering_candies.fem.client;

/**
 * This class xxx
 * 
 * @author Markus Sprunck
 * 
 */
final public class MatrixBanded {

	private final int row;

	private final int col;

	private final double[][] matrix;

	public MatrixBanded(double[][] matrix) {
		if (matrix == null || matrix.length <= 0 || matrix[0].length <= 0) {
			throw new RuntimeException("Illegal parameter.");
		}

		row = matrix.length;
		col = matrix[0].length;
		this.matrix = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				this.matrix[i][j] = matrix[i][j];
			}
		}
	}

	public Matrix times(Matrix B) {
		final MatrixBanded A = this;
		if (A.row != B.row || B.col != 1) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}

		final Matrix C = new Matrix(A.row, 1);
		for (int i = 0; i < A.row; i++) {
			for (int k = i - A.col; k - i < A.col; k++) {
				if (k >= 0 && k < B.row) {
					C.matrix[i][0] += getValue(A, i, k) * B.matrix[k][0];
				}
			}
		}
		return C;
	}

	public static double getValue(final MatrixBanded bandmatrix, int row, int col) {
		if (col == row) {
			return bandmatrix.matrix[row][0];
		} else if (row > col) {
			return getValue(bandmatrix, col, row);
		} else if (col - row < bandmatrix.col) {
			return bandmatrix.matrix[row][col - row];
		}

		return 0.0;
	}

	public static void setValue(final MatrixBanded bandmatrix, int row, int col, double value) {
		if (col == row) {
			bandmatrix.matrix[row][0] = value;
		} else if (row > col) {
			setValue(bandmatrix, col, row, value);
		} else if (col - row < bandmatrix.col) {
			bandmatrix.matrix[row][col - row] = value;
		}
	}

	public static Matrix solveConjugateGradient(final MatrixBanded A, final Matrix b, final double[] initial, int maxNumberItterations) {
		final double start = System.currentTimeMillis();

		Matrix x = (initial != null) ? new Matrix(initial).transpose() : new Matrix(b.row, b.col);
		Matrix r = b.minus(A.times(x));
		Matrix p = new Matrix(r);
		double rsold = r.transpose().times(r).getData()[0][0];
		int i;
		for (i = 1; i < maxNumberItterations; i++) {
			final Matrix Ap = A.times(p);
			final double alpha = rsold / p.transpose().times(Ap).getData()[0][0];
			x = x.plus(p.mult(alpha));
			r = r.minus(Ap.mult(alpha));
			final double rsnew = r.transpose().times(r).getData()[0][0];
			final double beta = rsnew / rsold;
			if (rsnew < 1e-5) {
				break;
			}
			p = r.plus(p.mult(beta));
			rsold = rsnew;
		}

		final double end = System.currentTimeMillis();
		System.out.println("conjugate gradient ready [" + (end - start) + "ms, itterations=" + i + "]");

		return x;
	}
}