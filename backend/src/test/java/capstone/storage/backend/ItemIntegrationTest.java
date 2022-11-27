package capstone.storage.backend;

import capstone.storage.backend.models.Item;
import capstone.storage.backend.models.ItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
    @Autowired
    private ObjectMapper objectMapper;

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
    void getAllItemsAndExpectEmtpyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/items/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    @DirtiesContext
    void addItemWithEanFromApiAndExpectItemWithId() throws Exception {
        //GIVEN
        ItemResponse[] itemResponse = {new ItemResponse(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(itemResponse))
                .setResponseCode(200));

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/8710847909610")
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
                                 "storableValue": "10"}]
                                 """.replace("<id>", mockItemResponse.id())));
    }

    @Test
    @DirtiesContext
    void updateStorableValueFromExistingItemToValue20() throws Exception {
        //GIVEN
        ItemResponse[] itemResponse = {new ItemResponse(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(itemResponse))
                .setResponseCode(200));

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/8710847909610")
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

        String id = mockItemResponse.id();
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id" : "<id>",
                                "name": "test",
                                "categoryName": "test",
                                "issuingCountry": "GER",
                                "ean":"8710847909610",
                                "storableValue": "10"}""".replace("<id>", id)))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                        "id" : "<id>",
                        "name": "test",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean":"8710847909610",
                        "storableValue": "10"}""".replace("<id>", id)));
    }

    @DirtiesContext
    @Test
    void updateNotExistingItemAndExpectStatus201() throws Exception {
        //GIVEN
        String id = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id" : "123",
                                "name": "test",
                                "categoryName": "test",
                                "issuingCountry": "GER",
                                "ean":"8710847909610",
                                "storableValue": "10"}"""))
                //THEN
                .andExpect(status().is(201));
    }

    @DirtiesContext
    @Test
    void updateWithNotMatchingPathvariableIdAndItemId() throws Exception {
        //GIVEN
        String id = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/items/" + id)
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
                .andExpect(status().is(406));
    }

    @Test
    @DirtiesContext
    void deleteExistingItemByIdAndReturnStatus204() throws Exception {
        //GIVEN
        ItemResponse[] itemResponse = {new ItemResponse(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(itemResponse))
                .setResponseCode(200));

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/8710847909610")
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
    void tryToDeleteNotExistingItemByIdAndReturnStatus404() throws Exception {
        //GIVEN
        String id = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/items/" + id))
                //THEN
                .andExpect(status().is(404));
    }

    @Test
    @DirtiesContext
    void postWithAlreadyExistingItemNumberAndExpectStatus400() throws Exception {
        //GIVEN
        String ean = "8710847909610";
        ItemResponse[] itemResponse = {new ItemResponse(
                "test",
                ean,
                "test",
                "GER")};
        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(itemResponse))
                .setResponseCode(200));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "10",
                                    "ean" : "8710847909610",
                                    "itemNumber": "12345"
                                }"""))
                .andExpect(status().is(201));

        //WHEN
        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(itemResponse))
                .setResponseCode(200));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "10",
                                    "ean" : "8710847909610",
                                    "itemNumber": "12345"
                                }"""))
                //THEN
                .andExpect(status().is(400));
    }

    @Test
    void postWithNotMatchingPathvariableEanAndEanFromRequestBodyAndExpectStatus403() throws Exception {
        //GIVEN
        String ean = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "10",
                                    "ean" : "1234",
                                    "itemNumber": "12345"
                                }"""))
                //THEN
                .andExpect(status().is(406));
    }

    @DirtiesContext
    @Test
    void postWithItemNumberLessThan1AndExpectStatus412() throws Exception {
        //GIVEN
        String ean = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "2",
                                    "ean" : "123",
                                    "itemNumber": "0"
                                }"""))
                //THEN
                .andExpect(status().is(412));
    }

    @DirtiesContext
    @Test
    void postWithStorableValueLessThan1AndExpectStatus412() throws Exception {
        //GIVEN
        String ean = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "0",
                                    "ean" : "123",
                                    "itemNumber": "1"
                                }"""))
                //THEN
                .andExpect(status().is(412));
    }

    @DirtiesContext
    @Test
    void postWithStorableValueNullAndExpectStatus404() throws Exception {
        //GIVEN
        String ean = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : null,
                                    "ean" : "123",
                                    "itemNumber": "1"
                                }"""))
                //THEN
                .andExpect(status().is(404));
    }

    @DirtiesContext
    @Test
    void postWithItemNumberNullExpectStatus404() throws Exception {
        //GIVEN
        String ean = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "1",
                                    "ean" : "123",
                                    "itemNumber": null
                                }"""))
                //THEN
                .andExpect(status().is(404));
    }

    @DirtiesContext
    @Test
    void postWithItemNumberEmptyStringExpectStatus405() throws Exception {
        //GIVEN
        String ean = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "1",
                                    "ean" : "123",
                                    "itemNumber": ""
                                }"""))
                //THEN
                .andExpect(status().is(404));
    }

    @DirtiesContext
    @Test
    void postWithEanEmptyStringExpectStatus405() throws Exception {
        //GIVEN
        String ean = "";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "1",
                                    "ean" : "",
                                    "itemNumber": "1"
                                }"""))
                //THEN
                .andExpect(status().is(405));
    }

    @DirtiesContext
    @Test
    void postWithStorableValueEmptyStringExpectStatus404() throws Exception {
        //GIVEN
        String ean = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/" + ean)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "storableValue" : "",
                                    "ean" : "123",
                                    "itemNumber": "1"
                                }"""))
                //THEN
                .andExpect(status().is(404));
    }


}
