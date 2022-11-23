package capstone.storage.backend;

import capstone.storage.backend.exceptions.ItemIsNullException;
import capstone.storage.backend.exceptions.ItemResponseException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

class EanApiServiceTest {
    private static MockWebServer mockWebServer;
    private EanApiService eanApiService;


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

        eanApiService = new EanApiService(baseUrl, token);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("expect Exception with message (item response list is null or invalid) because response list ist null")
    void expectItemResponseException() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse());
        String ean = "123";
        //WHEN
        try {
            eanApiService.getItemResponse(ean);
            fail();
        }
        //THEN
        catch (ItemResponseException e) {
            String expected = "item response list is null or invalid";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }

    }

    @Test
    @DisplayName("expect Exception with message (item response list is null or invalid)")
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
            eanApiService.getItemResponse(ean);
            fail();
        }
        //THEN
        catch (ItemResponseException e) {
            String expected = "item response list is null or invalid";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }


    }

    @Test
    @DisplayName("expect Exception with message (ean is null)")
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
            eanApiService.getItemResponse(ean);
            fail();
        }
        //THEN
        catch (ItemResponseException e) {
            String expected = "ean is null";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("expect Exception with message (item not found)")
    void expectItemResponseException4case() {
        //GIVEN
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        [{
                        "name": "test",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean" : 1234,
                        "storeableValue": "20"
                        }]""")
                .addHeader("Content-Type", "application/json"));

        String ean = "123";
        //WHEN
        try {
            eanApiService.getItemResponse(ean);
            fail();
        }
        //THEN
        catch (ItemIsNullException e) {
            String expected = "item not found";
            String actual = e.getMessage();
            assertEquals(expected, actual);
        }
    }
}
