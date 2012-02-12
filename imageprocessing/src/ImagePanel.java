package imagemanipulation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Sercan
 * Date: 17.12.2011
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public  class ImagePanel extends JPanel {

  private BufferedImage displayImage;


  public ImagePanel(BufferedImage img) {
        displayImage = img;
      setBackground(Color.red);

  }

    public void setDisplayedImage(BufferedImage img){
        displayImage = img;
        Graphics2D graphics = displayImage.createGraphics();
        graphics.drawImage(displayImage, 0, 0, null);
    }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphics = (Graphics2D) g;
    graphics.drawImage(displayImage, 0, 0, null);
  }
  public Dimension getPreferredSize() {

      if(displayImage != null){

          return new Dimension(displayImage.getWidth(), displayImage.getHeight());
      }
      else{

          return new Dimension(0,0);
      }
  }
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
}