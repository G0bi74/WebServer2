package pl.G0bi74.WebServer2;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static pl.G0bi74.WebServer2.ImageController.brighter;

@RestController
@RequestMapping("/api")
public class ImageFromController {
    @GetMapping
    public String showSite() {
        String site = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Upload Image</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h2>Wyślij obrazek</h2>\n" +
                "    <form method=\"post\" action=\"/api/upload\" enctype=\"multipart/form-data\">\n" +
                "        <input type=\"file\" name=\"image\" accept=\"image/*\">\n" +
                "        <br><br>\n" +
                "        <label for=\"brightness\">Zmiana jasności:</label>\n" +
                "        <input type=\"range\" id=\"brightness\" name=\"brightness\" min=\"-255\" max=\"255\" value=\"0\">\n" +
                "        <br><br>\n" +
                "        <button type=\"submit\">Upload</button>\n" +
                "    </form>\n" +
                "</body>\n" +
                "</html>";
        return site;
    }
    public BufferedImage imageBrighter( int value,  String base64Image) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
        try {
            BufferedImage image = ImageIO.read(bais);
            return brighter(image,value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getBrighterEncodedImage(int value, String secretMessage) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(secretMessage);
            ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
            BufferedImage image = ImageIO.read(bais);

            BufferedImage brighterImage = imageBrighter(value, secretMessage);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(brighterImage, "png", baos);
            byte[] outputBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(outputBytes);
        }
        catch (IOException e) {
            throw new RuntimeException("failed to process image", e);
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public String uploadImage(@RequestParam("image") MultipartFile file, @RequestParam("brightness") int brightness) {
        if (!file.isEmpty() && file.getContentType().startsWith("image")) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                base64Image = getBrighterEncodedImage(brightness, base64Image);
                return "<html><body><img src='data:image/png;base64, " + base64Image + "' /></body></html>";
            } catch (IOException e) {
                e.printStackTrace();
                return "Błąd podczas przetwarzania obrazka.";
            }
        } else {
            return "Błąd - proszę wybrać plik obrazka.";
        }
    }
}
