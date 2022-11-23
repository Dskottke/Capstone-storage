package capstone.storage.backend;


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

        ItemResponse[] itemResponses = {new ItemResponse(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(itemResponses))
                .setResponseCode(200));


        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/8710847909610")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Item itemResponse = objectMapper.readValue(body, Item.class);

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
                                 "storeableValue": "20"}]
                                 """.replace("<id>", itemResponse.id())));

    }

    @Test
    @DirtiesContext
    void updateStorableValueFromExistingItemToValue20() throws Exception {
        //GIVEN
        ItemResponse[] itemResponses = {new ItemResponse(
                "test",
                "8710847909610",
                "test",
                "GER")};

        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(itemResponses))
                .setResponseCode(200));


        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/8710847909610")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Item itemResponse = objectMapper.readValue(body, Item.class);

        String id = itemResponse.id();
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
                                "storeableValue": "10"}""".replace("<id>", id)))


                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                        "id" : "<id>",
                        "name": "test",
                        "categoryName": "test",
                        "issuingCountry": "GER",
                        "ean":"8710847909610",
                        "storeableValue": "10"}""".replace("<id>", id)));
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
                                "storeableValue": "10"}"""))
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
                                "storeableValue": "10"}"""))
                //THEN
                .andExpect(status().is(400));
    }


    @Test
    @DirtiesContext
    void deleteExistingItemByIdAndReturnStatus204() throws Exception {
        //GIVEN
        ItemResponse[] itemResponses = {new ItemResponse(
                "test",
                "8710847909610",
                "test",
                "GER")};


        mockWebServer.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(itemResponses))
                .setResponseCode(200));


        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/items/8710847909610")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Item itemResponse = objectMapper.readValue(body, Item.class);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/items/" + itemResponse.id())
                //THEN
        ).andExpect(status().is(204));

    }

    @Test
    @DirtiesContext
    void TryToDeleteNotExistingItemByIdAndReturnStatus404() throws Exception {
        //GIVEN
        String id = "123";
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/items/" + id)
                //THEN
        ).andExpect(status().is(404));

    }


}
