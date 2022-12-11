package capstone.storage.backend.item;
import capstone.storage.backend.ExceptionMessage;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.item.models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private static MockWebServer mockWebServer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("ean.api.url", () -> mockWebServer.url("/").toString());
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("GET -> expect empty list and HTTP-status 200")
    void getAllItemsAndExpectEmtpyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/items/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    @DisplayName("POST -> itemResponse field ean is null expect HTTP-status 400 and content: itemResponseEanNullExceptionMessage")
    void postRequestWithItemResponseWithEanNullAndExpectStatus400() throws Exception {
        Product[] product = {
                new Product(
                        "test123",
                        null,
                        "test123",
                        "GER")};
        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(product))
                .setResponseCode(200));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "10",
                                    "ean" : "8710847909610",
                                    "itemNumber": "12345"
                                }"""))
                .andExpect(status().is(400))
                .andExpect(content().string("couldn't find item by ean"));
    }

    @Test
    @DirtiesContext
    @DisplayName("POST -> add item and expect HTTP-status 201 and matching content")
    void addItemWithEanFromApiAndExpectItemWithId() throws Exception {
        //GIVEN
        Product[] product = {new Product(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(product))
                .setResponseCode(200));

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storableValue" : 10,
                                "ean" : "8710847909610",
                                "itemNumber" : 123
                                }"""))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Item mockItemResponse = objectMapper.readValue(body, Item.class);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/items/"))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id": "<id>",
                                "name": "test",
                                "categoryName": "test",
                                "issuingCountry": "GER",
                                "ean": "8710847909610",
                                "storableValue": 10}]
                                """.replace("<id>", mockItemResponse.id())));
    }

    @Test
    @DirtiesContext
    @DisplayName("PUT -> update existing item expect HTTP-status 200")
    void updateStorableValueFromExistingItemToValue20() throws Exception {
        //GIVEN
        Product[] product = {new Product(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(product))
                .setResponseCode(200));

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storableValue" : 10,
                                "ean" : "8710847909610",
                                "itemNumber" : "123"
                                }"""))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Item mockItemResponse = objectMapper.readValue(body, Item.class);

        String id = mockItemResponse.id();
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id" : "<id>",
                                "name": "test",
                                "categoryName": "test",
                                "issuingCountry": "GER",
                                "ean":"8710847909610",
                                "storableValue": 10}""".replace("<id>", id)))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                        "id" : "<id>",
                        "name": "test",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean":"8710847909610",
                        "storableValue": 10}""".replace("<id>", id)));
    }

    @Test
    @DirtiesContext
    @DisplayName("PUT -> should return ITEM_IS_NOT_EXISTING Exception because there is no existing Item ")
    void updateWithNotMatchingPathvariableIdAndItemId() throws Exception {
        //GIVEN

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id" : "1234",
                                "name": "test",
                                "categoryName": "test",
                                "issuingCountry": "GER",
                                "ean":"8710847909610",
                                "storableValue": "10"}"""))
                //THEN
                .andExpect(status().is(400)).andExpect(content().string(ExceptionMessage.ITEM_IS_NOT_EXISTING_MESSAGE.toString()));
    }

    @Test
    @DirtiesContext
    @DisplayName("DELETE -> delete existing item expect HTTP-Status 204")
    void deleteExistingItemByIdAndReturnStatus204() throws Exception {
        //GIVEN
        Product[] product = {new Product(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(product))
                .setResponseCode(200));

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storableValue" : "10",
                                "ean" : "8710847909610",
                                "itemNumber" : "123"
                                }"""))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Item mockItemResponse = objectMapper.readValue(body, Item.class);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/items/" + mockItemResponse.id()))
                //THEN
                .andExpect(status().is(204));
    }

    @Test
    @DirtiesContext
    @DisplayName("DELETE -> item to delete doesn't exist expect HTTP-status 404 and content : itemToDeleteNotFoundException ")
    void tryToDeleteNotExistingItemByIdAndReturnStatus404() throws Exception {
        //GIVEN
        String id = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/items/" + id))
                //THEN
                .andExpect(status().is(404))
                .andExpect(content().string(ExceptionMessage.ITEM_TO_DELETE_NOT_FOUND_EXCEPTION_MESSAGE.toString()));
    }


    @Test
    @DirtiesContext
    @DisplayName("POST -> item-number less than 1 expect HTTP-status 400 and content: IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE")
    void postWithItemNumberLessThan1AndExpect_Status400() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : 2,
                                    "ean" : "123",
                                    "itemNumber": 0
                                }"""))
                //THEN
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString()));
    }

    @Test
    @DirtiesContext
    @DisplayName("POST -> storable-value less than 1 expect HTTP-status 400 and content: IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE ")
    void postWithStorableValueLessThan1AndExpect_Status400() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : 0,
                                    "ean" : "123",
                                    "itemNumber": 1
                                }"""))
                //THEN
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString()));
    }


    @Test
    @DisplayName("POST -> item-number is null expect HTTP-status 400 and content: isNullOrEmptyException")
    @DirtiesContext
    void postWithItemNumberNullExpectStatus_400() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "1",
                                    "ean" : "123",
                                    "itemNumber": null
                                }"""))
                //THEN
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString()));
    }

    @Test
    @DirtiesContext
    @DisplayName("POST -> item-number has empty string expect HTTP-status 400 and content: isNullOrEmptyException")
    void postWithItemNumberEmptyStringExpect_Status400() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "1",
                                    "ean" : "123",
                                    "itemNumber": ""
                                }"""))
                //THEN
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString()));
    }

    @Test
    @DirtiesContext
    @DisplayName("POST -> ean has empty string expect HTTP-status 400 and content: isNullOrEmptyException")
    void postWithEanEmptyStringExpectStatus_400() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "1",
                                    "ean" : "",
                                    "itemNumber": "1"
                                }"""))
                //THEN
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString()));
    }

    @Test
    @DirtiesContext
    @DisplayName("POST -> storable value has empty string expect HTTP-status 400 and content: IsNullOrEmptyException")
    void postWithStorableValueEmptyStringExpect_Status400() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "",
                                    "ean" : "123",
                                    "itemNumber": "1"
                                }"""))
                //THEN
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString()));
    }

    @Test
    @DirtiesContext
    @DisplayName("POST -> with already existing Item-Number itemAlreadyExistExceptionMessage ")
    void postWithExistingItemNumberAndExpectStatus400() throws Exception {
        //GIVEN
        Product[] product = {new Product(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(product))
                .setResponseCode(200));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "10",
                                    "ean" : "8710847909610",
                                    "itemNumber": "12345"
                                }"""))
                .andExpect(status().is(201));

        //WHEN

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "10",
                                    "ean" : "8710847909610",
                                    "itemNumber": "12345"
                                }"""))
                //THEN
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.ITEM_ALREADY_EXIST_EXCEPTION_MESSAGE.toString()));

    }

}
