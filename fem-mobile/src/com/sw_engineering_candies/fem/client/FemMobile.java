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
import com.google.gwt.user.client.Timer;

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

	static String modelName = "";

	public FemMobile() {
		initModel();
	}

	private static void initModel() {
		model = new Solver();

		if ("Cantilever".equalsIgnoreCase(modelName)) {
			model.createModel(ModelFactory.createDefaultModel(350, 30, 70, 6, 0, false).toString());
		} else if ("Beam".equalsIgnoreCase(modelName)) {
			model.createModel(ModelFactory.createDefaultModel(350, 30, 70, 6, 0, true).toString());
		} else if ("Eiffel Tower".equalsIgnoreCase(modelName)) {
			model.createModel(ModelFactory.createEiffelTowerModel());
		}
		setModel(model.getJSON());
	}

	public void runSimulation() {
		if (isGravityActive) {
			model.solve(model.caluculateInputForcesGravity(beta, gamma));
		} else {
			model.solve(model.caluculateInputForcesSingle(forceX, forceY, selecedNodeId));
		}
		setModel(model.getJSON());
	}

	public static void fetchValuesFromGui() {
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
	}

	public static native void exportStaticMethod() /*-{
		$wnd.updateForces = $entry(@com.sw_engineering_candies.fem.client.FemMobile::fetchValuesFromGui());
	}-*/;

	public static native void setModel(String model)
	/*-{
		$wnd.setModel(model);
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

		exportStaticMethod();

		final Timer timerGraficUpdate = new Timer() {
			@Override
			public void run() {
				fetchValuesFromGui();
				runSimulation();
			}
		};
		timerGraficUpdate.scheduleRepeating(100);
	}

}
