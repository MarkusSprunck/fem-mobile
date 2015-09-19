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
		BETA : 0.0,
		GAMMA : 0.0,
		SCALE_FORCE : 0.005,
		SCALE_DISPLACEMENT : 1.0,
		ORIENTATION : 'Normal portrait'
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
	this.colorCode = 1;
	this.minColor = 20;
	this.maxColor = -20;

	this.graphic = document.getElementById("mySVGGui");

	ModelRenderer.prototype.renderModel = function(model, offset_x, offset_y) {
		var x, y = 0.0;
		var points = "";
		var node = null;
		var element = null;
		for (var ele = model.length - 1; ele >= 0; ele--, points = "") {
			element = model[ele];
			var delta = (this.colorCode == 1) ? model[ele][0].x_d : model[ele][0].y_d;
			for (var nodeId = 0; nodeId < 3; nodeId++) {
				// get node and location
				node = element[nodeId];
				x = offset_x + node.x + node.x_d * OPTIONS.SCALE_DISPLACEMENT;
				y = offset_y + node.y + node.y_d * OPTIONS.SCALE_DISPLACEMENT;

				// add this node to path for element
				points += [ x, y ].join(',') + ' ';

				this.renderFixtures(node, x, y);
				this.renderForces(node, x, y);
				this.renderNode(node, x, y);
			}
			this.renderElement(node, points, delta);
		}
	}

	ModelRenderer.prototype.renderColorScala = function() {
		var scalaNumber = 25;
		var offset_x_scala = 10;
		var offset_y_scala = 80;
		var scala_size_x = 15;
		var scala_size_y = 395;
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
				elementSVG.setAttribute('style', "fill: " + this.getColor(value) + "; stroke: " + this.getColor(value));
			}

			var text = document.getElementById("LT1" + index);
			if (null == text) {
				text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
				text.setAttribute('id', "LT1" + index);
			}
			text.setAttribute('x', offset_x_scala + scala_size_x * 5.5);
			text.setAttribute('y', offset_y_scala + (index + 0.75) * scala_size_y / scalaNumber);
			text.setAttribute('fill', '#FFFFFF');
			text.textContent = value.toFixed(2) + ' mm';
			var svg1 = document.getElementById("svgLegend");
			svg1.appendChild(text);
		}
	}

	ModelRenderer.prototype.renderFixtures = function(node, x, y) {
		var size = 10;
		if (node.x_fixed) {
			var trianglePoints = "";
			trianglePoints += [ x, y ].join(',') + ' ';
			trianglePoints += [ x - size, y - size * 0.75 ].join(',') + ' ';
			trianglePoints += [ x - size, y + size * 0.75 ].join(',') + ' ';
			var elementSVG = this.getPolygonElementSVG('FIX_V' + node.id, "svgFixed");
			if (null != elementSVG) {
				elementSVG.setAttribute('points', trianglePoints.trim());
				elementSVG.setAttribute('style', "stroke: #FFFFFF; fill-opacity: 0.5");
			}
		}
		if (node.y_fixed) {
			var trianglePoints = "";
			trianglePoints += [ x, y ].join(',') + ' ';
			trianglePoints += [ x + size * 0.75, y + size ].join(',') + ' ';
			trianglePoints += [ x - size * 0.75, y + size ].join(',') + ' ';
			var elementSVG = this.getPolygonElementSVG('FIX_H' + node.id, "svgFixed");
			if (null != elementSVG) {
				elementSVG.setAttribute('points', trianglePoints.trim());
				elementSVG.setAttribute('style', "stroke: #FFFFFF; fill-opacity: 0.5");
			}
		}
	}

	ModelRenderer.prototype.renderForces = function(node, x, y) {
		var isSelectedElement = !OPTIONS.GRAVITY_ACTIVE && ('N' + (node.id)) == this.selecedNodeId;
		this.drawVector(x, y, x + node.x_force * OPTIONS.SCALE_FORCE, y, true, (node.x_force > 0.0), node.id, isSelectedElement, node.x_fixed);
		this.drawVector(x, y, x, y + node.y_force * OPTIONS.SCALE_FORCE, false, (node.y_force > 0.0), node.id, isSelectedElement, node.y_fixed);
	}

	ModelRenderer.prototype.renderElement = function(node, elementPoints, delta) {
		var elementSVG = this.getPolygonElementSVG('E' + node.idElement, "svgElements");
		if (null != elementSVG) {
			elementSVG.setAttribute('points', elementPoints.trim());
			elementSVG.setAttribute('style', "fill:" + this.getColor(delta) + ";");
		}
	}

	ModelRenderer.prototype.renderNode = function(node, x, y) {
		var elementSVG = this.getCircleElementSVG('N' + node.id, "svgNodes");
		if (null != elementSVG) {
			elementSVG.setAttribute('cx', x);
			elementSVG.setAttribute('cy', y);
			elementSVG.setAttribute('r', 4);
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

	ModelRenderer.prototype.setStatusLineText = function(value) {
		var text = document.getElementById("SL1");
		if (null == text) {
			text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
			text.setAttribute('id', "SL1");
			text.setAttribute('style', 'text-anchor: start;');
			text.setAttribute('fill', '#FFFFFF');
			text.setAttribute('x', 10);
			text.setAttribute('y', 600);
			var svg1 = document.getElementById("svgStatusLine");
			svg1.appendChild(text);
		}
		text.textContent = value;
	}

	ModelRenderer.prototype.setHeadLine = function(value) {
		var text = document.getElementById("TI1");
		if (null == text) {
			text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
			text.setAttribute('id', "TI1");
			text.setAttribute('style', 'text-anchor: start; font-size: 1.4em;');
			text.setAttribute('fill', '#FFFFFF');
			text.setAttribute('x', 10);
			text.setAttribute('y', 25);
			var svg1 = document.getElementById("svgHeadLine");
			svg1.appendChild(text);
		}
		text.textContent = value;

		var link = document.getElementById("SL2");
		if (null == link) {
			link = document.createElementNS('http://www.w3.org/2000/svg', 'text');
			link.setAttribute('id', "SL2");
			link.setAttribute('style', 'text-anchor: start;');
			link.setAttribute('fill', '#8181F7');
			link.setAttribute('x', 10);
			link.setAttribute('y', 45);
			var svg1 = document.getElementById("svgHeadLineLink");
			svg1.appendChild(link);
			link.textContent = "by Markus Sprunck"
		}
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
				OPTIONS.GRAVITY_ACTIVE = false;
			}, false);
			elementSVG.addEventListener('touchstart', function(event) {
				event.preventDefault();
				_that.mouseDownX = event.touches[0].clientX;
				_that.mouseDownY = event.touches[0].clientY;
				_that.selecedNodeIdLast = _that.selecedNodeId;
				_that.selecedNodeId = event.target.id;
				OPTIONS.GRAVITY_ACTIVE = false;
			}, false);

			elementSVG.addEventListener('mousemove', function(event) {
				event.preventDefault();
				_that.getCircleElementSVG(_that.activeNodeId, "svgNodes").setAttribute('style', "opacity:0.0");
				_that.activeNodeId = event.target.id;
				if (_that.activeNodeId != null) {
					if (_that.selecedNodeId != null) {
						_that.getCircleElementSVG(_that.selecedNodeId, "svgNodes").setAttribute('style', "fill:white; opacity:1.0");
					} else {
						_that.getCircleElementSVG(_that.activeNodeId, "svgNodes").setAttribute('style', "fill:white; opacity:0.8");
					}
				}
			}, false);

			document.getElementById(groupId).appendChild(elementSVG);
		}
		return elementSVG;
	}

	ModelRenderer.prototype.calculateColorRange = function(nodes) {
		this.minColor = 250.0;
		this.maxColor = -250.0;
		for (var ele = nodes.length - 1; ele >= 0; ele--) {
			var deltaX = (this.colorCode == 1) ? nodes[ele][0].x_d : -nodes[ele][0].y_d;
			this.minColor = Math.min(deltaX, this.minColor);
			this.maxColor = Math.max(deltaX, this.maxColor);
		}
		this.minColor = Math.min(this.minColor, -0.1);
		this.maxColor = Math.max(this.maxColor, 0.1);
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
				var factor = 1;
				_that.forceY = (y2 - _that.mouseDownY) * factor;
				_that.forceX = (x2 - _that.mouseDownX) * factor;
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
