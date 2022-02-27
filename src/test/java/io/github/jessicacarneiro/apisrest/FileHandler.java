package io.github.jessicacarneiro.apisrest;

import java.io.InputStream;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

public class FileHandler {

    @SneakyThrows
    public static String loadFileContents(String filename) {
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);

        return new String(data);
    }
}
