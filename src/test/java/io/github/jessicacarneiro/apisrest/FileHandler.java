package io.github.jessicacarneiro.apisrest;

import java.io.InputStream;
import java.util.Map;
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

    @SneakyThrows
    public static String loadFileContents(String filename, Map<String, String> replacements) {
        String fileContents = loadFileContents(filename);

        for (Map.Entry<String, String> entry: replacements.entrySet()) {
            fileContents = fileContents.replace("{{" + entry.getKey()  + "}}", entry.getValue());
        }

        return fileContents;
    }
}
