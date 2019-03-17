import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class RandomDots
{
    public void generateRandomImage() throws IOException {
        BufferedImage outputImage1 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        BufferedImage outputImage2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        int[][] base = new int[512][512];
        int[][] top = new int[256][256];
        for (int i = 0; i < 512; i++)
        {
            for (int j = 0; j < 512; j++)
            {
                base[i][j] = new Random().nextBoolean() ? 0 : 255;
            }
        }

        for (int i = 0; i < 256; i++)
        {
            for (int j = 0; j < 256; j++)
            {
                top[i][j] = new Random().nextBoolean() ? 0 : 255;
            }
        }

        for (int i = 0; i < 512; i++)
        {
            for (int j = 0; j < 512; j++)
            {
                outputImage1.setRGB(j, i, base[i][j] * 0x00010101);
            }
        }

        for (int i = 0; i < 512; i++)
        {
            for (int j = 0; j < 512; j++)
            {
                outputImage2.setRGB(j, i, base[i][j] * 0x00010101);
            }
        }

        for (int i = 124; i < 124 + 256; i++)
        {
            for (int j = 128; j < 128 + 256; j++)
            {
                outputImage1.setRGB(i, j, top[i - 124][j - 128] * 0x00010101);
            }
        }

        for (int i = 132; i < 132 + 256; i++)
        {
            for (int j = 128; j < 128 + 256; j++)
            {
                outputImage2.setRGB(i, j, top[i - 132][j - 128] * 0x00010101);
            }
        }

        File output1 = new File("output1.png");
        ImageIO.write(outputImage1, "png", output1);

        File output2 = new File("output2.png");
        ImageIO.write(outputImage2, "png", output2);
    }
}
