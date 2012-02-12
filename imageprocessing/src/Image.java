
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Bahri
 */
public class Image {
    public static ArrayList<Region> regions = new ArrayList<Region>();

    public static BufferedImage open(String path) throws IOException{
        BufferedImage img = ImageIO.read(new File(path));
        return img;
    }

    public static void findRegionProps(){
          for(int i=0; i<regions.size(); i++){
              regions.get(i).computeArea();
              regions.get(i).computePerimeter();
              regions.get(i).setRadius();
              regions.get(i).getRegionProp().setRoundness( );
          }
    }







}
