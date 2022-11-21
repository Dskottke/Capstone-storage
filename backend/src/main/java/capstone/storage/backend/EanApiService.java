package capstone.storage.backend;
import capstone.storage.backend.exceptions.ItemIsNullException;
import capstone.storage.backend.exceptions.ItemResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Optional;

@Service
public class EanApiService {
    private final WebClient webClient;
    private static final int EXPECTED_ARRAY_LENGTH = 1;

    private final String url;


    public EanApiService(@Value("${ean.api.url}") String basicUrl, @Value("${ean.api.token}") String token) {
        this.url = token;
        this.webClient = WebClient.create(basicUrl);

    }

    public Optional<ItemResponse> getArticleResponseFromArray(ItemResponse[] itemResponsesList, String eanToFind) {

        return Optional.of(
                Arrays.stream(itemResponsesList)
                        .filter(find -> find.ean().equals(eanToFind))
                        .findFirst()
                        .orElseThrow(() -> new ItemIsNullException("item not found")));
    }

    public ItemResponse getArticleResponse(String eanToFind) {

        ResponseEntity<ItemResponse[]> itemResponse = webClient
                .get()
                .uri(url + "&op=barcode-lookup&format=json&ean=" + eanToFind)
                .retrieve()
                .toEntity(ItemResponse[].class)
                .block();


        if (itemResponse != null) {

            ItemResponse[] itemResponseList = itemResponse.getBody();

            if (itemResponseList != null && itemResponseList.length == EXPECTED_ARRAY_LENGTH) {

                if (itemResponseList[0].ean() != null) {

                    return getArticleResponseFromArray(itemResponseList, eanToFind)
                            .orElseThrow(() -> new ItemResponseException("no item found"));

                } else {
                    throw new ItemResponseException("item is null");
                }
            } else {
                throw new ItemResponseException("item response list is null or invalid");
            }
        } else {
            throw new ItemResponseException("item response is null");
        }
    }
}
