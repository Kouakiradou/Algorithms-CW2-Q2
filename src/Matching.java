import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

public class Matching
{
    private final double occlusion = 30;

    private int[][] imgArr1;
    private int[][] imgArr2;
    private int n;
    private int m;
    private int counter = 0;

    public Matching(String imgPath1, String imgPath2) throws IOException
    {
        imgArr1 = new ReadImage().readImage(imgPath1);
        imgArr2 = new ReadImage().readImage(imgPath2);
        n = imgArr1[0].length - 1;
        m = imgArr2[0].length - 1;
    }

    public double[][] matchLine(int line)
    {
        double[][] costForLine = new double[n + 1][m + 1];
        for (int i = 1; i <= n; i++)
        {
            costForLine[i][0] = i * occlusion;
        }
        for (int i = 1; i <= m; i++)
        {
            costForLine[0][i] = i * occlusion;
        }

        for (int i = 1; i <= n; i++)
        {
            for (int j = 1; j <= m; j++)
            {
                double a = costForLine[i - 1][j - 1] + costOf(i, j, line);
                double b = costForLine[i][j - 1] + occlusion;
                double c = costForLine[i - 1][j] + occlusion;
                costForLine[i][j] = min(a, b, c);
            }
        }


        return costForLine;
    }

    public int[] getDisparity(double[][] costForLine)
    {
        int[] disparity = new int[costForLine.length];
        int i = costForLine.length - 1;
        int j = costForLine.length - 1;
        while(i > 0 && j > 0)
        {
            if (costForLine[i][j] - costForLine[i][j - 1] < occlusion + 2 && costForLine[i][j] - costForLine[i][j - 1] > occlusion - 2)
            {
                j--;
            }
            else if (costForLine[i][j] - costForLine[i - 1][j] < occlusion + 2 && costForLine[i][j] - costForLine[i-1][j] > occlusion - 2)
            {
                i--;
            }
            else
            {
                i--;
                j--;
                disparity[i] = abs(i - j) * 6;
            }
        }
        return disparity;
    }

    public double min(double a, double b, double c)
    {
        return Math.min(Math.min(a, b), c);
    }

    public double costOf(int i, int j, int line)
    {
        int l = imgArr1[line][i];
        int r = imgArr2[line][j];
        return ((l - r) * (l - r) / 128.0);
    }

    public void matchImage() throws IOException {
        int[][] image = new int[imgArr1.length][];
        BufferedImage outputImage = new BufferedImage(imgArr1[0].length, imgArr1.length, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < imgArr1.length; i++)
        {
            image[i] = getDisparity(matchLine(i));
        }

        for (int i = 0; i < imgArr1.length; i++)
        {
            for (int j = 0; j < imgArr1[0].length; j++)
            {
                if (image[i][j] > 255){
                    image[i][j] = 255;
                }
                if (image[i][j] < 0){
                    image[i][j] = 0;
                }
                outputImage.setRGB(j,i,image[i][j] * 0x00010101);
            }
        }

        System.out.print(counter);
        File output = new File("output.png");
        ImageIO.write(outputImage, "png", output);
    }
}
