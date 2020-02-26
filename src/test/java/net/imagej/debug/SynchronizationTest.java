package net.imagej.debug;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.display.ImageDisplay;
import net.imagej.legacy.LegacyImageMap;
import net.imagej.legacy.LegacyService;
import net.imagej.legacy.translate.Harmonizer;
import net.imglib2.RandomAccess;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import net.imglib2.img.array.ArrayImgs;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SynchronizationTest {
    private ImageJ ij;
    private ArrayImg img;
    LegacyService legacyService;
    LegacyImageMap map;

    @Before
    public void setUp(){
        ij = new ImageJ();
        ij.launch();

        double[] pixels = new double[100];
        for (int i = 0; i < pixels.length; i++){
            pixels[i] = 5;
        }
        long[] dims = new long[3];
        dims[0] = 10;
        dims[1] = 2;
        dims[2] = 5;

        img = ArrayImgs.doubles(pixels, dims);


        legacyService = ij.getContext().getService(LegacyService.class);
        map = legacyService.getImageMap();
    }

    @After
    public void tearDown(){
        ij.getContext().dispose();
    }

    @Test
    public void testDatasetCreatedFromImagePlusUpdatesWhenImagePlusUpdates() throws Exception{

    }


    @Test
    public void testDatasetCreatedFromImagePlusUpdatesImagePlusWhenChanged() throws Exception{

    }


    @Test
    public void testImagePlusCreatedFromDatasetUpdatesWhenDatasetUpdates() throws Exception{

    }

    @Test
    public void testImagePlusCreatedFromDatasetUpdatesDatasetWhenChanged() throws Exception{
        int original_value = (int) Double.parseDouble(img.cursor().next().toString());

        Dataset ds = ij.dataset().create(img);
        ij.ui().show("Original", ds);
        IJ.run("Add...", "value=5");


        LegacyService legacyService = new LegacyService();

//        Harmonizer harmonizer = new Harmonizer(legacyService.getContext())

//        ImagePlus imp = WindowManager.getCurrentImage();
//        ImageStack stack = imp.getStack();
//        stack.setPixels(imp.getProcessor().getPixels(), imp.getCurrentSlice());

        int new_value = (int) Double.parseDouble(ds.getImgPlus().cursor().next().toString());


        assertEquals(original_value + 5, new_value);
//        assertEquals(ds.getImgPlus().cursor().get(), original_img.cursor().get(), 0);

    }

//    @Test
//    public void testHarmonizerUpdatesDatasetFromImagePlus() throws Exception{
//        int original_value = (int) Double.parseDouble(img.cursor().next().toString());
//
//        Dataset ds = ij.dataset().create(img);
//        ij.ui().show("Original", ds);
//        IJ.run("Add...", "value=5");
//
//        map.updateDataset(ds);
//
//        int new_value = (int) Double.parseDouble(ds.getImgPlus().cursor().next().toString());
//
//        assertEquals(original_value + 5, new_value);
//
//    }

    @Test
    public void testHarmonizerUpdatesDisplayFromImagePlus() throws Exception{
        int original_value = (int) Double.parseDouble(img.cursor().next().toString());

        Dataset ds = ij.dataset().create(img);
        ij.ui().show("Original", ds);
        IJ.run("Add...", "value=5");

        ImageDisplay display = ij.imageDisplay().getActiveImageDisplay();
        map.updateDisplay(display);

        int new_value = (int) Double.parseDouble(ds.getImgPlus().cursor().next().toString());

        assertEquals(original_value + 5, new_value);

    }

    @Test
    public void testHarmonizerUpdatesImagePlusFromDisplay() throws Exception{
        int original_value = (int) Double.parseDouble(img.cursor().next().toString());

        Dataset ds = ij.dataset().create(img);
        ij.ui().show("Original", ds);
        IJ.run("Add...", "value=5");

        ImagePlus imp = WindowManager.getCurrentImage();
        map.updateImagePlus(imp);


        int new_value = (int) imp.getProcessor().getPixelValue(1, 1);

        assertEquals(original_value, new_value);
    }

}
