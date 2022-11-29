package capstone.storage.backend.storagebins;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StorageBins(@JsonProperty("id") String locationNumber, String itemNumber, String amount) {
}
