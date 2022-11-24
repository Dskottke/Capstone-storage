package capstone.storage.backend;

import capstone.storage.backend.exceptions.ItemNotFound;
import capstone.storage.backend.exceptions.EanApiResponseException;
import capstone.storage.backend.models.ItemResponse;
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

    public ItemResponse getItemResponseFromApi(String eanToFind) {
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
            throw new EanApiResponseException("item response list is null or invalid");
        }
        ItemResponse itemResponse = itemResponseList[0];
        String firstEan = itemResponse.ean();

        if (firstEan == null) {
            throw new EanApiResponseException("ean is null");
        }
        if (!(firstEan.equals(eanToFind))) {
            throw new ItemNotFound("item not found");
        }
        return itemResponse;
    }
}
