import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UserInterface extends JFrame {

    int width, height;
    JFileChooser fc;
    BufferedImage img, displayedImage, grayScaledImage;
    JMenuBar menuBar;
    JSlider slider;
    ImagePanel imagePanel;
    JScrollPane spane;
    JToolBar toolBar;
    private static FileDialog fileDialog;
    JPanel sliderPanel;
    boolean morf;

    public UserInterface() {
        morf = false;
        grayScaledImage = null;
        sliderPanel = null;
        displayedImage = img = null;
        imagePanel = new ImagePanel(displayedImage);
        spane = new JScrollPane(imagePanel);

        slider = null;
        Toolkit tk = Toolkit.getDefaultToolkit();
        width = ((int) tk.getScreenSize().getWidth());
        height = ((int) tk.getScreenSize().getHeight() - 40);

        setSize(width, height);

        setVisible(true);
        menuBar = createMenuBar();
        toolBar = createToolBar();
        setJMenuBar(menuBar);

        JPanel k = new JPanel();
        k.setBackground(Color.yellow);
        k.setSize(200, 200);
        setLayout(null);



        int heightOftoolBar = 30;
        toolBar.setSize(width, heightOftoolBar);
        System.out.println(menuBar.getHeight());
        toolBar.setLocation(0, 0);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        spane.setLocation(0, heightOftoolBar);
        spane.setSize(width - 15, height - heightOftoolBar - 65);
        add(spane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }

    public JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        ImageIcon iconNew = new ImageIcon("file_open.jpg");
        JButton btn1 = new JButton(iconNew);
        btn1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = gd.getDefaultConfiguration();
                int sw = gc.getBounds().width - 64;
                int sh = gc.getBounds().height - 64;

                fileDialog = new FileDialog(UserInterface.this, "File Dialog", FileDialog.LOAD);

                fileDialog.setBounds(sw - 400 + 32, sh - 300 + 32, 300, 200);
                fileDialog.setVisible(true);
                String path = fileDialog.getDirectory() + fileDialog.getFile();
                try {
                    displayedImage = img = Controller.openImage(path);
                    imagePanel.setDisplayedImage(displayedImage);
                    //imagePanel = new ImagePanel(displayedImage);
                    spane.setViewportView(imagePanel);
                    spane.setPreferredSize(new Dimension(displayedImage.getWidth(), displayedImage.getHeight()));


                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                //setSize(img.getWidth(), img.getHeight() + 120);
                ImageProcessor.setImagePixels(img);
                repaint();
            }
        });
        toolBar.add(btn1);
        return toolBar;
    }

    public JMenuBar createMenuBar() {


        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        //Build the first menu.
        JMenu menu = new JMenu("Operations");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Open Image", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                grayScaledImage = null;
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = gd.getDefaultConfiguration();
                int sw = gc.getBounds().width - 64;
                int sh = gc.getBounds().height - 64;

                fileDialog = new FileDialog(UserInterface.this, "File Dialog", FileDialog.LOAD);

                fileDialog.setBounds(sw - 400 + 32, sh - 300 + 32, 300, 200);
                fileDialog.setVisible(true);
                String path = fileDialog.getDirectory() + fileDialog.getFile();
                try {
                    displayedImage = img = Controller.openImage(path);
                    imagePanel.setDisplayedImage(displayedImage);
                    //imagePanel = new ImagePanel(displayedImage);
                    spane.setViewportView(imagePanel);
                    spane.setPreferredSize(new Dimension(displayedImage.getWidth(), displayedImage.getHeight()));


                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                //setSize(img.getWidth(), img.getHeight() + 120);
                ImageProcessor.setImagePixels(img);
                repaint();
            }
        });
        menu.add(menuItem);


        //a group of JMenuItems
        JMenuItem saveMenuItem = new JMenuItem("Save Image", KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        saveMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String format = "jpeg";

                fileDialog = new FileDialog(UserInterface.this, "File Dialog", FileDialog.SAVE);
                fileDialog.setFile("savedImage." + format);
                fileDialog.setVisible(true);

                String path = fileDialog.getDirectory() + fileDialog.getFile();

                File saveFile = new File(path);

                try {
                    ImageIO.write(displayedImage, format, saveFile);
                } catch (IOException ex) {
                }
            }
        });
        menu.add(saveMenuItem);

        //a group of JMenuItems
        JMenuItem tMenuItem = new JMenuItem("Threshold Image", KeyEvent.VK_T);

        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
        tMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        tMenuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");

        tMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if(displayedImage.getType() != BufferedImage.TYPE_BYTE_GRAY){
                    displayedImage = ImageProcessor.rgbToGrayscale(displayedImage);
                    grayScaledImage = displayedImage;
                }    
                if (sliderPanel == null) {
                    spane.setSize(width - 15, height - 50 - 65);
                    spane.setViewportView(imagePanel);
                    spane.setPreferredSize(new Dimension(displayedImage.getWidth(), displayedImage.getHeight()));
                    sliderPanel = new JPanel();
                    sliderPanel.setBackground(Color.yellow);

                    slider = new JSlider(0, 255);
                    //slider.setLocation(300, 10);
                    slider.setSize(300, 15);
                    slider.addChangeListener(new ChangeListener() {

                        public void stateChanged(ChangeEvent ev) {
                            int thresh = slider.getValue();
                            displayedImage = ImageProcessor.threshold(grayScaledImage, thresh);
                            imagePanel.setDisplayedImage(displayedImage);
                            repaint();
                        }
                    });

                    //sliderPanel.setLayout(new BorderLayout());
                    sliderPanel.setSize(width, 100);
                    sliderPanel.setLocation(0, height - 15 - 65);


                    sliderPanel.add(slider);

                    getContentPane().add(sliderPanel);
                }


                int auto = ImageProcessor.otsuThresholding(grayScaledImage);
                slider.setValue(auto);
                displayedImage = ImageProcessor.threshold(grayScaledImage, auto);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
                sliderPanel.updateUI();
            }
        });
        menu.add(tMenuItem);


        //a group of JMenuItems
        JMenuItem cMenuItem = new JMenuItem("Find Connected Components", KeyEvent.VK_F);

        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
        cMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        cMenuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        cMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(grayScaledImage == null){
                    displayedImage = ImageProcessor.rgbToGrayscale(displayedImage);
                    grayScaledImage = displayedImage;
                }
                if(morf == false){
                    int auto = ImageProcessor.otsuThresholding(grayScaledImage);
                    displayedImage = ImageProcessor.threshold(grayScaledImage, auto/2);
                }
                morf = false;
                displayedImage = Controller.findConnectedComponents(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
                System.out.println();
            }
        });
        menu.add(cMenuItem);


        JMenuItem rgb2grayMenuItem = new JMenuItem("Convert Grayscale", KeyEvent.VK_G);
        rgb2grayMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        rgb2grayMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayedImage = ImageProcessor.rgbToGrayscale(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                grayScaledImage = displayedImage;
                repaint();
            }
        });

        menu.add(rgb2grayMenuItem);

        JMenuItem findEye = new JMenuItem("Find Eye", KeyEvent.VK_G);
        findEye.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        findEye.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               if(grayScaledImage == null){
                    displayedImage = ImageProcessor.rgbToGrayscale(displayedImage);
                    grayScaledImage = displayedImage;
                }
               
               if(morf == false){
                    int auto = ImageProcessor.otsuThresholding(grayScaledImage);
                    displayedImage = ImageProcessor.threshold(grayScaledImage, auto/2);
                }
                morf = false;
                
                displayedImage = Controller.findConnectedComponents(displayedImage);
                displayedImage = ImageProcessor.findEye();
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        menu.add(findEye);

        JMenuItem complementImageMenuItem = new JMenuItem("Complement Image", KeyEvent.VK_C);
        complementImageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        complementImageMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayedImage = ImageProcessor.complementImage(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        menu.add(complementImageMenuItem);

        JMenu morphologicalMenuItem = new JMenu("Morphological operators");


        JMenuItem erosionImageMenuItem = new JMenuItem("Erode Image", KeyEvent.VK_E);
        erosionImageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK));
        erosionImageMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                morf = true;
                displayedImage = ImageProcessor.erosionImage(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        morphologicalMenuItem.add(erosionImageMenuItem);

        JMenuItem dilationImageMenuItem = new JMenuItem("Dilade Image", KeyEvent.VK_D);
        dilationImageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.SHIFT_MASK));
        dilationImageMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                morf = true;
                displayedImage = ImageProcessor.dilationImage(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        morphologicalMenuItem.add(dilationImageMenuItem);

        JMenuItem openingImageMenuItem = new JMenuItem("Opening Image", KeyEvent.VK_O);
        openingImageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.SHIFT_MASK));
        openingImageMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                morf = true;
                displayedImage = ImageProcessor.dilationImage(displayedImage);
                displayedImage = ImageProcessor.erosionImage(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        morphologicalMenuItem.add(openingImageMenuItem);

        JMenuItem closingImageMenuItem = new JMenuItem("Closing Image", KeyEvent.VK_C);
        closingImageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.SHIFT_MASK));
        closingImageMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                morf = true;
                displayedImage = ImageProcessor.erosionImage(displayedImage);
                displayedImage = ImageProcessor.dilationImage(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        morphologicalMenuItem.add(closingImageMenuItem);

        menu.add(morphologicalMenuItem);

        JMenu filtermenu = new JMenu("Filters");
        filtermenu.setMnemonic(KeyEvent.VK_F);
        filtermenu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(filtermenu);

        JMenuItem sharpenMEnuItem = new JMenuItem("Sharpen", KeyEvent.VK_G);
        sharpenMEnuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        sharpenMEnuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayedImage = ImageProcessor.sharpenImage(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        filtermenu.add(sharpenMEnuItem);

        JMenuItem blurMenuItem = new JMenuItem("BlurFilter", KeyEvent.VK_G);
        blurMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        blurMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayedImage = ImageProcessor.blurImage(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        filtermenu.add(blurMenuItem);

        JMenuItem spatialMenuItem = new JMenuItem("SpatialFilter", KeyEvent.VK_G);
        spatialMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        spatialMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayedImage = ImageProcessor.spatialFilter(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        filtermenu.add(spatialMenuItem);


        JMenuItem sobelMenuItem = new JMenuItem("SobelFilter", KeyEvent.VK_G);
        sobelMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        sobelMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayedImage = ImageProcessor.sobelFilter(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        filtermenu.add(sobelMenuItem);


        JMenuItem prewittMenuItem = new JMenuItem("PrewittFilter", KeyEvent.VK_G);
        prewittMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        prewittMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayedImage = ImageProcessor.prewittFilter(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        filtermenu.add(prewittMenuItem);

        JMenuItem laplaceMenuItem = new JMenuItem("LaplaceFilter", KeyEvent.VK_G);
        laplaceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        laplaceMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displayedImage = ImageProcessor.laplaceFilter(displayedImage);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        filtermenu.add(laplaceMenuItem);


        JMenuItem avearageMenuItem = new JMenuItem("Avearage Filter", KeyEvent.VK_G);
        avearageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        avearageMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String ans = JOptionPane.showInputDialog(null, "Enter Avearage:");
                int avg = Integer.parseInt(ans);

                displayedImage = ImageProcessor.avearageFilter(displayedImage, avg);
                imagePanel.setDisplayedImage(displayedImage);
                repaint();
            }
        });

        filtermenu.add(avearageMenuItem);

        return menuBar;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = UserInterface.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        new UserInterface();
    }
}
