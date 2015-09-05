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

import java.util.HashMap;

public class Solver2 {

	// Thickness of 2D structure in mm
	private final double thickness = 10.0f;

	// Poisson's Ratio of material, e.g. steel=0.27–0.30
	private final double poissonRatio = 0.2f;

	// Young's Modulus of material in N/mm2
	private final double youngsModulus = 1.6E+05f;

	// Vector if input forces in N (size is 2*numberOfNodes for 2D)
	private Matrix inputForces;

	// Vector if input displacements in mm (size is 2*numberOfNodes for 2D)
	private Matrix inputDisplacements;

	// Stiffness matrix of all elements
	private MatrixBanded originalStiffnessMatrix;

	// Stiffness matrix with replaced of all known forces (see inputForces)
	private MatrixBanded rearangedStiffnessMatrix;

	// Resulting forces in N
	private double[] solutionForces;

	// Resulting displacements in mm
	private double[] solutionsDisplacements;

	// Average displacement of all nodes of a element in mm
	private double[] solutionsDisplacementsMeanX;

	// Expected band with for global stiffness matrix resulting form the max
	// node number distance in one element
	private int bandWidthExpected = 0;

	// Total number of nodes in the model
	private int numberOfNodes = 0;

	// Total number of elements in the model
	private int numberOfElements = 0;

	// Stores the node information for each element in the model
	private Node[][] nodesByElement;

	public void createModel(String input) {
		final double start = System.currentTimeMillis();

		bandWidthExpected = ModelUtil.createModelFromString(this, input);
		final double[][] stiffnesMatrix = createGlobalStiffnessMatrix();
		originalStiffnessMatrix = new MatrixBanded(stiffnesMatrix);
		rearangeGlobalStiffnesMatrix(stiffnesMatrix);
		rearangedStiffnessMatrix = new MatrixBanded(stiffnesMatrix);
		
		final double end = System.currentTimeMillis();
		System.out.println("stiffness matrix created [" + (end - start) + "ms]");
	}

	public void solve(double[] newInputForces) {

		if (null != newInputForces) {
			inputForces = new Matrix(newInputForces).transpose();
		}

		if (null == originalStiffnessMatrix && null == rearangedStiffnessMatrix) {
			System.out.print("solve -> ERROR");
			return;
		}

		final Matrix x = MatrixBanded.solveConjugateGradient(rearangedStiffnessMatrix, inputForces, solutionsDisplacements,
				100);
		solutionsDisplacements = x.transpose().getData()[0];
		final Matrix b2 = new Matrix(solutionsDisplacements).transpose();
		final Matrix x2 = originalStiffnessMatrix.times(b2).transpose();
		solutionForces = x2.getData()[0];
		calculateSolutionsDisplacementsMeanX();
	}

	private void calculateSolutionsDisplacementsMeanX() {
		for (int elementId = 1; elementId <= numberOfElements; elementId++) {
			final double deltaX1 = getSolutionNodeDisplacementX(nodesByElement[elementId][1].nodeID);
			final double deltaX2 = getSolutionNodeDisplacementX(nodesByElement[elementId][2].nodeID);
			final double deltaX3 = getSolutionNodeDisplacementX(nodesByElement[elementId][3].nodeID);
			solutionsDisplacementsMeanX[elementId - 1] = (deltaX3 + deltaX2 + deltaX1) / 3.0 * 100;
		}
	}

	private double calculateAreaOfElement(int elementId) {
		return 0.5 * ((-nodesByElement[elementId][2].x + nodesByElement[elementId][3].x)
				* (nodesByElement[elementId][1].y - nodesByElement[elementId][2].y) - (-nodesByElement[elementId][1].x + nodesByElement[elementId][2].x)
				* (nodesByElement[elementId][2].y - nodesByElement[elementId][3].y));
	}

