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

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FemMobile implements EntryPoint {

	/**  FEM Model for 2D Mechanics*/
	static Solver model;

	/** Case I: Device orientation for Gravity */
	static Double beta = 0.0;
	static Double gamma = 0.0;

	/** Case II: Single force */
	static Double forceX = 0.0;
	static Double forceY = 0.0;
	static String selecedNodeId = "";

	/** Activates the kind of force to be applied */
	static boolean isGravityActive = false;

	static String modelName = "Eiffel";

	public FemMobile() {
		initModel();
	}

	private static void initModel() {
		model = new Solver();
		if ("Cantilever".equalsIgnoreCase(modelName)) {
			model.createModel(ModelFactory.createDefaultModel(500, 100, 20, 6, 0, false).toString());
		} else if ("Beam".equalsIgnoreCase(modelName)) {
			model.createModel(ModelFactory.createDefaultModel(500, 100, 20, 6, 0, true).toString());
		} else if ("Eiffel".equalsIgnoreCase(modelName)) {
			model.createModel(ModelFactory.createEiffelTowerModel(2.7, 2.7));
		}
		renderModel();
	}

	public static void runSimulation() {
		forceY = Double.valueOf(getForceY());
		forceX = Double.valueOf(getForceX());
		beta = Double.valueOf(getBeta());
		gamma = Double.valueOf(getGamma());
		isGravityActive = Boolean.parseBoolean(isGravityActive());
		selecedNodeId = (null != getSelecedNodeId()) ? getSelecedNodeId() : "";

		if (!modelName.equalsIgnoreCase(getModelName())) {
			modelName = getModelName();
			initModel();
		}

		if (isGravityActive) {
			model.solve(model.caluculateInputForcesGravity(beta, gamma));
		} else {
			model.solve(model.caluculateInputForcesSingle(forceX, forceY, selecedNodeId));
		}

		renderModel();
	}

	public static native void renderModel()
	/*-{
		$wnd.renderModel();
	}-*/;

	public static native String getBeta()
	/*-{
		return $wnd.getBeta();
	}-*/;

	public static native String getGamma()
	/*-{
		return $wnd.getGamma();
	}-*/;

	public static native String getForceX()
	/*-{
		return $wnd.getForceX();
	}-*/;

	public static native String getForceY()
	/*-{
		return $wnd.getForceY();
	}-*/;

	public static native String getModelName()
	/*-{
		return $wnd.getModelName();
	}-*/;

	public static native String isGravityActive()
	/*-{
		return $wnd.isGravityActive();
	}-*/;

	public static native String getSelecedNodeId()
	/*-{
		return $wnd.getSelecedNodeId();
	}-*/;

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		exportRunSimulation();
		exportStaticMethod1();
		exportStaticMethod2();
		exportStaticMethod3();
		exportStaticMethod4();
		exportStaticMethod5();
		exportStaticMethod6();
		exportStaticMethod7();
		exportStaticMethod8();
		exportStaticMethod9();
		exportStaticMethod10();
		exportStaticMethod11();
		exportStaticMethod12();
		exportStaticMethod13();
		exportStaticMethod14();

		runSimulation();
	}

	public static int getNumberOfElements() {
		return model.numberOfElements;
	};

	public static int getNumberOfNodes() {
		return model.numberOfNodes;
	};

	public static int getNodeId(int elementId, int cornerId) {
		return model.nodes[elementId][cornerId].nodeID;
	};

	public static double getSolutionForcesX(int nodeId) {
		double value = model.solutionForces.getValue(nodeId * 2 - 2);
		return Double.isNaN(value) ? 0.0 : value;
	};

	public static double getSolutionForcesY(int nodeId) {
		double value = model.solutionForces.getValue(nodeId * 2 - 1);
		return Double.isNaN(value) ? 0.0 : value;
	};

	public static double getSolutionDisplacementsX(int nodeId) {
		double value = model.solutionDisplacements.getValue(nodeId * 2 - 2);
		return Double.isNaN(value) ? 0.0 : value;
	};

	public static double getSolutionDisplacementsY(int nodeId) {
		double value = -model.solutionDisplacements.getValue(nodeId * 2 - 1);
		return Double.isNaN(value) ? 0.0 : value;
	};

	public static boolean isFixedY(int nodeId) {
		return !Double.isNaN(model.inputDisplacements.getValue(nodeId * 2 - 1));
	};

	public static boolean isFixedX(int nodeId) {
		return !Double.isNaN(model.inputDisplacements.getValue(nodeId * 2 - 2));
	};

	public static double getX(int elementId, int cornerId) {
		return model.nodes[elementId][cornerId].x;
	};

	public static double getY(int elementId, int cornerId) {
		return -model.nodes[elementId][cornerId].y;
	};

	public static double getStressX(int elementId) {
		return model.getStressX(elementId);
	};

	public static double getStressY(int elementId) {
		return model.getStressY(elementId);
	};

	public static double getShearStress(int elementId) {
		return model.getShearStress(elementId);
	};

	public static double getColorCode(int elementId, int type) {
		double value = 0.0;

		switch (type) {
		case 1: {
			value = getStressX(elementId);
			break;
		}
		case 2: {
			value = getStressY(elementId);
			break;
		}
		case 3: {
			value = getShearStress(elementId);
			break;
		}
		case 4: {
			value = (getSolutionDisplacementsX(getNodeId(elementId, 1)) + getSolutionDisplacementsX(getNodeId(elementId, 2)) + getSolutionDisplacementsX(getNodeId(
					elementId, 3))) / 3.0;
			break;
		}
		case 5: {
			value = (getSolutionDisplacementsY(getNodeId(elementId, 1)) + getSolutionDisplacementsY(getNodeId(elementId, 2)) + getSolutionDisplacementsY(getNodeId(
					elementId, 3))) / 3.0;
			break;
		}
		}
		return value;
	};

	public static native void exportRunSimulation() /*-{
		$wnd.fem_runSimulation = $entry(@com.sw_engineering_candies.fem.client.FemMobile::runSimulation());
	}-*/;

	public static native void exportStaticMethod1() /*-{
		$wnd.fem_getNumberOfNodes = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getNumberOfNodes());
	}-*/;

	public static native void exportStaticMethod2() /*-{
		$wnd.fem_getNumberOfElements = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getNumberOfElements());
	}-*/;

	public static native void exportStaticMethod3() /*-{
		$wnd.fem_getNodeId = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getNodeId(II));
	}-*/;

	public static native void exportStaticMethod4() /*-{
		$wnd.fem_getSolutionForcesX = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getSolutionForcesX(I));
	}-*/;

	public static native void exportStaticMethod5() /*-{
		$wnd.fem_getSolutionForcesY = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getSolutionForcesY(I));
	}-*/;

	public static native void exportStaticMethod6() /*-{
		$wnd.fem_getSolutionDisplacementsX = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getSolutionDisplacementsX(I));
	}-*/;

	public static native void exportStaticMethod7() /*-{
		$wnd.fem_getSolutionDisplacementsY = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getSolutionDisplacementsY(I));
	}-*/;

	public static native void exportStaticMethod8() /*-{
		$wnd.fem_isFixedX = $entry(@com.sw_engineering_candies.fem.client.FemMobile::isFixedX(I));
	}-*/;

	public static native void exportStaticMethod9() /*-{
		$wnd.fem_isFixedY = $entry(@com.sw_engineering_candies.fem.client.FemMobile::isFixedY(I));
	}-*/;

	public static native void exportStaticMethod10() /*-{
		$wnd.fem_getX = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getX(II));
	}-*/;

	public static native void exportStaticMethod11() /*-{
		$wnd.fem_getY = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getY(II));
	}-*/;

	public static native void exportStaticMethod12() /*-{
		$wnd.fem_getStressX = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getStressX(I));
	}-*/;

	public static native void exportStaticMethod13() /*-{
		$wnd.fem_getStressY = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getStressY(I));
	}-*/;

	public static native void exportStaticMethod14() /*-{
		$wnd.fem_getColorCode = $entry(@com.sw_engineering_candies.fem.client.FemMobile::getColorCode(II));
	}-*/;

}
