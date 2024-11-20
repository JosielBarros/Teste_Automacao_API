package test;

import data_class.BreedsData;
import data_class.MessageData;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import utils.DataUtils;

import java.util.List;

import static client.dogapi.DogApiClient.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetImagesBreedTest {

    private static String breed;
    private static MessageData messageData;
    private static BreedsData breedsData;

    @BeforeAll
    public static void setUp() {
        // Carrega os dados esperados de mensagens e raças a partir de arquivos
        messageData = DataUtils.loadData("message", MessageData.class);
        breedsData = DataUtils.loadData("breeds", BreedsData.class);
        breed = breedsData.getBreed().get(0); // Obtém a primeira raça para os testes
    }

    @Test
    @DisplayName("Validar consulta de imagens de cães")
    public void testGetImageBreed() {
        Response response = getImagesBreeds(breed); // Faz a chamada para obter imagens da raça
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("status", equalTo(messageData.getSuccess()))
                .body("message", notNullValue());

        List<String> images = response.path("message");
        for (String image : images) {
            // Verifica se cada URL de imagem contém a raça correspondente
            assertTrue(image.contains(breed), "A URL " + image + " não contém o item pesquisado");
        }
    }

    @Test
    @DisplayName("Validar estrutura da resposta")
    public void testValidateImagesDataFormat() {
        Response response = getImagesBreeds(breed);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", isA(List.class)) // Verifica se o corpo da resposta "message" é uma lista
                .body("status", equalTo("success"));
    }

    @Test
    @DisplayName("Validar conteúdo das imagens de cães")
    public void testValidateImageContent() {
        Response response = getImagesBreeds(breed);
        response.then()
                .statusCode(HttpStatus.SC_OK);

        List<String> images = response.path("message");
        images.forEach(url -> {
            assertTrue(url.startsWith("https://"), "A URL deve começar com https://"); // Verifica se a URL da imagem começa com "https://"
            assertTrue(url.endsWith(".jpg") || url.endsWith(".png"), "A URL deve terminar com .jpg ou .png"); // Verifica se a URL termina com ".jpg" ou ".png"
        });
    }

    @Test
    @DisplayName("Validar tentativa de consulta com dado inválido")
    public void testValidateGetDataInvalid() {
        Response response = getImagesBreeds(breedsData.getBreed().get(1)); // Faz a chamada com uma raça inválida
        response.then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("status", equalTo(messageData.getError()))
                .body("message", containsString(messageData.getNotFound()))
                .body("code", equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    @DisplayName("Validar tempo de resposta")
    public void testResponseTimeIsExpectedLimit() {
        long responseTime = getImagesBreeds(breed).getTime();
        assertTrue(responseTime < 500, // Verifica se o tempo de resposta é inferior a 500 ms
                "O tempo de resposta deve ser menor que 500 ms, mas foi: " + responseTime + " ms");
    }

    @Test
    @DisplayName("Validar método HTTP inadequado")
    public void testInvalidHttpMethod() {
        Response response = postImagesBreeds(breed); // Tentativa de usar um método POST
        response.then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
                .body("status", equalTo(messageData.getError()))
                .body("message", containsString(messageData.getNotAllowed()))
                .body("code", equalTo(HttpStatus.SC_METHOD_NOT_ALLOWED));
    }
}