	private double[][] B_matrix_berechnen(int elementId, double area) {

		// B_Matrix_type = ARRAY [1..6,1..3] OF real;
		final double[][] B_ = new double[7][4];

		// hilfs_vektor_type = ARRAY [1..3] OF real;
		final double[] a = new double[4];
		final double[] b = new double[4];
		final double[] c = new double[4];

		a[1] = nodesByElement[elementId][2].x * nodesByElement[elementId][3].y - nodesByElement[elementId][3].x
				* nodesByElement[elementId][2].y;
		a[2] = nodesByElement[elementId][3].x * nodesByElement[elementId][1].y - nodesByElement[elementId][1].x
				* nodesByElement[elementId][3].y;
		a[3] = nodesByElement[elementId][1].x * nodesByElement[elementId][2].y - nodesByElement[elementId][2].x
				* nodesByElement[elementId][1].y;
		b[1] = nodesByElement[elementId][2].y - nodesByElement[elementId][3].y;
		b[2] = nodesByElement[elementId][3].y - nodesByElement[elementId][1].y;
		b[3] = nodesByElement[elementId][1].y - nodesByElement[elementId][2].y;
		c[1] = -nodesByElement[elementId][2].x + nodesByElement[elementId][3].x;
		c[2] = -nodesByElement[elementId][3].x + nodesByElement[elementId][1].x;
		c[3] = -nodesByElement[elementId][1].x + nodesByElement[elementId][2].x;

		B_[1][1] = b[1] / (area * 2);
		B_[2][1] = 0;
		B_[3][1] = b[2] / (area * 2);
		B_[4][1] = 0;
		B_[5][1] = b[3] / (area * 2);
		B_[6][1] = 0;
		B_[1][2] = 0;
		B_[2][2] = c[1] / (area * 2);
		B_[3][2] = 0;
		B_[4][2] = c[2] / (area * 2);
		B_[5][2] = 0;
		B_[6][2] = c[3] / (area * 2);
		B_[1][3] = c[1] / (area * 2);
		B_[2][3] = b[1] / (area * 2);
		B_[3][3] = c[2] / (area * 2);
		B_[4][3] = b[2] / (area * 2);
		B_[5][3] = c[3] / (area * 2);
		B_[6][3] = b[3] / (area * 2);
		return B_;
	}

