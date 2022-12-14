package capstone.storage.backend.item;

import capstone.storage.backend.exceptions.ApiItemNotFoundException;
import capstone.storage.backend.exceptions.EanApiResponseException;
import capstone.storage.backend.exceptions.ItemResponseEanNullException;
import capstone.storage.backend.item.models.Product;
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

    /**
     * This method performs an api request for a product with an ean.
     * It also handles the response check before returning the product.
     *
     * @param eanToFind the ean that is used for the api request
     * @return product
     */
    public Product getItemResponseFromApi(String eanToFind) {
        ResponseEntity<Product[]> itemResponseEntity = requireNonNull(webClient
                        .get()
                        .uri("api=?token=" + apiToken + "&op=barcode-lookup&format=json&ean=" + eanToFind)
                        .retrieve()
                        .toEntity(Product[].class)
                        .block(),

                "ResponseEntity is null"
        );
        Product[] productList = itemResponseEntity.getBody();

        if (productList == null || productList.length != EXPECTED_ARRAY_LENGTH) {
            throw new EanApiResponseException();
        }
        Product product = productList[0];
        String firstEan = product.ean();

        if (firstEan == null) {
            throw new ItemResponseEanNullException(firstEan);
        }
        if (!(firstEan.equals(eanToFind))) {
            throw new ApiItemNotFoundException();
        }
        return product;

    }
}
