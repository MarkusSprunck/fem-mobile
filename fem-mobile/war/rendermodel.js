/**
 * Copyright (C) 2012-2015, Markus Sprunck
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

var OPTIONS = function optionsModelRenderer() {
	"use strict";
	return {
		MODEL_NAME : 'Beam',
		GRAVITY_ACTIVE : true,
		BETA : -60.0,
		GAMMA : 0.0,
		SCALE_FORCE : 0.0005,
		SCALE_DISPLACEMENT : 1.5,
		ORIENTATION : 'Normal portrait',
		LEFT : 50,
		BOTTOM : 160,
		COLOR_CODE : 4,
		SHOW_LEGEND : false
	};
}();

function ModelRenderer() {

	// Create force by drag with mouse
	this.forceX = 0.0;
	this.forceY = 0.0;
	this.selecedNodeId = null;
	this.selecedNodeIdLast = null;
	this.activeNodeId = null;
	this.mouseDownX = null;
	this.mouseDownY = null;

	// Parameter for color legend
	this.minColor = 20;
	this.maxColor = -20;

	this.graphic = document.getElementById("mySVGGui");
	this.graphicLegend = document.getElementById("mySVGGuiLegend");

	ModelRenderer.prototype.calculateColorRange = function() {
		this.minColor = 25000.0;
		this.maxColor = -25000.0;
		var numberOfElements = fem_getNumberOfElements();
		var delta = 0.0;
		for (var elementId = 1; elementId <= numberOfElements; elementId++, points = "") {
			delta = fem_getColorCode(elementId, parseInt(OPTIONS.COLOR_CODE));
			this.minColor = Math.min(delta, this.minColor);
			this.maxColor = Math.max(delta, this.maxColor);
		}
		this.minColor = Math.min(this.minColor, -0.0001);
		this.maxColor = Math.max(this.maxColor, 0.0001);
		this.minColor *= 1.2;
		this.maxColor *= 1.2;
	}

	ModelRenderer.prototype.renderModel = function() {
		var x, y = 0.0;
		var nodeId = 0;
		var points = "";
		var numberOfElements = fem_getNumberOfElements();
		for (var elementId = 1; elementId <= numberOfElements; elementId++, points = "") {
			nodeId = fem_getNodeId(elementId, 1);
			for (var cornerId = 1; cornerId <= 3; cornerId++) {

				// get node and location
				nodeId = fem_getNodeId(elementId, cornerId);
				x = OPTIONS.LEFT + fem_getX(elementId, cornerId) + fem_getSolutionDisplacementsX(nodeId) * OPTIONS.SCALE_DISPLACEMENT;
				y = OPTIONS.BOTTOM + fem_getY(elementId, cornerId) - fem_getSolutionDisplacementsY(nodeId) * OPTIONS.SCALE_DISPLACEMENT;

				// add this node to path for element
				points += [ x, y ].join(',') + ' ';
				this.renderFixtures(nodeId, x, y);
				this.renderForces(nodeId, x, y);
				this.renderNode(nodeId, x, y);
			}
			this.renderElement(elementId, points, fem_getColorCode(elementId, parseInt(OPTIONS.COLOR_CODE)));
		}
	}

	ModelRenderer.prototype.renderColorScala = function() {
		var scalaNumber = 12;
		var offset_x_scala = 10;
		var offset_y_scala = 20;
		var scala_size_x = 15;
		var scala_size_y = 200;
		var delta_y = scala_size_y / scalaNumber;

		for (var index = 0; index <= scalaNumber; index++) {
			var value = this.maxColor - (this.maxColor - this.minColor) * index / scalaNumber;
			var cubePoints = "";
			cubePoints += [ offset_x_scala, offset_y_scala + (index + 1) * delta_y ].join(',') + ' ';
			cubePoints += [ offset_x_scala, offset_y_scala + index * delta_y ].join(',') + ' ';
			cubePoints += [ offset_x_scala + scala_size_x, offset_y_scala + index * delta_y ].join(',') + ' ';
			cubePoints += [ offset_x_scala + scala_size_x, offset_y_scala + (index + 1) * delta_y ].join(',') + ' ';
			var elementSVG = this.getPolygonElementSVG(index, "svgLegend");
			if (null != elementSVG) {
				elementSVG.setAttribute('points', cubePoints.trim());
				elementSVG.setAttribute('style', "fill: " + this.getColor(value) + "; stroke: " + this.getColor(value) + "; opacity:"
						+ (OPTIONS.SHOW_LEGEND ? 1.0 : 0.0));
			}

			var text = document.getElementById("LT1" + index);
			if (null == text) {
				text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
				text.setAttribute('id', "LT1" + index);
			}
			text.setAttribute('x', offset_x_scala + scala_size_x * 6.5);
			text.setAttribute('y', offset_y_scala + (index + 0.75) * scala_size_y / scalaNumber);
			text.setAttribute('fill', '#000000');
			text.textContent = value.toExponential(2).replace("e", "E");
			text.setAttribute('style', "opacity:" + (OPTIONS.SHOW_LEGEND ? 1.0 : 0.0));
			var svg1 = document.getElementById("svgLegend");
			svg1.appendChild(text);
		}
	}

	function toFixed(x) {
		if (Math.abs(x) < 1.0) {
			var e = parseInt(x.toString().split('e-')[1]);
			if (e) {
				x *= Math.pow(10, e - 1);
				x = '0.' + (new Array(e)).join('0') + x.toString().substring(2);
			}
		} else {
			var e = parseInt(x.toString().split('+')[1]);
			if (e > 20) {
				e -= 20;
				x /= Math.pow(10, e);
				x += (new Array(e + 1)).join('0');
			}
		}
		return x;
	}

	ModelRenderer.prototype.renderFixtures = function(nodeId, x, y) {
		var size = 10;
		if (fem_isFixedX(nodeId)) {
			var trianglePoints = "";
			trianglePoints += [ x, y ].join(',') + ' ';
			trianglePoints += [ x - size, y - size * 0.75 ].join(',') + ' ';
			trianglePoints += [ x - size, y + size * 0.75 ].join(',') + ' ';
			var elementSVG = this.getPolygonElementSVG('FIX_V' + nodeId, "svgFixed");
			if (null != elementSVG) {
				elementSVG.setAttribute('points', trianglePoints.trim());
				elementSVG.setAttribute('style', "stroke: #FFFFFF; fill-opacity: 0.2");
			}
		}
		if (fem_isFixedY(nodeId)) {
			var trianglePoints = "";
			trianglePoints += [ x, y ].join(',') + ' ';
			trianglePoints += [ x + size * 0.75, y + size ].join(',') + ' ';
			trianglePoints += [ x - size * 0.75, y + size ].join(',') + ' ';
			var elementSVG = this.getPolygonElementSVG('FIX_H' + nodeId, "svgFixed");
			if (null != elementSVG) {
				elementSVG.setAttribute('points', trianglePoints.trim());
				elementSVG.setAttribute('style', "stroke: #FFFFFF; fill-opacity: 0.2");
			}
		}
	}

	ModelRenderer.prototype.renderForces = function(nodeId, x, y) {
		var isSelectedElement = !OPTIONS.GRAVITY_ACTIVE && ('N' + (nodeId)) == this.selecedNodeId;
		this.drawVector(x, y, x + fem_getSolutionForcesX(nodeId) * OPTIONS.SCALE_FORCE, y, true, (fem_getSolutionForcesX(nodeId) > 0.0), nodeId,
				isSelectedElement, fem_isFixedX(nodeId));
		this.drawVector(x, y, x, y + fem_getSolutionForcesY(nodeId) * OPTIONS.SCALE_FORCE, false, (fem_getSolutionForcesY(nodeId) > 0.0), nodeId,
				isSelectedElement, fem_isFixedY(nodeId));
	}

	ModelRenderer.prototype.renderElement = function(idElement, elementPoints, delta) {
		var elementSVG = this.getPolygonElementSVG('E' + idElement, "svgElements");
		if (null != elementSVG) {
			elementSVG.setAttribute('points', elementPoints.trim());
			elementSVG.setAttribute('style', "fill:" + this.getColor(delta) + ";");
		}
	}

	ModelRenderer.prototype.renderNode = function(nodeId, x, y) {
		var elementSVG = this.getCircleElementSVG('N' + nodeId, "svgNodes");
		if (null != elementSVG) {
			elementSVG.setAttribute('cx', x);
			elementSVG.setAttribute('cy', y);
			elementSVG.setAttribute('r', 10);
		}
	}

	ModelRenderer.prototype.toDegrees = function(angle) {
		return angle * (180 / Math.PI);
	}

	ModelRenderer.prototype.getColor = function(mean) {
		var toHex = function(n) {
			return "0123456789ABCDEF".charAt((n - n % 16) / 16) + "0123456789ABCDEF".charAt(n % 16);
		}
		mean = -1.8 * Math.PI / (this.maxColor - this.minColor) * mean;
		red = Math.sin(mean + 2) * 127 + 128;
		green = Math.sin(mean + 1) * 127 + 128;
		blue = Math.sin(mean * 1.5 + 4) * 127 + 128;
		return '#' + toHex(red) + toHex(green) + toHex(blue);
	}

	ModelRenderer.prototype.getPolygonElementSVG = function(id, groupId) {
		var elementSVG = document.getElementById(id);
		if (null == elementSVG) {
			elementSVG = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
			elementSVG.setAttribute('id', id);
			document.getElementById(groupId).appendChild(elementSVG);
		}
		return elementSVG;
	}

	ModelRenderer.prototype.getCircleElementSVG = function(id, groupId) {
		var elementSVG = document.getElementById(id);
		if (null == elementSVG) {
			elementSVG = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
			elementSVG.setAttribute('id', id);
			var title = document.createElementNS('http://www.w3.org/2000/svg', 'title');
			title.innerHTML = id;
			elementSVG.setAttribute('style', "opacity:0.0");
			elementSVG.appendChild(title);

			// Add event listener for node
			var _that = this;
			elementSVG.addEventListener('mousedown', function(event) {
				event.preventDefault();
				_that.mouseDownX = event.clientX;
				_that.mouseDownY = event.clientY;
				_that.selecedNodeIdLast = _that.selecedNodeId;
				_that.selecedNodeId = event.target.id;
				_that.changeOpacityOfNode();
				OPTIONS.GRAVITY_ACTIVE = false;
			}, false);
			elementSVG.addEventListener('touchstart', function(event) {
				event.preventDefault();
				_that.mouseDownX = event.touches[0].clientX;
				_that.mouseDownY = event.touches[0].clientY;
				_that.selecedNodeIdLast = _that.selecedNodeId;
				_that.selecedNodeId = event.target.id;
				_that.changeOpacityOfNode();
				OPTIONS.GRAVITY_ACTIVE = false;
			}, false);

			elementSVG.addEventListener('touchmove', function(event) {
				event.preventDefault();
				_that.getCircleElementSVG(_that.activeNodeId, "svgNodes").setAttribute('style', "opacity:0.0");
				_that.activeNodeId = event.target.id;
				_that.changeOpacityOfNode();
				OPTIONS.GRAVITY_ACTIVE = false;
			}, false);

			elementSVG.addEventListener('mousemove', function(event) {
				event.preventDefault();
				_that.getCircleElementSVG(_that.activeNodeId, "svgNodes").setAttribute('style', "opacity:0.0");
				_that.activeNodeId = event.target.id;
				_that.changeOpacityOfNode();
			}, false);

			document.getElementById(groupId).appendChild(elementSVG);
		}
		return elementSVG;
	}

	ModelRenderer.prototype.changeOpacityOfNode = function() {
		if (this.activeNodeId != null) {
			if (this.selecedNodeId != null) {
				this.getCircleElementSVG(this.selecedNodeId, "svgNodes").setAttribute('style', "fill:white; opacity:0.5");
			} else {
				this.getCircleElementSVG(this.activeNodeId, "svgNodes").setAttribute('style', "fill:white; opacity:0.5");
			}
		}
	}

	ModelRenderer.prototype.drawVector = function(startX, startY, endX, endY, horizontal, positive, ele, isSelectedElement, isFixedNode) {

		var length = 6;
		var isVisible = (isFixedNode || isSelectedElement) && (Math.abs(startX - endX) + Math.abs(startY - endY) > length);

		var arrowPoints = "";
		if (isVisible) {
			arrowPoints += [ startX, startY ].join(',') + ' ';
			arrowPoints += [ endX, endY ].join(',') + ' ';

			if (horizontal && !positive) {
				arrowPoints += [ endX + length, endY - length ].join(',') + ' ';
				arrowPoints += [ endX, endY ].join(',') + ' ';
				arrowPoints += [ endX + length, endY + length ].join(',') + ' ';
				arrowPoints += [ endX, endY ].join(',') + ' ';
			} else if (horizontal && positive) {
				arrowPoints += [ endX - length, endY - length ].join(',') + ' ';
				arrowPoints += [ endX, endY ].join(',') + ' ';
				arrowPoints += [ endX - length, endY + length ].join(',') + ' ';
				arrowPoints += [ endX, endY ].join(',') + ' ';
			} else if (!horizontal && positive) {
				arrowPoints += [ endX - length, endY - length ].join(',') + ' ';
				arrowPoints += [ endX, endY ].join(',') + ' ';
				arrowPoints += [ endX + length, endY - length ].join(',') + ' ';
				arrowPoints += [ endX, endY ].join(',') + ' ';
			} else if (!horizontal && !positive) {
				arrowPoints += [ endX - length, endY + length ].join(',') + ' ';
				arrowPoints += [ endX, endY ].join(',') + ' ';
				arrowPoints += [ endX + length, endY + length ].join(',') + ' ';
				arrowPoints += [ endX, endY ].join(',') + ' ';
			}
		}
		var elementSVG = this.getPolygonElementSVG('Arrow_' + horizontal + ele, "svgArrows");
		if (null != elementSVG) {
			elementSVG.setAttribute('points', arrowPoints.trim());
			elementSVG.setAttribute('style', "stroke:#FF0000;stroke-width: 1.0; visibility:" + ((isVisible) ? "visible" : "hidden"));
		}
	}

	// Create force by drag with mouse - handle start
	var _that = this;
	var dragEndHandler = function(event) {
		event.preventDefault();
		_that.mouseDownX = null;
		_that.mouseDownY = null;
		_that.getCircleElementSVG(_that.selecedNodeId, "svgNodes").setAttribute('style', "opacity:0.0");
		_that.selecedNodeId = null;
		_that.activeNodeId = null;
	}
	this.graphic.addEventListener('mouseup', dragEndHandler, false);
	this.graphic.addEventListener('touchend', dragEndHandler, false);

	// Create force by drag with mouse - handle move
	var dragHandler = function(event) {
		event.preventDefault();
		var isVisible = _that.selecedNodeId != null;
		if (isVisible) {
			var x2 = (event.type == "mousemove") ? event.clientX : event.touches[0].clientX;
			var y2 = (event.type == "mousemove") ? event.clientY : event.touches[0].clientY;
			if (!OPTIONS.GRAVITY_ACTIVE) {
				var factor = 1.0;
				_that.forceY = (y2 - _that.mouseDownY) * factor;
				_that.forceX = (x2 - _that.mouseDownX) * factor;
				fem_runSimulation();
			}
		}
		var elementSVG = _that.getCircleElementSVG(_that.activeNodeId, "svgNodes");
		if (event.target.id != _that.activeNodeId && !isVisible) {
			elementSVG.setAttribute('style', "opacity: 0.0");
			_that.activeNodeId = null;
		}
	}
	this.graphic.addEventListener('mousemove', dragHandler, false);
	this.graphic.addEventListener('touchmove', dragHandler, false);

}
