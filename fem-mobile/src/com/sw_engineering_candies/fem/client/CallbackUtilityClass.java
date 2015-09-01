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
 */
package com.sw_engineering_candies.fem.client;

public class CallbackUtilityClass {

	final static Solver model = new Solver();

	static Double beta = 0.0;

	static Double gamma = 0.0;
	
	static boolean isGravityActive = true;

	public CallbackUtilityClass() {
		final String inputModel = ModelFactory.createEiffelTowerModel();
		model.createModel(inputModel);
		final Vector forces = model.caluculateInputForces(beta, gamma);
		model.solve(forces);
		setModelGlobal(model.getJSON());
	}

	public void updateModel() {
		getValuesFromGui();
		final Vector forces = model.caluculateInputForces(beta, gamma);
		model.solve(forces);
		setModelGlobal(model.getJSON());
	}

	public static void getValuesFromGui() {

		final String currentBetaNew = getBeta();
		if (null != currentBetaNew && !currentBetaNew.isEmpty()) {
			beta = Double.valueOf(currentBetaNew);
		}

		final String currentGammaNew = getGamma();
		if (null != currentGammaNew && !currentGammaNew.isEmpty()) {
			gamma = Double.valueOf(currentGammaNew);
		}
		
		final String currentIsGravityActive = isGravityActive();
		if (null != currentIsGravityActive && !currentIsGravityActive.isEmpty()) {
			isGravityActive = Boolean.parseBoolean(currentIsGravityActive);
		}
	}

	public static native void exportStaticMethod() /*-{
		$wnd.updateForces = $entry(@com.sw_engineering_candies.fem.client.CallbackUtilityClass::getValuesFromGui());
	}-*/;

	public static native void setModelGlobal(String model)
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
	
	public static native String isGravityActive()
	/*-{
		return $wnd.isGravityActive();
	}-*/;

}
