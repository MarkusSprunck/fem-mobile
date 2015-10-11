/**
 * Copyright (C) 2015, Markus Sprunck
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - The name of its contributor may be used to endorse
 * or promote products derived from this software without specific prior written
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 */

/**
 * The window.orientation is different for iOS and Android
 */
var checkOrientationHandler = function(eventData) {
	var previousOrientation = window.orientation;
	if (typeof previousOrientation !== "undefined") {
		var ua = navigator.userAgent.toLowerCase();
		var isAndroid = ua.indexOf("android") > -1;
		if (isAndroid) {
			if (!previousOrientation || previousOrientation === 90) {
				OPTIONS.ORIENTATION = 'Normal portrait';
			} else if (previousOrientation === 0) {
				OPTIONS.ORIENTATION = 'Landscape, rotated clockwise';
			} else if (previousOrientation === 180) {
				OPTIONS.ORIENTATION = 'Landscape, rotated counterclockwise';
			} else if (previousOrientation === -90) {
				OPTIONS.ORIENTATION = 'Portrait, upside down';
			}
		} else {
			if (!previousOrientation || previousOrientation === 0) {
				OPTIONS.ORIENTATION = 'Normal portrait';
			} else if (previousOrientation === -90) {
				OPTIONS.ORIENTATION = 'Landscape, rotated clockwise';
			} else if (previousOrientation === 90) {
				OPTIONS.ORIENTATION = 'Landscape, rotated counterclockwise';
			} else if (previousOrientation === 180) {
				OPTIONS.ORIENTATION = 'Portrait, upside down';
			}
		}
	}
};

/**
 * Calculate orientation angels
 */
var deviceOrientationHandler = function(eventData) {
	if (typeof modelRenderer !== "undefined") {
		var beta;
		var gamma;
		if (OPTIONS.ORIENTATION == 'Normal portrait') {
			beta = -eventData.beta;
			gamma = -eventData.gamma;
		} else if (OPTIONS.ORIENTATION == 'Landscape, rotated clockwise') {
			beta = -eventData.gamma;
			gamma = eventData.beta;
		} else if (OPTIONS.ORIENTATION == 'Landscape, rotated counterclockwise') {
			beta = eventData.gamma;
			gamma = -eventData.beta;
		} else if (OPTIONS.ORIENTATION == 'Portrait, upside down') {
			beta = eventData.beta;
			gamma = eventData.gamma;
		}

		if (modelRenderer.rotate) {
			temp = gamma;
			gamma = beta;
			beta = -temp;
		}

		if (typeof modelRenderer !== "undefined" && OPTIONS.GRAVITY_ACTIVE) {
			OPTIONS.BETA = beta;
			OPTIONS.GAMMA = gamma;
			fem_runSimulation();
		}
	}
}

window.addEventListener('onresize', checkOrientationHandler, false);
window.addEventListener('deviceorientation', deviceOrientationHandler, false);
window.addEventListener("resize", checkOrientationHandler, false);
window.addEventListener("orientationchange", checkOrientationHandler, false);

/**
 * Android doesn't always fire orientationChange on 180 degree turns
 */
setInterval(checkOrientationHandler, 3000);