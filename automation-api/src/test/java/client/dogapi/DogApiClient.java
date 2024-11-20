package client.dogapi;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class DogApiClient {

    private static final String ENDPOINT_ALL_BREEDS = "/breeds/list/all";
    private static final String ENDPOINT_IMAGE_RANDOM = "/breeds/image/random";

    static {
        baseURI = "https://dog.ceo";
        basePath = "/api";
    }

    public static Response getAllBreeds() {
        return given()
                .when()
                .get(ENDPOINT_ALL_BREEDS);
    }

    public static Response getImagesBreeds(String breed) {
        return given()
                .when()
                .get("/breed/" + breed + "/images");
    }

    public static Response getImagesBreedRandom() {
        return given()
                .when()
                .get(ENDPOINT_IMAGE_RANDOM);
    }

    public static Response postAllBreeds() {
        return given()
                .when()
                .post(ENDPOINT_ALL_BREEDS);
    }

    public static Response postImagesBreeds(String breed) {
        return given()
                .when()
                .post("/breed/" + breed + "/images");
    }

    public static Response postImagesBreedRandom() {
        return given()
                .when()
                .post(ENDPOINT_IMAGE_RANDOM);
    }
}