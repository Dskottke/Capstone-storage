package capstone.storage.backend.item;

import capstone.storage.backend.exceptions.ApiItemNotFoundException;
import capstone.storage.backend.exceptions.EanApiResponseException;
import capstone.storage.backend.exceptions.ItemResponseEanNullException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ItemEanApiServiceTest {
    private static MockWebServer mockWebServer;
    private ItemEanApiService eanApiService;


    @Value("${ean.api.token}")
    private String token;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());

        eanApiService = new ItemEanApiService(baseUrl, token);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Method : getItemResponseFromApi -> should throw Exception EanApiResponseException because response list ist null.")
    void getItemResponseFromApiShouldThrowEanApiResponseException1() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse());
        String ean = "123";
        //WHEN
        try {
            eanApiService.getItemResponseFromApi(ean);
            fail();
        }
        //THEN
        catch (EanApiResponseException e) {
            String expected = "The api responseBody is null or invalid.";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }

    }

    @Test
    @DisplayName("Method : getItemResponseFromApi -> should throw EanApiResponseException because the Api response size is greater 1.")
    void getItemResponseFromApiExpectEanApiResponseException2() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [{
                        "name": "test",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean": "8710847909610",
                        "storeableValue": "20"},
                        {
                        "name": "test2",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean": "8710847909609",
                        "storeableValue": "20"}]
                        """)
                .addHeader("Content-Type", "application/json"));

        String ean = "123";
        //WHEN
        try {
            eanApiService.getItemResponseFromApi(ean);
            fail();
        }
        //THEN
        catch (EanApiResponseException e) {
            String expected = "The api responseBody is null or invalid.";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("Method : getItemResponseFromApi -> should throw exception ItemResponseEanNullException because the request EAN is null.")
    void getItemResponseExpectExpectItemResponseException() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [{
                        "name": "test",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean" : null,
                        "storebleValue": "20"
                        }]""")
                .addHeader("Content-Type", "application/json"));

        String ean = "123";
        //WHEN
        try {
            eanApiService.getItemResponseFromApi(ean);
            fail();
        }
        //THEN
        catch (ItemResponseEanNullException e) {
            String expected = "couldn't find item by ean: null";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("Method : getItemResponseFromApi -> should throw ApiItemNotFoundException because the response EAN doesn't match with request EAN")
    void getItemResponseFromApiExpectApiItemNotFoundException() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [{
                        "name": "test",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean" : 1234,
                        "storableValue": "20"
                        }]""")
                .addHeader("Content-Type", "application/json"));

        String ean = "123";
        //WHEN
        try {
            eanApiService.getItemResponseFromApi(ean);
            fail();
        }
        //THEN
        catch (ApiItemNotFoundException e) {
            String expected = "EAN doesn't match with response EAN from Api.";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }
}
