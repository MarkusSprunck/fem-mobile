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
import java.util.LinkedList;
import java.util.List;

public class ModelUtil {

	public static int createModelFromString(Solver2 femCore, String input) {

		int bandwidthExpected = 0;

		final List<Node> temporaryNodes = new LinkedList<Node>();
		temporaryNodes.add(new Node());

		final List<Integer[]> temporaryElements = new LinkedList<Integer[]>();
		temporaryElements.add(new Integer[5]);

		final String[] lines = input.toString().split("\\n");
		for (final String line : lines) {
			if (!line.trim().isEmpty()) {
				final String[] args = line.split(",");
				if (0 == args[0].trim().compareToIgnoreCase("N")) {
					femCore.incementNumberOfNodes();
					final int number = Integer.valueOf(args[1].trim());
					for (int index = temporaryNodes.size(); index <= number; index++) {
						temporaryNodes.add(new Node());
					}

					final Integer first = Integer.valueOf(args[2].trim());
					final Integer second = Integer.valueOf(args[3].trim());

					temporaryNodes.get(number).x = first;
					temporaryNodes.get(number).y = second;
				}
				if (0 == args[0].trim().compareToIgnoreCase("E")) {
					femCore.incementNumberOfElements();
					final int number = Integer.valueOf(args[1].trim());
					for (int index = temporaryElements.size(); index <= number; index++) {
						temporaryElements.add(new Integer[5]);
					}

					final Integer first = Integer.valueOf(args[2].trim());
					final Integer second = Integer.valueOf(args[3].trim());
					final Integer third = Integer.valueOf(args[4].trim());

					temporaryElements.get(femCore.getNumberOfElements())[1] = number;
					temporaryElements.get(femCore.getNumberOfElements())[2] = first;
					temporaryElements.get(femCore.getNumberOfElements())[3] = second;
					temporaryElements.get(femCore.getNumberOfElements())[4] = third;

					final int max = Math.max(Math.max(first, second), third);
					final int min = Math.min(Math.min(first, second), third);
					final int bandwidthOfElement = (1 + max - min) * 2;
					bandwidthExpected = Math.max(bandwidthExpected, bandwidthOfElement);
				}
				if (0 == args[0].trim().compareToIgnoreCase("D")) {

					if (femCore.getInputDisplacements() == null) {
						femCore.resetInputDisplacements();
					}

					final int number = Integer.valueOf(args[1].trim());
					if (0 == args[2].trim().compareToIgnoreCase("x")) {
						femCore.setInputDisplacementX(number, Double.valueOf(args[3].trim()));
					}
					if (0 == args[2].trim().compareToIgnoreCase("y")) {
						femCore.setInputDisplacementY(number, Double.valueOf(args[3].trim()));
					}
				}
				if (0 == args[0].trim().compareToIgnoreCase("F")) {

					if (femCore.getInputForces() == null) {
						femCore.resetInputForces();
					}

					final int number = Integer.valueOf(args[1].trim());
					if (0 == args[2].trim().compareToIgnoreCase("x")) {
						femCore.setNodeForceX(number, Double.valueOf(args[3].trim()));
					}
					if (0 == args[2].trim().compareToIgnoreCase("y")) {
						femCore.setNodeForceY(number, Double.valueOf(args[3].trim()));
					}
				}
			}
		}

		femCore.initRest();

		for (int i = 1; i <= femCore.getNumberOfElements(); i++) {
			femCore.setNodeIdByElementIdX(temporaryElements.get(i)[1], 1,
					temporaryNodes.get(temporaryElements.get(i)[2]).x);
			femCore.setNodeIdByElementIdX(temporaryElements.get(i)[1], 2,
					temporaryNodes.get(temporaryElements.get(i)[3]).x);
			femCore.setNodeIdByElementIdX(temporaryElements.get(i)[1], 3,
					temporaryNodes.get(temporaryElements.get(i)[4]).x);

			femCore.setNodeIdByElementIdY(temporaryElements.get(i)[1], 1,
					temporaryNodes.get(temporaryElements.get(i)[2]).y);
			femCore.setNodeIdByElementIdY(temporaryElements.get(i)[1], 2,
					temporaryNodes.get(temporaryElements.get(i)[3]).y);
			femCore.setNodeIdByElementIdY(temporaryElements.get(i)[1], 3,
					temporaryNodes.get(temporaryElements.get(i)[4]).y);

			femCore.setNodeIdByElementIdnodeID(temporaryElements.get(i)[1], 1, temporaryElements.get(i)[2]);
			femCore.setNodeIdByElementIdnodeID(temporaryElements.get(i)[1], 2, temporaryElements.get(i)[3]);
			femCore.setNodeIdByElementIdnodeID(temporaryElements.get(i)[1], 3, temporaryElements.get(i)[4]);
		}
		System.out.println("default model created    [nodes=" + femCore.getNumberOfNodes() + ", elements="
				+ femCore.getNumberOfElements() + ", bandwidth=" + bandwidthExpected + "]");
		return bandwidthExpected;
	}

