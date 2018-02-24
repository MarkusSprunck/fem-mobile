package com.sw_engineering_candies.fem.client;

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
 *
 * 
 */

final public class Matrix {

	final int row;

	final int col;

	final double[][] matrix;

	public Matrix(int row, int col) {
		this.row = row;
		this.col = col;
		matrix = new double[row][col];
	}

	public Matrix(Matrix A) {
		this(A.matrix);
	}

	public Matrix(double[][] matrix) {
		row = matrix.length;
		col = matrix[0].length;
		this.matrix = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				this.matrix[i][j] = matrix[i][j];
			}
		}
	}

	public Matrix(double[] vector) {
		row = 1;
		col = vector.length;
		this.matrix = new double[row][col];
		for (int j = 0; j < col; j++) {
			this.matrix[0][j] = vector[j];
		}
	}

	// return C = A + B
	public Matrix plus(Matrix B) {
		final Matrix A = this;
		if (B.row != A.row || B.col != A.col) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}
		final Matrix C = new Matrix(row, col);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.matrix[i][j] = A.matrix[i][j] + B.matrix[i][j];
			}
		}
		return C;
	}

	// return C = A - B
	public Matrix minus(Matrix B) {
		final Matrix A = this;
		if (B.row != A.row || B.col != A.col) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}
		final Matrix C = new Matrix(row, col);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.matrix[i][j] = A.matrix[i][j] - B.matrix[i][j];
			}
		}
		return C;
	}

	public double getValue(int row, int col) {
		return matrix[row][col];
	}

	public double[][] getData() {
		return matrix;
	}

	// swap rows i and j
	void swap(int i, int j) {
		final double[] temp = matrix[i];
		matrix[i] = matrix[j];
		matrix[j] = temp;
	}

	// create and return the transpose of the invoking matrix
	public Matrix transpose() {
		final Matrix A = new Matrix(col, row);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				A.matrix[j][i] = this.matrix[i][j];
			}
		}
		return A;
	}

	// does A = B exactly?
	public boolean isEqual(Matrix B) {
		final Matrix A = this;
		if (B.row != A.row || B.col != A.col) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (A.matrix[i][j] != B.matrix[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	// return C = A * B
	public Matrix times(Matrix B) {
		final Matrix A = this;
		if (A.col != B.row) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}
		final Matrix C = new Matrix(A.row, B.col);
		for (int i = 0; i < C.row; i++) {
			for (int j = 0; j < C.col; j++) {
				for (int k = 0; k < A.col; k++) {
					C.matrix[i][j] += A.matrix[i][k] * B.matrix[k][j];
				}
			}
		}
		return C;
	}

	// return x = A^-1 b, assuming A is square and has full rank
	public Matrix solve(Matrix rhs) {
		if (row != col || rhs.row != col || rhs.col != 1) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}

		// create copies of the data
		final Matrix A = new Matrix(this);
		final Matrix b = new Matrix(rhs);

		// Gaussian elimination with partial pivoting
		for (int i = 0; i < col; i++) {

			// find pivot row and swap
			int max = i;
			for (int j = i + 1; j < col; j++) {
				if (Math.abs(A.matrix[j][i]) > Math.abs(A.matrix[max][i])) {
					max = j;
				}
			}
			A.swap(i, max);
			b.swap(i, max);

			// singular
			if (A.matrix[i][i] == 0.0) {
				throw new RuntimeException("Matrix is singular.");
			}

			// pivot within b
			for (int j = i + 1; j < col; j++) {
				b.matrix[j][0] -= b.matrix[i][0] * A.matrix[j][i] / A.matrix[i][i];
			}

			// pivot within A
			for (int j = i + 1; j < col; j++) {
				final double m = A.matrix[j][i] / A.matrix[i][i];
				for (int k = i + 1; k < col; k++) {
					A.matrix[j][k] -= A.matrix[i][k] * m;
				}
				A.matrix[j][i] = 0.0;
			}
		}

		// back substitution
		final Matrix x = new Matrix(col, 1);
		for (int j = col - 1; j >= 0; j--) {
			double t = 0.0;
			for (int k = j + 1; k < col; k++) {
				t += A.matrix[j][k] * x.matrix[k][0];
			}
			x.matrix[j][0] = (b.matrix[j][0] - t) / A.matrix[j][j];
		}
		return x;

	}

	public void setValue(int row, int col, double value) {
		matrix[row][col] = value;
	}

	public Matrix mult(double alpha) {
		final Matrix A = this;
		final Matrix C = new Matrix(row, col);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.matrix[i][j] = A.matrix[i][j] * alpha;
			}
		}
		return C;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Matrix [rows=" + row + ", cols=" + col + "\n");
		for (int c = 0; c < col; c++) {
			for (int r = 0; r < row; r++) {
				builder.append(Math.round(1000 * getValue(r, c)) / 1000.0).append(" ");
			}
			builder.append("\n");
		}
		builder.append("]");
		return builder.toString();
	}
}