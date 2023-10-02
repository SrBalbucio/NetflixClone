package app.netflix.utils;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.URL;
import java.util.Base64;

public class EncodeUtils {

    public static String imageToBase64(String path){
        try {
            URL url = new URL("https://image.tmdb.org/t/p/original"+path);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            File imgFile = new File(path.replace("/", ""));
            FileOutputStream fos = new FileOutputStream(imgFile);
            fos.write(response);
            fos.close();
            byte[] fileContent = FileUtils.readFileToByteArray(imgFile);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            imgFile.delete();
            return encodedString;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