	// Ke_matrix_berechnen
	private double[][] Ke_matrix_berechnen(double[][] D, double[][] B, int k, double area) {
		// TYPE Hilfs_Matrix_type = ARRAY [1..3,1..6] OF real;
		final double[][] h = new double[4][7];

		// Ke_Matrix_type = ARRAY [1..6,1..6] OF real;
		final double[][] KElement = new double[7][7];

		final double varAreaThickness = area * thickness;
		h[1][1] = varAreaThickness * (B[1][1] * D[1][1] + B[1][2] * D[2][1] + B[1][3] * D[3][1]);
		h[2][1] = varAreaThickness * (B[1][1] * D[1][2] + B[1][2] * D[2][2] + B[1][3] * D[3][2]);
		h[3][1] = varAreaThickness * (B[1][1] * D[1][3] + B[1][2] * D[2][3] + B[1][3] * D[3][3]);
		h[1][2] = varAreaThickness * (B[2][1] * D[1][1] + B[2][2] * D[2][1] + B[2][3] * D[3][1]);
		h[2][2] = varAreaThickness * (B[2][1] * D[1][2] + B[2][2] * D[2][2] + B[2][3] * D[3][2]);
		h[3][2] = varAreaThickness * (B[2][1] * D[1][3] + B[2][2] * D[2][3] + B[2][3] * D[3][3]);
		h[1][3] = varAreaThickness * (B[3][1] * D[1][1] + B[3][2] * D[2][1] + B[3][3] * D[3][1]);
		h[2][3] = varAreaThickness * (B[3][1] * D[1][2] + B[3][2] * D[2][2] + B[3][3] * D[3][2]);
		h[3][3] = varAreaThickness * (B[3][1] * D[1][3] + B[3][2] * D[2][3] + B[3][3] * D[3][3]);
		h[1][4] = varAreaThickness * (B[4][1] * D[1][1] + B[4][2] * D[2][1] + B[4][3] * D[3][1]);
		h[2][4] = varAreaThickness * (B[4][1] * D[1][2] + B[4][2] * D[2][2] + B[4][3] * D[3][2]);
		h[3][4] = varAreaThickness * (B[4][1] * D[1][3] + B[4][2] * D[2][3] + B[4][3] * D[3][3]);
		h[1][5] = varAreaThickness * (B[5][1] * D[1][1] + B[5][2] * D[2][1] + B[5][3] * D[3][1]);
		h[2][5] = varAreaThickness * (B[5][1] * D[1][2] + B[5][2] * D[2][2] + B[5][3] * D[3][2]);
		h[3][5] = varAreaThickness * (B[5][1] * D[1][3] + B[5][2] * D[2][3] + B[5][3] * D[3][3]);
		h[1][6] = varAreaThickness * (B[6][1] * D[1][1] + B[6][2] * D[2][1] + B[6][3] * D[3][1]);
		h[2][6] = varAreaThickness * (B[6][1] * D[1][2] + B[6][2] * D[2][2] + B[6][3] * D[3][2]);
		h[3][6] = varAreaThickness * (B[6][1] * D[1][3] + B[6][2] * D[2][3] + B[6][3] * D[3][3]);

		KElement[1][1] = h[1][1] * B[1][1] + h[2][1] * B[1][2] + h[3][1] * B[1][3];
		KElement[2][1] = h[1][1] * B[2][1] + h[2][1] * B[2][2] + h[3][1] * B[2][3];
		KElement[3][1] = h[1][1] * B[3][1] + h[2][1] * B[3][2] + h[3][1] * B[3][3];
		KElement[4][1] = h[1][1] * B[4][1] + h[2][1] * B[4][2] + h[3][1] * B[4][3];
		KElement[5][1] = h[1][1] * B[5][1] + h[2][1] * B[5][2] + h[3][1] * B[5][3];
		KElement[6][1] = h[1][1] * B[6][1] + h[2][1] * B[6][2] + h[3][1] * B[6][3];

		KElement[1][2] = h[1][2] * B[1][1] + h[2][2] * B[1][2] + h[3][2] * B[1][3];
		KElement[2][2] = h[1][2] * B[2][1] + h[2][2] * B[2][2] + h[3][2] * B[2][3];
		KElement[3][2] = h[1][2] * B[3][1] + h[2][2] * B[3][2] + h[3][2] * B[3][3];
		KElement[4][2] = h[1][2] * B[4][1] + h[2][2] * B[4][2] + h[3][2] * B[4][3];
		KElement[5][2] = h[1][2] * B[5][1] + h[2][2] * B[5][2] + h[3][2] * B[5][3];
		KElement[6][2] = h[1][2] * B[6][1] + h[2][2] * B[6][2] + h[3][2] * B[6][3];

		KElement[1][3] = h[1][3] * B[1][1] + h[2][3] * B[1][2] + h[3][3] * B[1][3];
		KElement[2][3] = h[1][3] * B[2][1] + h[2][3] * B[2][2] + h[3][3] * B[2][3];
		KElement[3][3] = h[1][3] * B[3][1] + h[2][3] * B[3][2] + h[3][3] * B[3][3];
		KElement[4][3] = h[1][3] * B[4][1] + h[2][3] * B[4][2] + h[3][3] * B[4][3];
		KElement[5][3] = h[1][3] * B[5][1] + h[2][3] * B[5][2] + h[3][3] * B[5][3];
		KElement[6][3] = h[1][3] * B[6][1] + h[2][3] * B[6][2] + h[3][3] * B[6][3];

		KElement[1][4] = h[1][4] * B[1][1] + h[2][4] * B[1][2] + h[3][4] * B[1][3];
		KElement[2][4] = h[1][4] * B[2][1] + h[2][4] * B[2][2] + h[3][4] * B[2][3];
		KElement[3][4] = h[1][4] * B[3][1] + h[2][4] * B[3][2] + h[3][4] * B[3][3];
		KElement[4][4] = h[1][4] * B[4][1] + h[2][4] * B[4][2] + h[3][4] * B[4][3];
		KElement[5][4] = h[1][4] * B[5][1] + h[2][4] * B[5][2] + h[3][4] * B[5][3];
		KElement[6][4] = h[1][4] * B[6][1] + h[2][4] * B[6][2] + h[3][4] * B[6][3];

		KElement[1][5] = h[1][5] * B[1][1] + h[2][5] * B[1][2] + h[3][5] * B[1][3];
		KElement[2][5] = h[1][5] * B[2][1] + h[2][5] * B[2][2] + h[3][5] * B[2][3];
		KElement[3][5] = h[1][5] * B[3][1] + h[2][5] * B[3][2] + h[3][5] * B[3][3];
		KElement[4][5] = h[1][5] * B[4][1] + h[2][5] * B[4][2] + h[3][5] * B[4][3];
		KElement[5][5] = h[1][5] * B[5][1] + h[2][5] * B[5][2] + h[3][5] * B[5][3];
		KElement[6][5] = h[1][5] * B[6][1] + h[2][5] * B[6][2] + h[3][5] * B[6][3];

		KElement[1][6] = h[1][6] * B[1][1] + h[2][6] * B[1][2] + h[3][6] * B[1][3];
		KElement[2][6] = h[1][6] * B[2][1] + h[2][6] * B[2][2] + h[3][6] * B[2][3];
		KElement[3][6] = h[1][6] * B[3][1] + h[2][6] * B[3][2] + h[3][6] * B[3][3];
		KElement[4][6] = h[1][6] * B[4][1] + h[2][6] * B[4][2] + h[3][6] * B[4][3];
		KElement[5][6] = h[1][6] * B[5][1] + h[2][6] * B[5][2] + h[3][6] * B[5][3];
		KElement[6][6] = h[1][6] * B[6][1] + h[2][6] * B[6][2] + h[3][6] * B[6][3];
		return KElement;

	}

