package capstone.storage.backend.drivingorders;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DrivingOrderIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

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
}