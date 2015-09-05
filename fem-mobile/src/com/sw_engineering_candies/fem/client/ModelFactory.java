/**
 * Copyright (C) 2015, Markus Sprunck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote
 * products derived from this software without specific prior
 * written permission.
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

public class ModelFactory {

	public static String createEiffelTowerModel() {
		final StringBuilder result = new StringBuilder();
		result.append("N 1 0 0").append("\n");
		result.append("N 2 11 0").append("\n");
		result.append("N 3 8 5").append("\n");
		result.append("N 4 5 8.5").append("\n");
		result.append("N 5 15 8.5").append("\n");
		result.append("N 6 12 12").append("\n");
		result.append("N 7 8.3 15").append("\n");
		result.append("N 8 18 15").append("\n");
		result.append("N 9 15 18").append("\n");
		result.append("N 10 11.5 20.5").append("\n");
		result.append("N 11 20.5 20.5").append("\n");
		result.append("N 12 29 20.5").append("\n");
		result.append("N 13 37.7 22").append("\n");
		result.append("N 14 47 20.5").append("\n");
		result.append("N 15 58 15").append("\n");
		result.append("N 16 55 20.5").append("\n");
		result.append("N 17 64.5 20.5").append("\n");
		result.append("N 18 61.3 18").append("\n");
		result.append("N 19 67.5 15").append("\n");
		result.append("N 20 64.5 12").append("\n");
		result.append("N 21 61.5 8.5").append("\n");
		result.append("N 22 71 8.5").append("\n");
		result.append("N 23 68 5").append("\n");
		result.append("N 24 65 0").append("\n");
		result.append("N 25 76 0").append("\n");
		result.append("N 26 18 24").append("\n");
		result.append("N 27 27 24").append("\n");
		result.append("N 28 34.5 24").append("\n");
		result.append("N 29 41.7 24").append("\n");
		result.append("N 30 49.5 24").append("\n");
		result.append("N 31 58 24").append("\n");
		result.append("N 32 15 27.5").append("\n");
		result.append("N 33 24 27.5").append("\n");
		result.append("N 34 31 27.5").append("\n");
		result.append("N 35 37.7 27.5").append("\n");
		result.append("N 36 45 27.5").append("\n");
		result.append("N 37 52 27.5").append("\n");
		result.append("N 38 61.5 27.5").append("\n");
		result.append("N 39 20.7 30.5").append("\n");
		result.append("N 40 28.5 30.5").append("\n");
		result.append("N 41 35 30.5").append("\n");
		result.append("N 42 41 30.5").append("\n");
		result.append("N 43 47.5 30.5").append("\n");
		result.append("N 44 55.3 30.5").append("\n");
		result.append("N 45 13.5 33").append("\n");
		result.append("N 46 17.1 33").append("\n");
		result.append("N 47 27 33").append("\n");
		result.append("N 48 32 33").append("\n");
		result.append("N 49 37.7 33").append("\n");
		result.append("N 50 44 33").append("\n");
		result.append("N 51 50 33").append("\n");
		result.append("N 52 59.5 33").append("\n");
		result.append("N 53 63 33").append("\n");
		result.append("N 54 23 37").append("\n");
		result.append("N 55 53.5 37").append("\n");
		result.append("N 56 19.5 40").append("\n");
		result.append("N 57 28.5 40").append("\n");
		result.append("N 58 48 40").append("\n");
		result.append("N 59 57 40").append("\n");
		result.append("N 60 25 43").append("\n");
		result.append("N 61 51.2 43").append("\n");
		result.append("N 62 21.2 46").append("\n");
		result.append("N 63 30 46").append("\n");
		result.append("N 64 46 46").append("\n");
		result.append("N 65 55 46").append("\n");
		result.append("N 66 26 49").append("\n");
		result.append("N 67 49 49").append("\n");
		result.append("N 68 23.2 53").append("\n");
		result.append("N 69 31.4 53").append("\n");
		result.append("N 70 44 53").append("\n");
		result.append("N 71 53 53").append("\n");
		result.append("N 72 28 56").append("\n");
		result.append("N 73 48 56").append("\n");
		result.append("N 74 25 59").append("\n");
		result.append("N 75 33 59").append("\n");
		result.append("N 76 42.5 59").append("\n");
		result.append("N 77 51 59").append("\n");
		result.append("N 78 29.5 62").append("\n");
		result.append("N 79 37.7 62").append("\n");
		result.append("N 80 46.3 62").append("\n");
		result.append("N 81 27 65").append("\n");
		result.append("N 82 33.5 65").append("\n");
		result.append("N 83 43 65").append("\n");
		result.append("N 84 49 65").append("\n");
		result.append("N 85 30.5 67").append("\n");
		result.append("N 86 37.7 67").append("\n");
		result.append("N 87 45.5 67").append("\n");
		result.append("N 88 25 69").append("\n");
		result.append("N 89 27.8 69").append("\n");
		result.append("N 90 33.8 69").append("\n");
		result.append("N 91 42.5 69").append("\n");
		result.append("N 92 48 69").append("\n");
		result.append("N 93 51 69").append("\n");
		result.append("N 94 31 72").append("\n");
		result.append("N 95 37.7 72").append("\n");
		result.append("N 96 45 72").append("\n");
		result.append("N 97 29 75").append("\n");
		result.append("N 98 35 75").append("\n");
		result.append("N 99 41.5 75").append("\n");
		result.append("N 100 47 75").append("\n");
		result.append("N 101 32 78").append("\n");
		result.append("N 102 37.7 78").append("\n");
		result.append("N 103 44 78").append("\n");
		result.append("N 104 30 81").append("\n");
		result.append("N 105 35 81").append("\n");
		result.append("N 106 40.5 81").append("\n");
		result.append("N 107 46 81").append("\n");
		result.append("N 108 33 84.5").append("\n");
		result.append("N 109 37.7 84.9").append("\n");
		result.append("N 110 43 84.5").append("\n");
		result.append("N 111 30.5 88").append("\n");
		result.append("N 112 36 88").append("\n");
		result.append("N 113 40 88").append("\n");
		result.append("N 114 45.5 88").append("\n");
		result.append("N 115 33.5 91").append("\n");
		result.append("N 116 37.7 91.5").append("\n");
		result.append("N 117 42.5 91").append("\n");
		result.append("N 118 31 94.5").append("\n");
		result.append("N 119 36.5 94.5").append("\n");
		result.append("N 120 39.5 94.5").append("\n");
		result.append("N 121 45 94.5").append("\n");
		result.append("N 122 34 97.5").append("\n");
		result.append("N 123 41.5 97.5").append("\n");
		result.append("N 124 31.5 101").append("\n");
		result.append("N 125 37.7 101").append("\n");
		result.append("N 126 44.3 101").append("\n");
		result.append("N 127 35 104").append("\n");
		result.append("N 128 41 104").append("\n");
		result.append("N 129 32 107").append("\n");
		result.append("N 130 37.7 107").append("\n");
		result.append("N 131 44 107").append("\n");
		result.append("N 132 35 110").append("\n");
		result.append("N 133 41 110").append("\n");
		result.append("N 134 32.1 113").append("\n");
		result.append("N 135 37.7 113").append("\n");
		result.append("N 136 43.5 113").append("\n");
		result.append("N 137 35 116").append("\n");
		result.append("N 138 41 116").append("\n");
		result.append("N 139 32.1 119").append("\n");
		result.append("N 140 37.7 119").append("\n");
		result.append("N 141 43.3 119").append("\n");
		result.append("N 142 35 121.5").append("\n");
		result.append("N 143 40.5 121.5").append("\n");
		result.append("N 144 32.2 124").append("\n");
		result.append("N 145 37.7 124").append("\n");
		result.append("N 146 43 124").append("\n");
		result.append("N 147 35.5 126.5").append("\n");
		result.append("N 148 40.5 126.5").append("\n");
		result.append("N 149 32.4 129").append("\n");
		result.append("N 150 37.7 129").append("\n");
		result.append("N 151 43 129").append("\n");
		result.append("N 152 35.5 131.6").append("\n");
		result.append("N 153 39.8 131.6").append("\n");
		result.append("N 154 32.8 134").append("\n");
		result.append("N 155 37.7 134").append("\n");
		result.append("N 156 42.7 134").append("\n");
		result.append("N 157 35.5 137").append("\n");
		result.append("N 158 40 137").append("\n");
		result.append("N 159 33.1 139").append("\n");
		result.append("N 160 37.7 139").append("\n");
		result.append("N 161 42.3 139").append("\n");
		result.append("N 162 36 141.5").append("\n");
		result.append("N 163 40 141.5").append("\n");
		result.append("N 164 33.7 143.5").append("\n");
		result.append("N 165 37.7 143.5").append("\n");
		result.append("N 166 42 143.5").append("\n");
		result.append("N 167 36 145.5").append("\n");
		result.append("N 168 40 145.5").append("\n");
		result.append("N 169 34.0 148").append("\n");
		result.append("N 170 37.7 148").append("\n");
		result.append("N 171 41.7 148").append("\n");
		result.append("N 172 36.1 149.8").append("\n");
		result.append("N 173 39.7 149.8").append("\n");
		result.append("N 174 34.4 151.8").append("\n");
		result.append("N 175 37.7 151.8").append("\n");
		result.append("N 176 41.2 151.8").append("\n");
		result.append("N 177 36 154").append("\n");
		result.append("N 178 39.6 154").append("\n");
		result.append("N 179 34.8 156").append("\n");
		result.append("N 180 37.7 156").append("\n");
		result.append("N 181 40.9 156").append("\n");
		result.append("N 182 36.1  158").append("\n");
		result.append("N 183 39.5 158").append("\n");
		result.append("N 184 35.0 160").append("\n");
		result.append("N 185 37.7 160").append("\n");
		result.append("N 186 40.8 160").append("\n");
		result.append("N 187 36 161.5").append("\n");
		result.append("N 188 39.8 161.5").append("\n");
		result.append("N 189 32 165").append("\n");
		result.append("N 190 37.7 165").append("\n");
		result.append("N 191 43.8 165").append("\n");
		result.append("N 192 32 167.5").append("\n");
		result.append("N 193 33.9 167.5").append("\n");
		result.append("N 194 37.7 167.5").append("\n");
		result.append("N 195 41.7 167.5").append("\n");
		result.append("N 196 43.8 167.5").append("\n");
		result.append("N 197 36 169").append("\n");
		result.append("N 198 40 169").append("\n");
		result.append("N 199 33.9 171").append("\n");
		result.append("N 200 37.7 171").append("\n");
		result.append("N 201 41.7 171").append("\n");
		result.append("N 202 37.7 175").append("\n");
		result.append("E 1 1 3 2").append("\n");
		result.append("E 2 2 3 5").append("\n");
		result.append("E 3 3 4 5").append("\n");
		result.append("E 4 1 4 3").append("\n");
		result.append("E 5 4 6 5").append("\n");
		result.append("E 6 5 6 8").append("\n");
		result.append("E 7 6 7 8").append("\n");
		result.append("E 8 4 7 6").append("\n");
		result.append("E 9 7 10 9").append("\n");
		result.append("E 10 7 9 8").append("\n");
		result.append("E 11 8 9 11").append("\n");
		result.append("E 12 9 10 11").append("\n");
		result.append("E 13 8 11 12").append("\n");
		result.append("E 14 10 26 11").append("\n");
		result.append("E 15 11 26 33").append("\n");
		result.append("E 16 33 26 32").append("\n");
		result.append("E 17 32 26 10").append("\n");
		result.append("E 18 11 27 12").append("\n");
		result.append("E 19 12 27 34").append("\n");
		result.append("E 20 27 33 34").append("\n");
		result.append("E 21 11 33 27").append("\n");
		result.append("E 22 12 28 13").append("\n");
		result.append("E 23 13 28 35").append("\n");
		result.append("E 24 28 34 35").append("\n");
		result.append("E 25 12 34 28").append("\n");
		result.append("E 26 13 29 14").append("\n");
		result.append("E 27 14 29 36").append("\n");
		result.append("E 28 29 35 36").append("\n");
		result.append("E 29 13 35 29").append("\n");
		result.append("E 30 14 30 16").append("\n");
		result.append("E 31 16 30 37").append("\n");
		result.append("E 32 30 36 37").append("\n");
		result.append("E 33 14 36 30").append("\n");
		result.append("E 34 16 31 17").append("\n");
		result.append("E 35 31 38 17").append("\n");
		result.append("E 36 31 37 38").append("\n");
		result.append("E 37 16 37 31").append("\n");
		result.append("E 38 15 14 16").append("\n");
		result.append("E 39 15 18 19").append("\n");
		result.append("E 40 19 18 17").append("\n");
		result.append("E 41 18 16 17").append("\n");
		result.append("E 42 15 16 18").append("\n");
		result.append("E 43 20 15 19").append("\n");
		result.append("E 44 21 15 20").append("\n");
		result.append("E 45 22 20 19").append("\n");
		result.append("E 46 21 20 22").append("\n");
		result.append("E 47 21 22 23").append("\n");
		result.append("E 48 24 21 23").append("\n");
		result.append("E 49 25 23 22").append("\n");
		result.append("E 50 24 23 25").append("\n");
		result.append("E 51 32 45 46").append("\n");
		result.append("E 52 32 46 39").append("\n");
		result.append("E 53 32 39 33").append("\n");
		result.append("E 54 33 39 47").append("\n");
		result.append("E 55 39 46 47").append("\n");
		result.append("E 56 33 40 34").append("\n");
		result.append("E 57 34 40 48").append("\n");
		result.append("E 58 40 47 48").append("\n");
		result.append("E 59 33 47 40").append("\n");
		result.append("E 60 34 41 35").append("\n");
		result.append("E 61 35 41 49").append("\n");
		result.append("E 62 41 48 49").append("\n");
		result.append("E 63 34 48 41").append("\n");
		result.append("E 64 35 42 36").append("\n");
		result.append("E 65 42 50 36").append("\n");
		result.append("E 66 42 49 50").append("\n");
		result.append("E 67 35 49 42").append("\n");
		result.append("E 68 36 43 37").append("\n");
		result.append("E 69 37 43 51").append("\n");
		result.append("E 70 43 50 51").append("\n");
		result.append("E 71 36 50 43").append("\n");
		result.append("E 72 37 44 38").append("\n");
		result.append("E 73 44 52 38").append("\n");
		result.append("E 74 44 51 52").append("\n");
		result.append("E 75 37 51 44").append("\n");
		result.append("E 76 38 52 53").append("\n");
		result.append("E 77 47 46 54").append("\n");
		result.append("E 78 47 54 57").append("\n");
		result.append("E 79 54 56 57").append("\n");
		result.append("E 80 46 56 54").append("\n");
		result.append("E 81 51 55 52").append("\n");
		result.append("E 82 55 59 52").append("\n");
		result.append("E 83 55 58 59").append("\n");
		result.append("E 84 51 58 55").append("\n");
		result.append("E 85 56 60 57").append("\n");
		result.append("E 86 57 60 63").append("\n");
		result.append("E 87 60 62 63").append("\n");
		result.append("E 88 56 62 60").append("\n");
		result.append("E 89 58 61 59").append("\n");
		result.append("E 90 61 65 59").append("\n");
		result.append("E 91 61 64 65").append("\n");
		result.append("E 92 58 64 61").append("\n");
		result.append("E 93 62 66 63").append("\n");
		result.append("E 94 63 66 69").append("\n");
		result.append("E 95 69 66 68").append("\n");
		result.append("E 96 62 68 66").append("\n");
		result.append("E 97 64 67 65").append("\n");
		result.append("E 98 67 71 65").append("\n");
		result.append("E 99 70 71 67").append("\n");
		result.append("E 100 64 70 67").append("\n");
		result.append("E 101 68 72 69").append("\n");
		result.append("E 102 69 72 75").append("\n");
		result.append("E 103 72 74 75").append("\n");
		result.append("E 104 72 68 74").append("\n");
		result.append("E 105 70 73 71").append("\n");
		result.append("E 106 71 73 77").append("\n");
		result.append("E 107 73 76 77").append("\n");
		result.append("E 108 70 76 73").append("\n");
		result.append("E 109 74 78 75").append("\n");
		result.append("E 110 78 82 75").append("\n");
		result.append("E 111 78 81 82").append("\n");
		result.append("E 112 74 81 78").append("\n");
		result.append("E 113 75 79 76").append("\n");
		result.append("E 114 79 83 76").append("\n");
		result.append("E 115 79 82 83").append("\n");
		result.append("E 116 79 75 82").append("\n");
		result.append("E 117 76 80 77").append("\n");
		result.append("E 118 80 84 77").append("\n");
		result.append("E 119 80 83 84").append("\n");
		result.append("E 120 80 76 83").append("\n");
		result.append("E 121 84 92 93").append("\n");
		result.append("E 122 81 85 82").append("\n");
		result.append("E 123 82 85 90").append("\n");
		result.append("E 124 85 89 90").append("\n");
		result.append("E 125 81 89 85").append("\n");
		result.append("E 126 81 88 89").append("\n");
		result.append("E 127 82 86 83").append("\n");
		result.append("E 128 83 86 91").append("\n");
		result.append("E 129 86 90 91").append("\n");
		result.append("E 130 86 82 90").append("\n");
		result.append("E 131 83 87 84").append("\n");
		result.append("E 132 84 87 92").append("\n");
		result.append("E 133 87 91 92").append("\n");
		result.append("E 134 87 83 91").append("\n");
		result.append("E 135 89 94 90").append("\n");
		result.append("E 136 90 94 98").append("\n");
		result.append("E 137 94 97 98").append("\n");
		result.append("E 138 89 97 94").append("\n");
		result.append("E 139 90 95 91").append("\n");
		result.append("E 140 91 95 99").append("\n");
		result.append("E 141 95 98 99").append("\n");
		result.append("E 142 90 98 95").append("\n");
		result.append("E 143 91 96 92").append("\n");
		result.append("E 144 92 96 100").append("\n");
		result.append("E 145 96 99 100").append("\n");
		result.append("E 146 91 99 96").append("\n");
		result.append("E 147 97 101 98").append("\n");
		result.append("E 148 98 102 99").append("\n");
		result.append("E 149 100 99 103").append("\n");
		result.append("E 150 97 104 101").append("\n");
		result.append("E 151 98 101 105").append("\n");
		result.append("E 152 102 98 105").append("\n");
		result.append("E 153 99 102 106").append("\n");
		result.append("E 154 99 106 103").append("\n");
		result.append("E 155 100 103 107").append("\n");
		result.append("E 156 101 104 105").append("\n");
		result.append("E 157 102 105 106").append("\n");
		result.append("E 158 103 106 107").append("\n");
		result.append("E 159 104 108 105").append("\n");
		result.append("E 160 105 109 106").append("\n");
		result.append("E 161 106 110 107").append("\n");
		result.append("E 162 104 111 108").append("\n");
		result.append("E 163 105 108 112").append("\n");
		result.append("E 164 105 112 109").append("\n");
		result.append("E 165 106 109 113").append("\n");
		result.append("E 166 106 113 110").append("\n");
		result.append("E 167 110 114 107").append("\n");
		result.append("E 168 108 111 112").append("\n");
		result.append("E 169 109 112 113").append("\n");
		result.append("E 170 110 113 114").append("\n");
		result.append("E 171 111 115 112").append("\n");
		result.append("E 172 112 116 113").append("\n");
		result.append("E 173 113 117 114").append("\n");
		result.append("E 174 111 118 115").append("\n");
		result.append("E 175 112 115 119").append("\n");
		result.append("E 176 112 119 116").append("\n");
		result.append("E 177 113 116 120").append("\n");
		result.append("E 178 113 120 117").append("\n");
		result.append("E 179 114 117 121").append("\n");
		result.append("E 180 115 118 119").append("\n");
		result.append("E 181 116 119 120 ").append("\n");
		result.append("E 182 117 120 121").append("\n");
		result.append("E 183 118 122 119").append("\n");
		result.append("E 184 119 125 120").append("\n");
		result.append("E 185 120 123 121").append("\n");
		result.append("E 186 118 124 122").append("\n");
		result.append("E 187 119 122 125").append("\n");
		result.append("E 188 120 125 123").append("\n");
		result.append("E 189 121 123 126").append("\n");
		result.append("E 190 122 124 125").append("\n");
		result.append("E 191 123 125 126").append("\n");
		result.append("E 192 124 127 125").append("\n");
		result.append("E 193 125 128 126").append("\n");
		result.append("E 194 124 129 127").append("\n");
		result.append("E 195 125 127 130").append("\n");
		result.append("E 196 125 130 128").append("\n");
		result.append("E 197 126 128 131").append("\n");
		result.append("E 198 127 129 130").append("\n");
		result.append("E 199 128 130 131").append("\n");
		result.append("E 200 129 132 130").append("\n");
		result.append("E 201 130 133 131").append("\n");
		result.append("E 202 129 134 132").append("\n");
		result.append("E 203 130 132 135").append("\n");
		result.append("E 204 130 135 133").append("\n");
		result.append("E 205 133 136 131").append("\n");
		result.append("E 206 132 134 135").append("\n");
		result.append("E 207 133 135 136").append("\n");
		result.append("E 208 134 137 135").append("\n");
		result.append("E 209 135 138 136").append("\n");
		result.append("E 210 134 139 137").append("\n");
		result.append("E 211 135 137 140").append("\n");
		result.append("E 212 135 140 138").append("\n");
		result.append("E 213 136 138 141").append("\n");
		result.append("E 214 137 139 140").append("\n");
		result.append("E 215 138 140 141").append("\n");
		result.append("E 216 139 142 140").append("\n");
		result.append("E 217 140 143 141").append("\n");
		result.append("E 218 139 144 142").append("\n");
		result.append("E 219 140 142 145").append("\n");
		result.append("E 220 140 145 143").append("\n");
		result.append("E 221 143 146 141").append("\n");
		result.append("E 222 144 145 142").append("\n");
		result.append("E 223 143 145 146").append("\n");
		result.append("E 224 144 147 145").append("\n");
		result.append("E 225 145 148 146").append("\n");
		result.append("E 226 144 149 147").append("\n");
		result.append("E 227 145 147 150").append("\n");
		result.append("E 228 145 150 148").append("\n");
		result.append("E 229 148 151 146").append("\n");
		result.append("E 230 147 149 150").append("\n");
		result.append("E 231 148 150 151").append("\n");
		result.append("E 232 149 152 150").append("\n");
		result.append("E 233 150 153 151").append("\n");
		result.append("E 234 149 154 152").append("\n");
		result.append("E 235 150 152 155").append("\n");
		result.append("E 236 150 155 153").append("\n");
		result.append("E 237 151 153 156").append("\n");
		result.append("E 238 152 154 155").append("\n");
		result.append("E 239 153 155 156").append("\n");
		result.append("E 240 154 157 155").append("\n");
		result.append("E 241 155 158 156").append("\n");
		result.append("E 242 154 159 157").append("\n");
		result.append("E 243 155 157 160").append("\n");
		result.append("E 244 155 160 158").append("\n");
		result.append("E 245 156 158 161").append("\n");
		result.append("E 246 157 159 160").append("\n");
		result.append("E 247 158 160 161").append("\n");
		result.append("E 248 159 162 160").append("\n");
		result.append("E 249 160 163 161").append("\n");
		result.append("E 250 159 164 162").append("\n");
		result.append("E 251 160 162 165").append("\n");
		result.append("E 252 160 165 163").append("\n");
		result.append("E 253 161 163 166").append("\n");
		result.append("E 254 162 164 165").append("\n");
		result.append("E 255 163 165 166").append("\n");
		result.append("E 256 164 167 165").append("\n");
		result.append("E 257 165 168 166").append("\n");
		result.append("E 258 164 169 167").append("\n");
		result.append("E 259 165 167 170").append("\n");
		result.append("E 260 165 170 168").append("\n");
		result.append("E 261 166 168 171").append("\n");
		result.append("E 262 169 170 167").append("\n");
		result.append("E 263 168 170 171").append("\n");
		result.append("E 264 169 172 170").append("\n");
		result.append("E 265 170 173 171").append("\n");
		result.append("E 266 169 174 172").append("\n");
		result.append("E 267 170 172 175").append("\n");
		result.append("E 268 170 175 173").append("\n");
		result.append("E 269 171 173 176").append("\n");
		result.append("E 270 172 174 175").append("\n");
		result.append("E 271 173 175 176").append("\n");
		result.append("E 272 174 177 175").append("\n");
		result.append("E 273 175 178 176").append("\n");
		result.append("E 274 174 179 177").append("\n");
		result.append("E 275 175 177 180").append("\n");
		result.append("E 276 175 180 178").append("\n");
		result.append("E 277 176 178 181").append("\n");
		result.append("E 278 177 179 180").append("\n");
		result.append("E 279 178 180 181").append("\n");
		result.append("E 280 179 182 180").append("\n");
		result.append("E 281 180 183 181").append("\n");
		result.append("E 282 179 184 182").append("\n");
		result.append("E 283 180 182 185").append("\n");
		result.append("E 284 180 185 183").append("\n");
		result.append("E 285 181 183 186").append("\n");
		result.append("E 286 182 184 185").append("\n");
		result.append("E 287 183 185 186").append("\n");
		result.append("E 288 184 187 185").append("\n");
		result.append("E 289 185 188 186").append("\n");
		result.append("E 290 184 189 187").append("\n");
		result.append("E 291 185 187 190").append("\n");
		result.append("E 292 185 190 188").append("\n");
		result.append("E 293 186 188 191").append("\n");
		result.append("E 294 187 189 190").append("\n");
		result.append("E 295 188 190 191").append("\n");
		result.append("E 296 189 192 193").append("\n");
		result.append("E 297 189 193 190").append("\n");
		result.append("E 298 190 193 194").append("\n");
		result.append("E 299 190 194 195").append("\n");
		result.append("E 300 190 195 191").append("\n");
		result.append("E 301 191 195 196").append("\n");
		result.append("E 302 193 197 194").append("\n");
		result.append("E 303 194 198 195").append("\n");
		result.append("E 304 193 199 197").append("\n");
		result.append("E 305 194 197 200").append("\n");
		result.append("E 306 194 200 198").append("\n");
		result.append("E 307 195 198 201").append("\n");
		result.append("E 308 197 199 200").append("\n");
		result.append("E 309 198 200 201").append("\n");
		result.append("E 310 199 202 200").append("\n");
		result.append("E 311 200 202 201").append("\n");
		result.append("D 1 x 0.0").append("\n");
		result.append("D 1 y 0.0").append("\n");
		result.append("D 2 x 0.0").append("\n");
		result.append("D 2 y 0.0").append("\n");
		result.append("D 24 x 0.0").append("\n");
		result.append("D 24 y 0.0").append("\n");
		result.append("D 25 x 0.0").append("\n");
		result.append("D 25 y 0.0").append("\n");
		result.append("F 13 y 0.01").append("\n");
		return result.toString();
	}

	public static String createEiffelTowerModelBase() {
		final StringBuilder result = new StringBuilder();
		result.append("N 1 0 0").append("\n");
		result.append("N 2 11 0").append("\n");
		result.append("N 3 8 5").append("\n");
		result.append("N 4 5 8.5").append("\n");
		result.append("N 5 15 8.5").append("\n");
		result.append("N 6 12 12").append("\n");
		result.append("N 7 8.3 15").append("\n");
		result.append("N 8 18 15").append("\n");
		result.append("N 9 15 18").append("\n");
		result.append("N 10 11.5 20.5").append("\n");
		result.append("N 11 20.5 20.5").append("\n");
		result.append("N 12 29 20.5").append("\n");
		result.append("N 13 37.7 22").append("\n");
		result.append("N 14 47 20.5").append("\n");
		result.append("N 15 58 15").append("\n");
		result.append("N 16 55 20.5").append("\n");
		result.append("N 17 64.5 20.5").append("\n");
		result.append("N 18 61.3 18").append("\n");
		result.append("N 19 67.5 15").append("\n");
		result.append("N 20 64.5 12").append("\n");
		result.append("N 21 61.5 8.5").append("\n");
		result.append("N 22 71 8.5").append("\n");
		result.append("N 23 68 5").append("\n");
		result.append("N 24 65 0").append("\n");
		result.append("N 25 76 0").append("\n");
		result.append("N 26 18 24").append("\n");
		result.append("N 27 27 24").append("\n");
		result.append("N 28 34.5 24").append("\n");
		result.append("N 29 41.7 24").append("\n");
		result.append("N 30 49.5 24").append("\n");
		result.append("N 31 58 24").append("\n");
		result.append("N 32 15 27.5").append("\n");
		result.append("N 33 24 27.5").append("\n");
		result.append("N 34 31 27.5").append("\n");
		result.append("N 35 37.7 27.5").append("\n");
		result.append("N 36 45 27.5").append("\n");

		result.append("E 1 1 3 2").append("\n");
		result.append("E 2 2 3 5").append("\n");
		result.append("E 3 3 4 5").append("\n");
		result.append("E 4 1 4 3").append("\n");
		result.append("E 5 4 6 5").append("\n");
		result.append("E 6 5 6 8").append("\n");
		result.append("E 7 6 7 8").append("\n");
		result.append("E 8 4 7 6").append("\n");
		result.append("E 9 7 10 9").append("\n");
		result.append("E 10 7 9 8").append("\n");
		result.append("E 11 8 9 11").append("\n");
		result.append("E 12 9 10 11").append("\n");

		result.append("D 1 x 0.0").append("\n");
		result.append("D 1 y 0.0").append("\n");
		result.append("D 2 y 0.0").append("\n");
		result.append("F 13 y 0.01").append("\n");
		return result.toString();
	}

	public static String createDefaultModel() {
		final StringBuffer nodeText = new StringBuffer();
		final int maxCols = 18;
		final int maxRows = 6;
		final int scaleFactorX = 5;
		final int scaleFactorY = 5;
		for (int col = 1; col <= maxCols; col++) {
			for (int row = 1; row <= maxRows; row++) {
				final int nodeId = row + maxRows * (col - 1);
				nodeText.append("N ").append(nodeId).append(" ").append(col * scaleFactorX).append(" ").append(row * scaleFactorY).append("\n");
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
				nodeText.append("E ").append(firstElementId).append(" ").append(node1Id).append(" ").append(node2Id).append(" ").append(node3Id).append("\n");
				nodeText.append("E ").append(secondElementId).append(" ").append(node2Id).append(" ").append(node4Id).append(" ").append(node3Id).append("\n");
			}
		}

		nodeText.append("D ").append(1).append(" y ").append(0).append("\n");
		for (int row = 1; row <= maxRows; row++) {
			nodeText.append("D ").append(row).append(" x ").append(0).append("\n");
		}

		for (int col = 1; col <= maxCols; col++) {
			for (int row = 1; row <= maxRows; row++) {
				final int nodeId = row + maxRows * (col - 1);
				nodeText.append("F ").append(nodeId).append(" y ").append(10.0).append("\n");
			}
		}

		return nodeText.toString();
	}

}