	private double[][] createGlobalStiffnessMatrix() {

		final double[][] D_Matrix_eben = new double[4][4];

		final double hilfsvariable = youngsModulus / (1 + poissonRatio) / (1 - 2 * poissonRatio);
		D_Matrix_eben[1][1] = (1 - poissonRatio) * hilfsvariable;
		D_Matrix_eben[2][1] = poissonRatio * hilfsvariable;
		D_Matrix_eben[3][1] = 0;
		D_Matrix_eben[1][2] = poissonRatio * hilfsvariable;
		D_Matrix_eben[2][2] = (1 - poissonRatio) * hilfsvariable;
		D_Matrix_eben[3][2] = 0;
		D_Matrix_eben[1][3] = 0;
		D_Matrix_eben[2][3] = 0;
		D_Matrix_eben[3][3] = (1 - 2 * poissonRatio) / 2 * hilfsvariable;

		final double[][] KB = new double[numberOfNodes * 2][bandWidthExpected];

		int bandWidthActual = 0;
		for (int elementID = 1; elementID <= numberOfElements; elementID++) {

			final double area = calculateAreaOfElement(elementID);
			final double[][] B_Matrix = B_matrix_berechnen(elementID, area);
			final double[][] K_e_ = Ke_matrix_berechnen(D_Matrix_eben, B_Matrix, elementID, area);

			for (int i = 1; i <= 3; i++) {
				for (int j = 1; j <= 3; j++) {
					final int Spalte = nodesByElement[elementID][i].nodeID * 2;
					final int Reihe = nodesByElement[elementID][j].nodeID * 2;

					final int NeueSpalte = Spalte - Reihe;
					if (bandWidthActual < Spalte - Reihe + 1) {
						bandWidthActual = Spalte - Reihe + 1;
					}

					if (NeueSpalte > -1) {
						KB[Reihe - 2][NeueSpalte] = KB[Reihe - 2][NeueSpalte] + K_e_[i * 2 - 1][j * 2 - 1];
						KB[Reihe - 2][NeueSpalte + 1] = KB[Reihe - 2][NeueSpalte + 1] + K_e_[i * 2][j * 2 - 1];
						KB[Reihe - 1][NeueSpalte] = KB[Reihe - 1][NeueSpalte] + K_e_[i * 2][j * 2];
						if (NeueSpalte > 0) {
							KB[Reihe - 1][NeueSpalte - 1] = KB[Reihe - 1][NeueSpalte - 1] + K_e_[i * 2 - 1][j * 2];
						}
					}
				}
			}
		}
		return KB;
	}

