package capstone.storage.backend.drivingorders;
import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.exceptions.ExceptionMessage;
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

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
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

    @Test
    @DisplayName("POST -> should return HTTP-Status 400 and IsNullOrEmptyExceptionMessage because all fields are null ")
    @DirtiesContext
    void addNewInputDrivingOrderWithAllFieldsNullAndExpectStatus400andIsNullOrEmptyExceptionMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storageLocationId" : null,
                                "itemNumber" : null,
                                "amount" : null
                                }"""))
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString()));

    }

    @Test
    @DisplayName("POST -> should return HTTP-Status 400 and IsNullOrEmptyExceptionMessage because the RequestBody is null ")
    @DirtiesContext
    void addNewInputDrivingOrderWithRequestBodyNullAndExpectStatus400andIsNullOrEmptyException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString()));

    }

    @Test
    @DisplayName("DELETE -> should return HTTP-Status 204")
    @DirtiesContext
    void drivingOrderDoneShouldReturnStatus204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/test-data"))
                .andExpect(status().is(204));

        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storageLocationId" : "1",
                                "itemNumber" : "1",
                                "amount" : "10"
                                }"""))
                .andExpect(status().is(201)).andReturn().getResponse().getContentAsString();

        DrivingOrder drivingOrder = objectMapper.readValue(body, DrivingOrder.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/driving-orders/input/" + drivingOrder.id()))
                .andExpect(status().is(204));
    }

    @Test
    @DisplayName("DELETE -> should return HTTP-Status 404 and ITEM_TO_DELETE_NOT_FOUND_EXCEPTION_MESSAGE")
    @DirtiesContext
    void drivingOrderDoneShouldReturnStatus400() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/driving-orders/input/123"))
                .andExpect(status().is(404))
                .andExpect(content().string(ExceptionMessage.ORDER_TO_DELETE_NOT_FOUND_EXCEPTION_MESSAGE.toString()));
    }

    @Test
    @DisplayName("POST -> should return HTTP-Status 400 and ITEM_OR_STORAGE_BIN_NOT_EXISTING_EXCEPTION_MESSAGE ")
    @DirtiesContext
    void addNewInputDrivingOrderWithNotExistingStorageLocationIdShouldReturnStatus400() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storageLocationId" : "101",
                                "itemNumber" : "1",
                                "amount" : "10"
                                }"""))
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.ITEM_OR_STORAGE_BIN_NOT_EXISTING_EXCEPTION_MESSAGE.toString()));
    }

    @Test
    @DisplayName("POST -> should return HTTP-Status 400 and IS_NOT_ENOUGH_SPACE_EXCEPTION_MESSAGE ")
    @DirtiesContext
    void addNewInputDrivingOrderWithAmountGreaterThanCapacity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/test-data"))
                .andExpect(status().is(204));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storageLocationId" : "1",
                                "itemNumber" : "1",
                                "amount" : "10"
                                }"""))
                .andExpect(status().is(201));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storageLocationId" : "1",
                                "itemNumber" : "1",
                                "amount" : "20"
                                }"""))
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.IS_NOT_ENOUGH_SPACE_EXCEPTION_MESSAGE.toString()));
    }

    @Test
    @DirtiesContext
    @DisplayName("POST -> should return status 400 and STORAGE_BIN_FALSE_ITEM_EXCEPTION_MESSAGE")
    void addNewInputDrivingOrderWithExistingInputOrderWithMatchingStorageButDifferentItemNumbers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/test-data"))
                .andExpect(status().is(204));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storageLocationId" : "1",
                                "itemNumber" : "1",
                                "amount" : "10"
                                }"""))
                .andExpect(status().is(201));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/driving-orders/?type=INPUT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "storageLocationId" : "1",
                                "itemNumber" : "2",
                                "amount" : "10"
                                }"""))
                .andExpect(status().is(400))
                .andExpect(content().string(ExceptionMessage.STORAGE_BIN_FALSE_ITEM_EXCEPTION_MESSAGE.toString()));

    }

}