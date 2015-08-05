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

function ModelRenderer() {

	// Parameter for legend
	this.scalaNumber = 18.0;
	this.scala_size_x = 15;
	this.scala_size_y = 300;
	this.offset_x_scala = 0.0;
	this.offset_y_scala = 80.0;

	this.offset_x = 70;
	this.offset_y = 480;

	this.factorForce = 0.02;
	this.factorDisplacement = 1.0;

	this.beta = 0.0;
	this.gamma = -60.0;

	this.rotate = false;
	this.display_scale = true;
	this.orientation = 'Normal portrait';

	this.minColor = 20;
	this.maxColor = -20;

	this.showDeltaX = false;

	ModelRenderer.prototype.getColor = function(mean) {
		mean = -1.8 * Math.PI / (this.maxColor - this.minColor) * mean;
		red = Math.sin(mean + 2) * 127 + 128;
		green = Math.sin(mean + 1) * 127 + 128;
		blue = Math.sin(mean * 1.5 + 4) * 127 + 128;
		return '#' + toHex(red) + toHex(green) + toHex(blue);
	}

	function toHex(n) {
		return "0123456789ABCDEF".charAt((n - n % 16) / 16) + "0123456789ABCDEF".charAt(n % 16);
	}

	ModelRenderer.prototype.draw_scala_color = function() {
		var delta_y = this.scala_size_y / this.scalaNumber;

		for (var index = 0; index <= this.scalaNumber; index++) {
			var value = this.maxColor - (this.maxColor - this.minColor) * index / this.scalaNumber;
			var pointsSVG = "";
			pointsSVG += [ this.offset_x_scala, this.offset_y_scala + (index + 1) * delta_y ].join(',') + ' ';
			pointsSVG += [ this.offset_x_scala, this.offset_y_scala + index * delta_y ].join(',') + ' ';
			pointsSVG += [ this.offset_x_scala + this.scala_size_x, this.offset_y_scala + index * delta_y ].join(',') + ' ';
			pointsSVG += [ this.offset_x_scala + this.scala_size_x, this.offset_y_scala + (index + 1) * delta_y ].join(',') + ' ';
			var elementSVG = this.getPoygonElementSVG(index, "svgLegend");
			if (null != elementSVG) {
				elementSVG.setAttribute('points', pointsSVG.trim());
				elementSVG.setAttribute('style', "fill: " + this.getColor(value) + "; stroke: " + this.getColor(value));
			}

			var text = document.getElementById("LT1" + index);
			if (null == text) {
				text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
				text.setAttribute('id', "LT1" + index);
			}
			text.setAttribute('x', this.offset_x_scala + this.scala_size_x * 3.5);
			text.setAttribute('y', this.offset_y_scala + (index + 0.75) * this.scala_size_y / this.scalaNumber);
			text.setAttribute('fill', '#FFFFFF');
			text.textContent = value.toFixed(2);
			var svg1 = document.getElementById("svgLegend");
			svg1.appendChild(text);
		}
	}

	ModelRenderer.prototype.setStatusLineText = function(value) {
		var text = document.getElementById("SL1");
		if (null == text) {
			text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
			text.setAttribute('id', "SL1");
			text.setAttribute('style', 'text-anchor: start;');
			text.setAttribute('fill', '#FFFFFF');
			text.setAttribute('x', this.offset_x_scala);
			text.setAttribute('y', this.offset_y + 60);
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
			text.setAttribute('x', this.offset_x_scala);
			text.setAttribute('y', 15);
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
			link.setAttribute('x', this.offset_x_scala);
			link.setAttribute('y', 35);
			var svg1 = document.getElementById("svgHeadLineLink");
			svg1.appendChild(link);
			link.textContent = "by Markus Sprunck"
		}
	}

	ModelRenderer.prototype.getPoygonElementSVG = function(id, groupId) {
		var elementSVG = document.getElementById(id);
		if (null == elementSVG) {
			elementSVG = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
			elementSVG.setAttribute('id', id);
			var title = document.createElementNS('http://www.w3.org/2000/svg', 'title');
			title.innerHTML = id;
			elementSVG.appendChild(title);
			var svg1 = document.getElementById(groupId);
			svg1.appendChild(elementSVG);
		}
		return elementSVG;
	}

	ModelRenderer.prototype.drawFixedVerticalSVG = function(x, y, nodeId) {
		var length = 8;
		var pointsSVG = "";
		pointsSVG += [ x, y ].join(',') + ' ';
		pointsSVG += [ x - length, y - length * 0.75 ].join(',') + ' ';
		pointsSVG += [ x - length, y + length * 0.75 ].join(',') + ' ';
		var elementSVG = this.getPoygonElementSVG('FIX_V' + nodeId, "svgFixed");
		if (null != elementSVG) {
			elementSVG.setAttribute('points', pointsSVG.trim());
			elementSVG.setAttribute('style', "stroke: #FFFFFF; fill-opacity: 0.5");
		}
	}

	ModelRenderer.prototype.drawFixedHorizontalSVG = function(x, y, nodeId) {
		var length = 8;
		var pointsSVG = "";
		pointsSVG += [ x, y ].join(',') + ' ';
		pointsSVG += [ x + length * 0.75, y + length ].join(',') + ' ';
		pointsSVG += [ x - length * 0.75, y + length ].join(',') + ' ';
		var elementSVG = this.getPoygonElementSVG('FIX_H' + nodeId, "svgFixed");
		if (null != elementSVG) {
			elementSVG.setAttribute('points', pointsSVG.trim());
			elementSVG.setAttribute('style', "stroke: #FFFFFF; fill-opacity: 0.5");
		}

	}

	ModelRenderer.prototype.draw_elements = function(elements) {

		// calculate color range
		this.minColor = 250.0;
		this.maxColor = -250.0;
		for (var ele = elements.length - 1; ele >= 0; ele--) {
			var deltaArea = elements[ele][0].deltaArea;
			this.minColor = Math.min(deltaArea / 3.0, this.minColor);
			this.maxColor = Math.max(deltaArea / 3.0, this.maxColor);
		}

		// render elements
		for (var ele = elements.length - 1; ele >= 0; ele--) {
			var pointsSVG = "";
			var deltaArea = elements[ele][0].deltaArea;
			for (var nodeId = 0; nodeId < 3; nodeId++) {
				element = elements[ele][nodeId];
				var x = this.offset_x + element.x + element.x_d * this.factorDisplacement;
				var y = this.offset_y + element.y + element.y_d * this.factorDisplacement;
				pointsSVG += [ x, y ].join(',') + ' ';
				if (element.x_fixed) {
					this.drawFixedVerticalSVG(x, y, element.id);
					this.drawVector(x, y, x + element.x_force * this.factorForce, y, true, (element.x_force > 0.0), element.id);
				}
				if (element.y_fixed) {
					this.drawFixedHorizontalSVG(x, y, element.id);
					this.drawVector(x, y, x, y + element.y_force * this.factorForce, false, (element.y_force > 0.0), element.id);
				}
			}
			var elementSVG = this.getPoygonElementSVG('E' + (ele + 1), "svgElements");
			if (null != elementSVG) {
				elementSVG.setAttribute('points', pointsSVG.trim());
				elementSVG.setAttribute('style', "fill:" + this.getColor(deltaArea / 3.0) + ";");
			}
		}

		if (this.display_scale) {
			this.draw_scala_color();
		}
	}

	ModelRenderer.prototype.drawVector = function(startX, startY, endX, endY, horizontal, positive, ele) {

		var length = 5;
		var isVisible = Math.abs(startX - endX) + Math.abs(startY - endY) > length;

		var pointsSVG = "";
		if (isVisible) {
			pointsSVG += [ startX, startY ].join(',') + ' ';
			pointsSVG += [ endX, endY ].join(',') + ' ';

			if (horizontal && !positive) {
				pointsSVG += [ endX + length, endY - length ].join(',') + ' ';
				pointsSVG += [ endX, endY ].join(',') + ' ';
				pointsSVG += [ endX + length, endY + length ].join(',') + ' ';
				pointsSVG += [ endX, endY ].join(',') + ' ';
			} else if (horizontal && positive) {
				pointsSVG += [ endX - length, endY - length ].join(',') + ' ';
				pointsSVG += [ endX, endY ].join(',') + ' ';
				pointsSVG += [ endX - length, endY + length ].join(',') + ' ';
				pointsSVG += [ endX, endY ].join(',') + ' ';
			} else if (!horizontal && positive) {
				pointsSVG += [ endX - length, endY - length ].join(',') + ' ';
				pointsSVG += [ endX, endY ].join(',') + ' ';
				pointsSVG += [ endX + length, endY - length ].join(',') + ' ';
				pointsSVG += [ endX, endY ].join(',') + ' ';
			} else if (!horizontal && !positive) {
				pointsSVG += [ endX - length, endY + length ].join(',') + ' ';
				pointsSVG += [ endX, endY ].join(',') + ' ';
				pointsSVG += [ endX + length, endY + length ].join(',') + ' ';
				pointsSVG += [ endX, endY ].join(',') + ' ';
			}
		}
		var elementSVG = this.getPoygonElementSVG('Arrow_' + horizontal + (ele + 1), "svgArrows");
		if (null != elementSVG) {
			elementSVG.setAttribute('points', pointsSVG.trim());
			elementSVG.setAttribute('style', "stroke:#FF0000;stroke-width: 1.0; visibility:" + ((isVisible) ? "visible" : "hidden"));
		}
	}

}
