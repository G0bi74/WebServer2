package pl.G0bi74.WebServer2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
public class ImageController {

    public static BufferedImage brighter(BufferedImage image, int value){
        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int argb = image.getRGB(x,y);
                int a = (argb >> 24) & 0xff;
                int r = ((argb >> 16) & 0xff)+value;
                if (r > 255) r = 255;
                if (r < 0) r = 0;
                int g = ((argb >> 8) & 0xff)+value;
                if (g > 255) g = 255;
                if (g < 0) g = 0;
                int b = ((argb) & 0xff)+value;
                if (b > 255) b = 255;
                if (b < 0) b = 0;
                int newRGB = (a << 24) + (r << 16) + (g << 8) + b;
                image.setRGB(x,y,newRGB);
            }
        }
        return image;
    }
    public static String encodeImageToBase64(BufferedImage image, String format) throws IOException {
        // Tworzymy strumień wyjściowy
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Zapisujemy obraz do strumienia wyjściowego w podanym formacie
        ImageIO.write(image, format, outputStream);
        // Przekształcamy zapisane bajty do ciągu Base64
        String base64String = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        // Zwracamy ciąg Base64
        return base64String;
    }

    @GetMapping("/encodedImageBrighter")
    public String encodedImageBrighter(@RequestParam int value, @RequestBody String base64Image) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
        try {
            BufferedImage image = ImageIO.read(bais);
            image = brighter(image,value);
            return encodeImageToBase64(image,"png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/imageBrighter")
    public BufferedImage imageBrighter(@RequestParam int value, @RequestBody String base64Image) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
        try {
            BufferedImage image = ImageIO.read(bais);
            return image = brighter(image,value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
