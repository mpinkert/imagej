//
// ReciprocalDataValues.java
//

/*
ImageJ software for multidimensional image processing and analysis.

Copyright (c) 2010, ImageJDev.org.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the names of the ImageJDev.org developers nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package imagej.core.plugins.assign;

import mpicbg.imglib.cursor.Cursor;
import mpicbg.imglib.image.Image;
import mpicbg.imglib.type.numeric.real.FloatType;
import imagej.model.Dataset;
import imagej.plugin.ImageJPlugin;
import imagej.plugin.Menu;
import imagej.plugin.Parameter;
import imagej.plugin.Plugin;
import imglib.ops.operator.UnaryOperator;
import imglib.ops.operator.unary.Copy;
import imglib.ops.operator.unary.Reciprocal;

/**
 * Fills an output Dataset by taking reciprocal values of an input Dataset. IJ1
 * did nothing for integral values and so we mirror here.
 * 
 * @author Barry DeZonia
 */
@Plugin(menu = {
	@Menu(label = "Process", mnemonic = 'p'),
	@Menu(label = "Math", mnemonic = 'm'),
	@Menu(label = "Reciprocal", weight = 16) })
public class ReciprocalDataValues implements ImageJPlugin {

	// -- instance variables that are Parameters --

	@Parameter
	private Dataset input;

	@Parameter(output = true)
	private Dataset output;

	// -- public interface --

	@Override
	public void run() {
		if (input == null) // TODO - temporary code to test these until IJ2
		// plugins can correctly fill a Dataset @Parameter
		{
			Image<FloatType> junkImage1 =
				Dataset.createPlanarImage("", new FloatType(), new int[] { 200, 200 });
			Cursor<FloatType> cursor = junkImage1.createCursor();
			int index = 0;
			for (FloatType pixRef : cursor)
				pixRef.set(index++);
			cursor.close();

			input = new Dataset(junkImage1);
		}
		UnaryOperator op = new Reciprocal();
		if (!input.isFloat()) // This is similar to what IJ1 does
			op = new Copy();
		output = new UnaryTransformation(input, output, op).run();
	}
}
