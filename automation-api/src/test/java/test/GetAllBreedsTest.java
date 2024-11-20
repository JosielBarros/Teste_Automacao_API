package test;

import data_class.BreedsData;
import data_class.MessageData;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.DataUtils;

import java.util.List;
import java.util.Map;

import static client.dogapi.DogApiClient.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetAllBreedsTest {

    private static MessageData messageData;
    private static BreedsData breedsData;

    @BeforeAll
    public static void setUp() {
        // Carrega os dados esperados de mensagens e raças a partir de arquivos
        messageData = DataUtils.loadData("message", MessageData.class);
        breedsData = DataUtils.loadData("breeds", BreedsData.class);
    }

    @Test
    @DisplayName("Validar consulta das raças de cães")
    public void testGetAllBreeds() {
        Response response = getAllBreeds();
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("status", equalTo(messageData.getSuccess()))
                .body("message", notNullValue())
                .body("message", hasKey(breedsData.getBreed().get(0))) // Verifica se a resposta contém a raça esperada
                .body("message.bulldog", hasItems(breedsData.getSubBreed())); // Verifica se a sub-raça "bulldog" contém as sub-raças esperadas
    }

    @Test
    @DisplayName("Validar estrutura da resposta")
    public void testValidateBreedsDataFormat() {
        Response response = getAllBreeds();
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", isA(Map.class)); // Verifica se o corpo da resposta é um Map

        Map<String, List<String>> breeds = response.path("message");
        breeds.forEach((breed, subBreeds) -> {
            // Verifica se cada raça tem uma lista de sub-raças
            assertInstanceOf(List.class, subBreeds, "A raça " + breed + " deve ser do tipo lista");
        });
    }

    @Test
    @DisplayName("Validar tempo de resposta")
    public void testResponseTimeIsExpectedLimit() {
        long responseTime = getAllBreeds().getTime();
        assertTrue(responseTime < 500,
                "O tempo de resposta deve ser menor que 500 ms, mas foi: " + responseTime + " ms");
    }

    @Test
    @DisplayName("Validar que não existe raça com valor nulo")
    public void testNoBreedsHaveNullValues() {
        Response response = getAllBreeds();
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", not(hasKey("null")));
    }

    @Test
    @DisplayName("Validar método HTTP inadequado")
    public void testInvalidHttpMethod() {
        Response response = postAllBreeds();
        response.then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
                .body("status", equalTo(messageData.getError())) // Verifica se o status da resposta está correto
                .body("message", containsString(messageData.getNotAllowed())) // Verifica se a mensagem contém a descrição de método não permitido
                .body("code", equalTo(HttpStatus.SC_METHOD_NOT_ALLOWED));
    }
}