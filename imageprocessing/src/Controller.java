/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagemanipulation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Bahri
 */
public class Controller {
    
    public static BufferedImage openImage(String path) throws IOException{
        return Image.open(path);
    }
    
    public static void saveImage(BufferedImage img) {
        try {
            String format = "jpeg";
            File saveFile = new File("out/image." + format);
            ImageIO.write(img, format, saveFile);


        } catch (IOException ef) {
            System.out.println("Image could not be read");
            System.exit(1);
        }
    }

    public static BufferedImage findConnectedComponents(BufferedImage img){
            BufferedImage displayedImage = ImageProcessor.connectedComponent(img, null);
            Image.findRegionProps();
            return displayedImage;
    }
    

}