	public static String getJSON(Solver2 model, double beta, double gamma) {

		final double start = System.currentTimeMillis();

		final int numberOfNodes = model.getNumberOfNodes();
		final int numberOfElements = model.getNumberOfElements();

		// Set new gravity forces
		if (Math.abs(beta) >= 0.1 || Math.abs(gamma) >= 0.1) {
			
			double xForce = 20.0 * Math.sin(- beta / 180 * Math.PI) / numberOfNodes;
			double yForce = 20.0 * Math.sin(- gamma / 180 * Math.PI) / numberOfNodes;

			final double[] newinputForces = new double[numberOfNodes * 2];
			for (int nodeId = 1; nodeId <= numberOfNodes; nodeId++) {
				if (!model.isNodeHorzontallyFixed(nodeId)) {
					newinputForces[nodeId * 2 - 1] = xForce;
				} else {
					newinputForces[nodeId * 2 - 1] = 0.0;
				}

				if (!model.isNodeVerticallyFixed(nodeId)) {
					newinputForces[nodeId * 2 - 2] = yForce;
				} else {
					newinputForces[nodeId * 2 - 2] = 0.0;
				}
			}
			model.solve(newinputForces);
		} else {
			model.solve(null);
		}

		final HashMap<Integer, Boolean> nodeIds = new HashMap<Integer, Boolean>();
		final StringBuilder pre = new StringBuilder("[");
		for (int elementId = 1; elementId <= numberOfElements; elementId++) {
			pre.append("[");
			for (int cornerId = 1; cornerId < 4; cornerId++) {
				final int nodeId = model.getNodeIdByElementId(elementId, cornerId);
				pre.append("\n{\"id\": ")
						.append(nodeId //
						).append(", \"x_force\" : ")
						.append(model.getSolutionNodeForceX(nodeId) //
						).append(", \"y_force\" : ")
						.append(model.getSolutionNodeForceY(nodeId) //
						).append(", \"x_d\" : ")
						.append(model.getSolutionNodeDisplacementX(nodeId) //
						).append(", \"y_d\" : ")
						.append(model.getSolutionNodeDisplacementY(nodeId) //
						).append(", \"x_fixed\" : ")
						.append(model.isNodeVerticallyFixed(nodeId) //
						).append(", \"y_fixed\" : ")
						.append(model.isNodeHorzontallyFixed(nodeId) //
						).append(", \"x\" : ")
						.append(model.getNodeIdByElementIdX(elementId, cornerId) //
						).append(", \"y\" : ")
						.append(model.getNodeIdByElementIdY(elementId, cornerId) //
						).append(", \"deltaArea\" : ").append(model.getSolutionsDisplacementsMeanX(elementId) //
						).append("  }\n");
				if (cornerId < 3) {
					pre.append(',');
				}
				nodeIds.put(nodeId, true);
			}
			pre.append("]");
			if (elementId < numberOfElements) {
				pre.append(',');
			}
		}
		pre.append("]");
		final double end = System.currentTimeMillis();
		System.out.println("json created             [" + (end - start) + "ms]");

		return pre.toString();
	}
	
	
	public static String createDefaultModel(Solver2 femCore) {
		final StringBuffer nodeText = new StringBuffer();
		final int maxCols = 24;
		final int maxRows = 6;
		final int scaleFactorX = 30;
		final int scaleFactorY = 30;
		for (int col = 1; col <= maxCols; col++) {
			for (int row = 1; row <= maxRows; row++) {
				final int nodeId = row + maxRows * (col - 1);
				nodeText.append("N, ").append(nodeId).append(", ").append(col * scaleFactorX).append(", ")
						.append(row * scaleFactorY).append(",\n");
			}
		}

		for (int col = 1; col < maxCols; col++) {
			for (int row = 1; row < maxRows; row++) {
				final int firstElementId = row * 2 - 1 + (maxRows - 1) * 2 * (col - 1);
				final int secondElementId = row * 2 + (maxRows - 1) * 2 * (col - 1);
				final int node1Id = row + maxRows * (col - 1);
				final int node2Id = row + maxRows * col;
				final int node3Id = row + 1 + maxRows * (col - 1);
				final int node4Id = row + 1 + maxRows * (col + 1 - 1);
				nodeText.append("E, ").append(firstElementId).append(", ").append(node1Id).append(", ")
						.append(node2Id).append(", ").append(node3Id).append(",\n");
				nodeText.append("E, ").append(secondElementId).append(", ").append(node2Id).append(", ")
						.append(node4Id).append(", ").append(node3Id).append(",\n");
			}
		}

		nodeText.append("D, ").append(1).append(", y, ").append(0).append(",\n");
		for (int row = 1; row <= maxRows; row++) {
			nodeText.append("D, ").append(row).append(", x, ").append(0).append(",\n");
		}

		nodeText.append("F, ").append(maxRows * maxCols).append(", y, ").append(0.0001).append(",\n");

		return nodeText.toString();
	}

}
