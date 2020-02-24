package net.imagej.debug;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.img.array.ArrayImg;
import org.junit.Test;
import net.imglib2.img.array.ArrayImgs;

public class SynchronizationTest {

    @Test
    public void testImagePlusHarmonizesToDataset() throws Exception{
        final ImageJ ij = new ImageJ();
        ij.launch();

        double[] pixels = new double[100];
        for (int i = 0; i < pixels.length; i++){
            pixels[i] = 5;
        }
        long[] dims = new long[3];
        dims[0] = 10;
        dims[1] = 10;
        dims[2] = 1;

        ArrayImg img = ArrayImgs.doubles(pixels, dims);

        Dataset ds = ij.dataset().create(img);

        ij.ui().show("Original", ds);

        IJ.run("Add...", "value=5");

        ImagePlus imp = WindowManager.getCurrentImage();
        ImageStack stack = imp.getStack();
        stack.setPixels(imp.getProcessor().getPixels(), imp.getCurrentSlice());

        ij.ui().show("Synced", ds);



    }

}
