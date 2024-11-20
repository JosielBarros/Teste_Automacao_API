package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class DataUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T loadData(String arquivo, Class<T> clazz) {
        try (InputStream inputStream = DataUtils.class.getResourceAsStream("/data/" + arquivo + ".json")) {
            if (inputStream == null) {
                throw new IOException("Arquivo não encontrado: " + arquivo + ".json");
            }

            return objectMapper.readValue(inputStream, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o arquivo: " + e.getMessage(), e);
        }
    }
}