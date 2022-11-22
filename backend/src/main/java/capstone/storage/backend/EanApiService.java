package capstone.storage.backend;
import capstone.storage.backend.exceptions.ItemIsNullException;
import capstone.storage.backend.exceptions.ItemResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static java.util.Objects.requireNonNull;

@Service
public class EanApiService {
    private final WebClient webClient;
    private static final int EXPECTED_ARRAY_LENGTH = 1;
    private final String apiToken;

    public EanApiService(@Value("${ean.api.url}") String basicUrl, @Value("${token}") String token) {
        this.apiToken = token;
        this.webClient = WebClient.create(basicUrl);
    }

    public ItemResponse getItemResponse(String eanToFind) {

        ResponseEntity<ItemResponse[]> itemResponseEntity = requireNonNull(webClient
                        .get()
                        .uri("api=?token=" + apiToken + "&op=barcode-lookup&format=json&ean=" + eanToFind)
                        .retrieve()
                        .toEntity(ItemResponse[].class)
                        .block(),

                "ResponseEntity is null"
        );
        ItemResponse[] itemResponseList = itemResponseEntity.getBody();

        if (itemResponseList == null || itemResponseList.length != EXPECTED_ARRAY_LENGTH) {
            throw new ItemResponseException("item response list is null or invalid");
        }
        ItemResponse itemResponse = itemResponseList[0];
        String firstEan = itemResponse.ean();

        if (firstEan == null) {
            throw new ItemResponseException("item is null");
        }
        if (!(firstEan.equals(eanToFind))) {
            throw new ItemIsNullException("item not found");
        }
        return itemResponse;
    }

}
