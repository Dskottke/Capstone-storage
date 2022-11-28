package capstone.storage.backend.item;

import capstone.storage.backend.exceptions.EanApiResponseException;
import capstone.storage.backend.exceptions.ItemNotFound;
import capstone.storage.backend.exceptions.ItemResponseEanNullException;
import capstone.storage.backend.item.models.ItemResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static java.util.Objects.requireNonNull;

@Service
public class ItemEanApiService {
    private final WebClient webClient;
    private static final int EXPECTED_ARRAY_LENGTH = 1;
    private final String apiToken;

    public ItemEanApiService(@Value("${ean.api.url}") String basicUrl, @Value("${token}") String token) {
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
            throw new EanApiResponseException("couldn't find item by ean");
        }
        ItemResponse itemResponse = itemResponseList[0];
        String firstEan = itemResponse.ean();

        if (firstEan == null) {
            throw new ItemResponseEanNullException("couldn't find item by ean");
        }
        if (!(firstEan.equals(eanToFind))) {
            throw new ItemNotFound("ean to find doesn't match with response ean");
        }
        return itemResponse;

    }
}
