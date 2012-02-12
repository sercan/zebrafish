/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagemanipulation;

import java.awt.*;
import java.awt.Image;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Bahri
 */
public class ImageProcessor {

    static int[][][] imagePixels;
    static int[][] labeledPixels;
    private static int numLab = 0;
    private static final int black = 0;
    private static final int white = 1;
    static int[] array;

    private final static int BTYE = 1;

    public static void setImagePixels(BufferedImage bufferedImage) {
        imagePixels = getRGBPixels(bufferedImage);
    }

    public static int[][][] getRGBPixels(BufferedImage img) {
        Raster inputRaster = img.getData();
        int nbands = inputRaster.getNumBands();
        int height = inputRaster.getHeight();
        int width = inputRaster.getWidth();
        int[] pixels = new int[height * width * nbands];
        inputRaster.getPixels(0, 0, inputRaster.getWidth(), inputRaster.getHeight(), pixels);
        int[][][] pixels3D = new int[height][width][nbands];
        int offset;
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                offset = h * width * nbands + w * nbands;
                for (int band = 0; band < nbands; band++) {
                    pixels3D[h][w][band] = pixels[offset + band];
                }
            }
        }
        return pixels3D;
    }

    public static int[][] getBinaryPixels(BufferedImage img) {
        Raster inputRaster = img.getData();
        int height = inputRaster.getHeight();
        int width = inputRaster.getWidth();
        int[] pixels = new int[height * width];
        inputRaster.getPixels(0, 0, inputRaster.getWidth(), inputRaster.getHeight(), pixels);
        int[][] pixels3D = new int[height][width];
        int offset;
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                offset = h * width + w;
                pixels3D[h][w] = pixels[offset];
            }
        }
        return pixels3D;
    }

    public static BufferedImage threshold(BufferedImage img, int requiredThresholdValue) {

        int height = img.getHeight();
        int width = img.getWidth();
        BufferedImage finalThresholdImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        int[] grayScaleImageData = new int[height * width];
        img.getRaster().getPixels(0, 0, width, height, grayScaleImageData);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grayScaleImageData[y * width + x] < requiredThresholdValue) {
                    finalThresholdImage.setRGB(x, y, ImageProcessor.mixColor(0, 0, 0));
                } else {
                    finalThresholdImage.setRGB(x, y, ImageProcessor.mixColor(255, 255, 255));
                }
            }
        }
        return finalThresholdImage;
    }

    private static int mixColor(int red, int green, int blue) {
        return red << 16 | green << 8 | blue;
    }

    public static int otsuThresholding(BufferedImage img) {
        int[] histData = new int[256];
        Raster raster = img.getData();
        DataBuffer buffer = raster.getDataBuffer();
        DataBufferByte byteBuffer = (DataBufferByte) buffer;
        byte[] srcData = byteBuffer.getData(0);
        // Calculate histogram
        int ptr = 0;
        while (ptr < srcData.length) {
            int h = 0xFF & srcData[ptr];
            histData[h]++;
            ptr++;
        }

        // Total number of pixels
        int total = srcData.length;

        float sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += t * histData[t];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += histData[t];               // Weight Background
            if (wB == 0) {
                continue;
            }

            wF = total - wB;                 // Weight Foreground
            if (wF == 0) {
                break;
            }

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB;            // Mean Background
            float mF = (sum - sumB) / wF;    // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        return threshold;
    }


    public static BufferedImage connectedComponent(BufferedImage img, int[][] labels) {
        int[][] pixels;
        if (labels == null) {
            if (img.getType() != BufferedImage.TYPE_BYTE_BINARY)
                throw new IllegalArgumentException("Wrong image format");

            pixels = getBinaryPixels(img);
            labelAllImage(pixels);
            labeledPixels = pixels;
        } else {
            pixels = labels;


        }


        ArrayList<Color> colors = new ArrayList<Color>();


        for (int i = 0; i < numLab; i++) {
            colors.add(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        }
        colors.add(1, new Color(255, 255, 255));
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage finalThresholdImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int y = 0; y < height; y++) {
            try {
                for (int x = 0; x < width; x++) {
                    finalThresholdImage.setRGB(x, y, colors.get(pixels[y][x]).getRGB());
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return finalThresholdImage;

    }

    /**
     * Uses Matlab rgb2gray coefficients
     * http://http://www.mathworks.com/help/toolbox/images/ref/rgb2gray.html
     *
     * @param rgbImage
     * @return
     */
    public static BufferedImage rgbToGrayscale(BufferedImage rgbImage) {
        assert (rgbImage.getType() != BufferedImage.TYPE_INT_RGB);

        int[][][] rgbData = getRGBPixels(rgbImage);
        int height = rgbImage.getHeight();
        int width = rgbImage.getWidth();
        int[] grayScaleImage = new int[height * width];

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);


        for (int h = 0; h < height; h++)
            for (int w = 0; w < width; w++) {
                grayScaleImage[h * width + w] = (int) (0.2989 * rgbData[h][w][0] + 0.5870 * rgbData[h][w][1] + 0.1140 * rgbData[h][w][2]);
            }

        WritableRaster writableRaster = grayImage.getRaster().createCompatibleWritableRaster();
        writableRaster.setPixels(0, 0, width, height, grayScaleImage);
        grayImage.setData(writableRaster);
        return grayImage;


    }

    public static BufferedImage complementImage(BufferedImage img) {

        System.out.println(img.getType());
        int h = img.getHeight();
        int w = img.getWidth();
        int nbands = img.getRaster().getNumBands();
        byte[] pixels = new byte[h * w * nbands];
        img.getRaster().getDataElements(0, 0, w, h, pixels);
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (byte) (255 - pixels[i]);
        }
        WritableRaster writableRaster = img.getRaster().createCompatibleWritableRaster();
        writableRaster.setDataElements(0, 0, w, h, pixels);
        img.setData(writableRaster);
        return img;
    }
    
    public static BufferedImage sharpenImage(BufferedImage img) {
        float[] sharpenMatrix = {0.0f, -1.0f, 0.0f, 
                                -1.0f,  5.0f, -1.0f, 
                                 0.0f, -1.0f, 0.0f};
        BufferedImageOp sharpenFilter = new ConvolveOp(new Kernel(3, 3, sharpenMatrix),
                ConvolveOp.EDGE_NO_OP, null);
        return sharpenFilter.filter(img, null);
    }
    
    public static BufferedImage prewittFilter(BufferedImage img) {
        float[] prewittMatrix = {1.0f, 1.0f, 1.0f, 
                                0.0f,  0.0f, 0.0f, 
                                 -1.0f, -1.0f, -1.0f};
        BufferedImageOp prewittFilter = new ConvolveOp(new Kernel(3, 3, prewittMatrix),
                ConvolveOp.EDGE_NO_OP, null);
        return prewittFilter.filter(img, null);
    }
    
    public static BufferedImage blurImage(BufferedImage img) {
        float[] blurMatrix = {1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
                              1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f, 
                              1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f};
        BufferedImageOp blurFilter = new ConvolveOp(new Kernel(3, 3, blurMatrix),
                ConvolveOp.EDGE_NO_OP, null);

        return blurFilter.filter(img, null);
    }
    
    public static BufferedImage spatialFilter(BufferedImage img) {
        float[] linearMatrix = {-1.0f, 2.0f, -1.0f, 
                                 -1.0f, 2.0f, -1.0f, 
                                 -1.0f, 2.0f, -1.0f};
        BufferedImageOp linearFilter = new ConvolveOp(new Kernel(3, 3, linearMatrix),
                ConvolveOp.EDGE_ZERO_FILL, null);
        return linearFilter.filter(img, null);
    }
    
    public static BufferedImage sobelFilter(BufferedImage img) {
        float[] sobelMatrix = {1.0f, 2.0f, 1.0f, 
                                0.0f, 0.0f, 0.0f, 
                                 -1.0f, -2.0f, -1.0f};
        
        BufferedImageOp sobelFilter = new ConvolveOp(new Kernel(3, 3, sobelMatrix),
                ConvolveOp.EDGE_ZERO_FILL, null);

        
        return sobelFilter.filter(img,null);
    }
    
    public static BufferedImage laplaceFilter(BufferedImage img) {
        float[] laplaceMatrix = {-1f, -3f, -4f, -3f, -1f,
                                 -3f,  0f,  6f,  0f, -3f,
                                 -4f,  6f, 20f,  6f, -4f,
                                 -3f,  0f,  6f,  0f, -3f,
                                 -1f, -3f, -4f, -3f, -1f};
        BufferedImageOp laplaceFilter = new ConvolveOp(new Kernel(5, 5, laplaceMatrix),
                ConvolveOp.EDGE_NO_OP, null);
        return laplaceFilter.filter(img, null);
    }
    
    public static BufferedImage avearageFilter(BufferedImage img, int n) {
        float[] avearageMatrix = new float[n*n];
        int m = n*n;
        for(int i=0; i<m; i++){
            avearageMatrix[i] = 1f/(float)m;
        }
        
        
        BufferedImageOp laplaceFilter = new ConvolveOp(new Kernel(n, n, avearageMatrix),
                ConvolveOp.EDGE_NO_OP, null);
        return laplaceFilter.filter(img, null);
    }
    


    public static BufferedImage erosionImage(BufferedImage img) {

        int squareStructure = 5;
        int look = squareStructure / 2;
        int[][] pixels = getBinaryPixels(img);
        int width = pixels[0].length;
        int height = pixels.length;

        byte[] newPixels = new byte[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int minX = j - look;
                int maxX = j + look;
                int minY = i - look;
                int maxY = i + look;
                if (minX < 0)
                    minX = 0;
                if (minY < 0)
                    minY = 0;
                if (maxX >= width)
                    maxX = width - 1;
                if (maxY >= height)
                    maxY = height - 1;
                newPixels[i * width + j] = (byte) white;
                for (; minY <= maxY; minY++) {
                    for (; minX <= maxX; minX++) {
                        if (pixels[minY][minX] == black) {
                            newPixels[i * width + j] = (byte) black;
                            minX = maxX + 1;
                            minY = maxY + 1;
                        }
                    }
                }

            }
        }
        WritableRaster writableRaster = img.getRaster().createCompatibleWritableRaster();
        writableRaster.setDataElements(0, 0, width, height, newPixels);
        img.setData(writableRaster);
        return img;

    }

    public static BufferedImage dilationImage(BufferedImage img) {
        int squareStructure = 5;
        int look = squareStructure / 2;
        int[][] pixels = getBinaryPixels(img);
        int width = pixels[0].length;
        int height = pixels.length;

        byte[] newPixels = new byte[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int minX = j - look;
                int maxX = j + look;
                int minY = i - look;
                int maxY = i + look;
                if (minX < 0)
                    minX = 0;
                if (minY < 0)
                    minY = 0;
                if (maxX >= width)
                    maxX = width - 1;
                if (maxY >= height)
                    maxY = height - 1;
                newPixels[i * width + j] = (byte) black;
                for (; minY <= maxY; minY++) {
                    for (; minX <= maxX; minX++) {
                        if (pixels[minY][minX] == white) {
                            newPixels[i * width + j] = (byte) white;
                            minX = maxX + 1;
                            minY = maxY + 1;
                        }
                    }
                }

            }
        }
        WritableRaster writableRaster = img.getRaster().createCompatibleWritableRaster();
        writableRaster.setDataElements(0, 0, width, height, newPixels);
        img.setData(writableRaster);
        return img;


    }

    static void labelAllImage(int[][] pixels) {
       Image.regions.clear();
        int label = 2;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                array = new int[6];
                if (pixels[i][j] == black) {
                    array[0] = j;
                    array[1] = j;
                    array[2] = i;
                    array[3] = i;
                    array[4] = -1;
                    array[5] = -1;
                }
                if (labelConnectedComponent(pixels, j, i, label, array)) {
                    label++;
                    Region r = new Region(array[0], array[2], array[3], array[1], array[4], array[5]);
                    Image.regions.add(r);
                }
            }
        }
        numLab = label;
    }


    //Region bounding box yanlış
    static boolean labelConnectedComponent(int[][] pixels, int x, int y, int label, int[] arr) {
        if (x < 0 || x >= pixels[0].length || y < 0 || y >= pixels.length) {
            // This position is not in the grid, so there is
            // no blob at this position.  Return a blob size of zero.
            return false;
        }
        if (pixels[y][x] >= white) {
            // This square is not part of a blob, or else it has
            // already been counted, so return zero.
            return false;
        }
        pixels[y][x] = label;   // Mark the square as visited so that
        //    we won't count it again during the
        //    following recursive calls.


        //TODO: Doğru calısıp calismadığını kontrol et
        if (x < arr[0])
            arr[0] = x;
        if (x > arr[1])
            arr[1] = x;
        if (y > arr[3])
            arr[3] = y;
        if (y < arr[2])
            arr[2] = y;
        if (arr[4] == -1) {
            arr[4] = x;
            arr[5] = y;
        }
        labelConnectedComponent(pixels, x + 1, y, label, arr);

        labelConnectedComponent(pixels, x - 1, y, label, arr);

        labelConnectedComponent(pixels, x, y + 1, label, arr);
        labelConnectedComponent(pixels, x, y - 1, label, arr);

        return true;
    }  // end labelConnectedComponent()

    public static BufferedImage findEye() {
        Collections.sort(Image.regions);

        double max = Math.max(Image.regions.get(0).getRegionProp().getRoundness(), Image.regions.get(1).getRegionProp().getRoundness());

        max = Math.max(max, Image.regions.get(2).getRegionProp().getRoundness());

        Region region = null;
        for (int i = 0; i < 3; i++) {
            if (Image.regions.get(i).getRegionProp().getRoundness() == max) {
                region = Image.regions.get(i);
            }
        }

        int label = labeledPixels[region.getaPointY()][region.getaPointX()];
        System.out.println(region.getMinX());
        extractRegion(label);
        return connectedComponent(null, labeledPixels);

    }

    public static void extractRegion(int label) {

        for (int i = 0; i < labeledPixels.length; i++)
            for (int j = 0; j < labeledPixels[0].length; j++) {
                if (labeledPixels[i][j] != label) {
                    labeledPixels[i][j] = white;
                }
            }
    }

}




