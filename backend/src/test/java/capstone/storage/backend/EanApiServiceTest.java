package capstone.storage.backend;

import capstone.storage.backend.exceptions.ItemIsNullException;
import capstone.storage.backend.exceptions.ItemResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Optional;

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
    void getItemResponseFromArrayReturnsTheItemResponse() {
        //GIVEN
        ItemResponse itemResponseToGet = new ItemResponse(
                "test",
                "8710847909610",
                "test",
                "test");

        ItemResponse[] itemResponseArray = {itemResponseToGet
        };

        String eanToFind = "8710847909610";
        //WHEN
        Optional<ItemResponse> actual = eanApiService.getItemResponseFromArray(itemResponseArray, eanToFind);
        ItemResponse expected = itemResponseToGet;
        //THEN
        assertEquals(expected, actual.get());
    }

    @Test
    void ExpectItemIsNullException() {
        //GIVEN
        ItemResponse itemResponseToGet = new ItemResponse(
                "test",
                "8710847909610",
                "test",
                "test");

        ItemResponse[] itemResponseArray = {itemResponseToGet
        };

        String eanToFind = "123";
        //WHEN
        try {
            eanApiService.getItemResponseFromArray(itemResponseArray, eanToFind);
            fail();
        }
        //THEN
        catch (ItemIsNullException e) {
            String expected = "item not found";
            String actual = e.getMessage();

            assertEquals(expected, actual);
        }

    }

    @Test
    @DisplayName("expect ItemResponseException with message (item response list is null or invalid)")
    void expectItemResponseExceptionFromGetItemResponseMethod() {
        //GIVEN


        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        //WHEN
        try {
            eanApiService.getItemResponse("123");
            fail();
        }
        //THEN
        catch (ItemResponseException e) {
            String expected = "item response list is null or invalid";
            String actual = e.getMessage();

            assertEquals(expected, actual);
        }

    }
}