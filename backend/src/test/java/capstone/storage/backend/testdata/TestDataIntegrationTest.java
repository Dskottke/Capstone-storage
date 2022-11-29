package capstone.storage.backend.testdata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestDataIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST -> expect HTTP-status 204")
    void getAllItemsAndExpectEmtpyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/test-data"))
                .andExpect(status().is(204));
    }

}