	private void rearangeGlobalStiffnesMatrix(double[][] KB) {
		int i, nodeID;
		int REI, reihe;
		final int bandbreite = KB[0].length;
		for (nodeID = 1; nodeID <= numberOfNodes; nodeID++) {
			// Jeder Knoten wird wenn die Verschiebung bekannt ist abgearbeitet
			if (isNodeVerticallyFixed(nodeID)) {
				REI = nodeID * 2 - 1;
				for (i = 1; i <= numberOfNodes; i++) {
					reihe = i * 2 - 1; // { reihe := 1..(num_zahl*2-1) }
					if (REI > reihe) // { Oberer Teil des Gleichungssystemes }
					{
						setNodeForceX(i, getNodeForceX(i) - getNodeDisplacementX(nodeID) * KB[reihe - 1][REI - reihe]);
						setNodeForceY(i, getNodeForceY(i) - getNodeDisplacementX(nodeID) * KB[reihe][REI - reihe - 1]);
						KB[reihe - 1][REI - reihe] = 0;
						KB[reihe][REI - reihe - 1] = 0;
					}

					if (REI <= reihe)// { unterer Teil des Gleichungssystemes }
					{
						if (reihe - REI + 1 <= bandbreite) {
							setNodeForceX(i, getNodeForceX(i) - getNodeDisplacementX(nodeID) * KB[REI - 1][reihe - REI]);
							setNodeForceY(i, getNodeForceY(i) - getNodeDisplacementX(nodeID) * KB[REI - 1][reihe - REI + 1]);
							KB[REI - 1][reihe - REI] = 0;
							KB[REI - 1][reihe - REI + 1] = 0;
						}
					}
				}
				KB[REI - 1][0] = 1;
				setNodeForceX(nodeID, getNodeDisplacementX(nodeID));
			}

			if (isNodeHorzontallyFixed(nodeID)) {
				REI = nodeID * 2;
				for (i = 1; i <= numberOfNodes; i++) {
					reihe = i * 2; // { reihe := 1..(num_zahl*2-1) }
					if (REI >= reihe) // { Oberer Teil des Gleichungssystemes }
					{
						if (REI - reihe < bandbreite) {
							setNodeForceX(i, getNodeForceX(i) - getNodeDisplacementY(nodeID) * KB[reihe - 2][REI - reihe + 1]);
							setNodeForceY(i, getNodeForceY(i) - getNodeDisplacementY(nodeID) * KB[reihe - 1][REI - reihe + 1]);
							KB[reihe - 2][REI - reihe + 1] = 0;
							KB[reihe - 1][REI - reihe] = 0;
						}
					}
					// oberer Teil des Gleichungssystemes
					if (REI == reihe) {
						KB[REI - 1][1] = 0;
					}
					// unterer Teil des Gleichungssystemes
					if (REI < reihe) {
						if (reihe - REI + 1 <= bandbreite) {
							setNodeForceX(i, getNodeForceX(i) - getNodeDisplacementY(nodeID) * KB[REI - 1][reihe - REI]);
							setNodeForceY(i, getNodeForceY(i) - getNodeDisplacementY(nodeID) * KB[REI - 1][reihe - REI + 1]);
							KB[REI - 1][reihe - REI] = 0;
							KB[REI - 1][reihe - REI + 1] = 0;
						}
					}
				}
				setNodeForceY(nodeID, getNodeDisplacementY(nodeID));
				KB[REI - 1][0] = 1;
			}
		}

		// Now all forces are known and will be reseted to zero
		for (nodeID = 0; nodeID < numberOfNodes * 2; nodeID++) {
			if (Double.isNaN(inputForces.getValue(nodeID, 0))) {
				inputForces.setValue(nodeID, 0, 0.0);
			}
		}
	}

	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public double getSolutionsDisplacementsMeanX(int elementId) {
		return solutionsDisplacementsMeanX[elementId - 1];
	}

	public Matrix getInputForces() {
		return inputForces;
	}

	public double[] getSolutionDisplacements() {
		return solutionsDisplacements;
	}

	public Matrix getInputDisplacements() {
		return inputDisplacements;
	}

