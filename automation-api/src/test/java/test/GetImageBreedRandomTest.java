package test;

import data_class.MessageData;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import utils.DataUtils;

import static client.dogapi.DogApiClient.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetImageBreedRandomTest {

    private Response response;
    private static MessageData messageData;

    @BeforeAll
    public static void setUp() {
        // Carrega os dados esperados de mensagens a partir de arquivos
        messageData = DataUtils.loadData("message", MessageData.class);
    }

    @BeforeEach
    public void initializeTest() {
        // Inicializa a resposta chamando a API para obter uma imagem aleatória de cães
        response = getImagesBreedRandom();
    }

    @Test
    @DisplayName("Validar consulta aleatoria de cães")
    public void testGetImageBreedRandom() {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("status", equalTo(messageData.getSuccess()))
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Validar estrutura da resposta")
    public void testValidateImagesDataFormat() {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", isA(String.class)) // Verifica se o corpo da resposta "message" é uma String
                .body("status", isA(String.class)); // Verifica se o corpo da resposta "status" é uma String
    }

    @Test
    @DisplayName("Validar conteúdo das imagens de cães")
    public void testValidateImageContent() {
        response.then()
                .statusCode(HttpStatus.SC_OK);

        String url = response.path("message");
        assertTrue(url.startsWith("https://"), "A URL deve começar com https://"); // Verifica se a URL começa com https://
        assertTrue(url.endsWith(".jpg") || url.endsWith(".png"), "A URL deve terminar com .jpg ou .png"); // Verifica se a URL termina com .jpg ou .png
    }

    @Test
    @DisplayName("Validar tempo de resposta")
    public void testResponseTimeIsExpectedLimit() {
        long responseTime = getImagesBreedRandom().getTime();
        assertTrue(responseTime < 500, // Verifica se o tempo de resposta é inferior a 500 ms
                "O tempo de resposta deve ser menor que 500 ms, mas foi: " + responseTime + " ms");
    }

    @Test
    @DisplayName("Validar aleatoriedade das imagens")
    public void testRandomDogImageIsDifferent() {
        response.then()
                .statusCode(HttpStatus.SC_OK);

        String firstImage = response.path("message");
        String secondImage = getImagesBreedRandom().path("message"); // Obtém uma nova imagem aleatória
        assertNotEquals(firstImage, secondImage, "As imagens retornadas devem ser diferentes."); // Verifica se as imagens são diferentes
    }

    @Test
    @DisplayName("Validar método HTTP inadequado")
    public void testInvalidHttpMethod() {
        Response response = postImagesBreedRandom(); // Tentativa de usar um método POST
        response.then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
                .body("status", equalTo(messageData.getError()))
                .body("message", containsString(messageData.getNotAllowed()))
                .body("code", equalTo(HttpStatus.SC_METHOD_NOT_ALLOWED));
    }
}