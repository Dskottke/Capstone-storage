package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DrivingOrderIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET -> should return empty list of all driving-orders with type INPUT")
    void getAllDrivingOrdersByTypeAndExpectStatus200AndEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/driving-orders/?type=INPUT"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("""
                                []
                                """));


    }

    @Test
    @DisplayName("POST -> should return HTTP-Status 201 and Content ")
    @DirtiesContext
    void addNewInputDrivingOrderAndExpectStatus201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/test-data"))
                .andExpect(status().is(204));

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/input")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storageLocationId" : "1",
                                "itemNumber" : "1",
                                "amount" : "10"
                                }"""))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        DrivingOrder drivingOrder = objectMapper.readValue(body, DrivingOrder.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/driving-orders/?type=INPUT"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id": "<id>",
                                "storageLocationId": "1",
                                "itemNumber": "1",
                                "type":"INPUT",
                                "amount": "10"}]
                                """.replace("<id>", drivingOrder.id())));
    }
}