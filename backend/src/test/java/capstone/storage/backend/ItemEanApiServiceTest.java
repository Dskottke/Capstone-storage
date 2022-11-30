package capstone.storage.backend;

import capstone.storage.backend.exceptions.EanApiResponseException;
import capstone.storage.backend.exceptions.ExceptionMessage;
import capstone.storage.backend.exceptions.ItemNotFound;
import capstone.storage.backend.exceptions.ItemResponseEanNullException;
import capstone.storage.backend.item.ItemEanApiService;
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
            String expected = ExceptionMessage.EAN_API_RESPONSE_EXCEPTION_MESSAGE.toString();
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
            String expected = ExceptionMessage.EAN_API_RESPONSE_EXCEPTION_MESSAGE.toString();
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("method : getItemResponseFromApi -> should throw Exception with EanApiResponseExceptionMessage because the request ean is null ")
    void expectItemResponseException3case() {
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
            String expected = ExceptionMessage.EAN_API_RESPONSE_EXCEPTION_MESSAGE.toString();
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("method : getItemResponseFromApi -> should throw Exception with ItemNotFoundExceptionMessage")
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
            String expected = ExceptionMessage.ITEM_NOT_FOUND_EXCEPTION_MESSAGE.toString();
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }
}
