package capstone.storage.backend;

import capstone.storage.backend.exceptions.EanApiResponseException;
import capstone.storage.backend.exceptions.ItemNotFound;
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
    private final String eanApiResponseExceptionMessage = "couldn't find item by ean";

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
    @DisplayName("method : getItemResponseFromApi -> should throw Exception eanApiResponseExceptionMessage because response list ist null")
    void expectItemResponseException() {
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
            String expected = eanApiResponseExceptionMessage;
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }

    }

    @Test
    @DisplayName("method : getItemResponseFromApi -> should throw Exception with eanApiResponseExceptionMessage because the Api response size is greater 1")
    void expectItemResponseException2case() {
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
            String expected = eanApiResponseExceptionMessage;
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("method : getItemResponseFromApi -> should throw Exception with message (ean is null) because the request ean is null ")
    void expectItemResponseException3case() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [{
                        "name": "test",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean" : null,
                        "storeableValue": "20"
                        }]""")
                .addHeader("Content-Type", "application/json"));

        String ean = "123";
        //WHEN
        try {
            eanApiService.getItemResponseFromApi(ean);
            fail();
        }
        //THEN
        catch (EanApiResponseException e) {
            String expected = "couldn't find item by ean";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("method : getItemResponseFromApi -> should throw Exception with message (ean to find doesn't match with response ean) ")
    void expectItemResponseException4case() {
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
        catch (ItemNotFound e) {
            String expected = "ean to find doesn't match with response ean";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }
}