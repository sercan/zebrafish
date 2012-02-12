
/**
 * @author Bahri
 */
public class Region implements Comparable {
    private int minX, minY, maxX, maxY, aPointX, aPointY;
    private RegionProp regionProp;

    public Region(int minX, int minY, int maxY, int maxX, int x, int y) {
        this.minX = minX;
        this.minY = minY;
        this.maxY = maxY;
        this.maxX = maxX;
        aPointX = x;
        aPointY = y;
        regionProp = new RegionProp();
    }


    public int compareTo(Object region) {
        Region regionnew = (Region) region;
        return regionnew.getRegionProp().getArea() - this.getRegionProp().getArea();

    }

    //cevre ne demek sor
    public int computePerimeter() {
        int perimeter = 0;
        int minX = getMinX();
        int maxX = getMaxX();
        int minY = getMinY();
        int maxY = getMaxY();
        int label = ImageProcessor.labeledPixels[getaPointY()][getaPointX()];
        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                if (ImageProcessor.labeledPixels[i][j] == label) {
                    if (i - 1 < 0 || ImageProcessor.labeledPixels[i - 1][j] != label)
                        perimeter++;
                    else if (i + 1 >= ImageProcessor.labeledPixels.length || ImageProcessor.labeledPixels[i + 1][j] != label)
                        perimeter++;
                    else if (j - 1 < 0 || ImageProcessor.labeledPixels[i][j - 1] != label)
                        perimeter++;
                    else if (j + 1 >= ImageProcessor.labeledPixels[0].length || ImageProcessor.labeledPixels[i][j + 1] != label)
                        perimeter++;

                }
            }
        }

        getRegionProp().setPerimeter(perimeter);
        return perimeter;
    }

    public int computeArea(){
        int area = 0;
        int minX = getMinX();
        int maxX = getMaxX();
        int minY = getMinY();
        int maxY = getMaxY();
        int label = ImageProcessor.labeledPixels[getaPointY()][getaPointX()];
        for(int i=minY; i<=maxY; i++){
            for(int j=minX; j<=maxX; j++){
                if(ImageProcessor.labeledPixels[i][j]==label){
                    area++;
                }
            }
        }
        getRegionProp().setArea(area);
        return area;
    }

    public void setRadius() {
        getRegionProp().setRadius(((double) (maxX - minX) / 2 + (double) (maxY - minY) / 2) / 2);
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public RegionProp getRegionProp() {
        return regionProp;
    }

    public void setRegionProp(RegionProp regionProp) {
        this.regionProp = regionProp;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getaPointX() {
        return aPointX;
    }

    public int getaPointY() {
        return aPointY;
    }
}
