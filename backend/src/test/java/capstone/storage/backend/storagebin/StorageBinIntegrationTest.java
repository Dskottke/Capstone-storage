package capstone.storage.backend.storagebin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class StorageBinIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("GET -> expect empty list and HTTP-status 200")
    void getRequestShouldReturn_Status200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/storagebins/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("""
                        []
                        """));
    }
}
