package capstone.storage.backend;

public record Item(String id,
                   String name,
                   String categoryName,
                   String issuingCountry,
                   String ean,
                   String storeableValue,
                   String itemNumber
) {
}