	public double[] getSolutionForces() {
		return solutionForces;
	}

	public double getSumOfSolutionForces() {
		double result = 0;
		for (final double value : solutionForces) {
			result += value;
		}
		return result;
	}

	public double getSolutionNodeDisplacementY(int nodeId) {
		return solutionsDisplacements[nodeId * 2 - 1];
	}

	public double getSolutionNodeDisplacementX(int nodeId) {
		return solutionsDisplacements[nodeId * 2 - 2];
	}

	public double getSolutionNodeForceY(int nodeId) {
		return solutionForces[nodeId * 2 - 1];
	}

	public double getSolutionNodeForceX(int nodeId) {
		return solutionForces[nodeId * 2 - 2];
	}

	public double getNodeForceY(int nodeId) {
		return inputForces.getValue(nodeId * 2 - 1, 0);
	}

	public double getNodeForceX(int nodeId) {
		return inputForces.getValue(nodeId * 2 - 2, 0);
	}

	private double getNodeDisplacementY(int nodeId) {
		return inputDisplacements.getValue(nodeId * 2 - 1, 0);
	}

	private double getNodeDisplacementX(int nodeId) {
		return inputDisplacements.getValue(nodeId * 2 - 2, 0);
	}

	public void setNodeForceY(int nodeId, double value) {
		inputForces.setValue(nodeId * 2 - 1, 0, value);
	}

	public void setNodeForceX(int nodeId, double value) {
		inputForces.setValue(nodeId * 2 - 2, 0, value);
	}

	public boolean isNodeHorzontallyFixed(int nodeId) {
		return !Double.isNaN(inputDisplacements.getValue(nodeId * 2 - 1, 0));
	}

	public boolean isNodeVerticallyFixed(int nodeId) {
		return !Double.isNaN(inputDisplacements.getValue(nodeId * 2 - 2, 0));
	}

	public void incementNumberOfNodes() {
		numberOfNodes++;
	}

	public void incementNumberOfElements() {
		numberOfElements++;
	}

	public void resetInputDisplacements() {
		final double[] newInputDisplacements = new double[numberOfNodes * 2];
		for (int i = 0; i < numberOfNodes * 2; i++) {
			newInputDisplacements[i] = Double.NaN;
		}
		inputDisplacements = new Matrix(newInputDisplacements).transpose();
	}

	public void setInputDisplacementX(int nodeId, Double value) {
		inputDisplacements.setValue(nodeId * 2 - 2, 0, value);
	}

	public void setInputDisplacementY(int nodeId, Double value) {
		inputDisplacements.setValue(nodeId * 2 - 1, 0, value);
	}

	public void resetInputForces() {
		final double[] newInputForces = new double[numberOfNodes * 2];
		for (int i = 0; i < numberOfNodes * 2; i++) {
			newInputForces[i] = Double.NaN;
		}
		inputForces = new Matrix(newInputForces).transpose();
	}

	public void initRest() {
		solutionsDisplacementsMeanX = new double[numberOfElements];
		nodesByElement = new Node[1 + numberOfElements][1 + numberOfNodes];
		for (int i = 0; i < 1 + numberOfElements; i++) {
			for (int k = 0; k < 1 + numberOfNodes; k++) {
				nodesByElement[i][k] = new Node();
			}
		}
	}

	public int getNodeIdByElementId(int elementId, int cornerId) {
		return nodesByElement[elementId][cornerId].nodeID;
	}

	public void setNodeIdByElementIdX(int elementId, int cornerId, double value) {
		nodesByElement[elementId][cornerId].x = value;
	}

	public void setNodeIdByElementIdY(int elementId, int cornerId, double value) {
		nodesByElement[elementId][cornerId].y = value;
	}

	public double getNodeIdByElementIdX(int elementId, int cornerId) {
		return nodesByElement[elementId][cornerId].x;
	}

	public double getNodeIdByElementIdY(int elementId, int cornerId) {
		return nodesByElement[elementId][cornerId].y;
	}

	public void setNodeIdByElementIdnodeID(int elementId, int cornerId, int value) {
		nodesByElement[elementId][cornerId].nodeID = value;
	}
	

}