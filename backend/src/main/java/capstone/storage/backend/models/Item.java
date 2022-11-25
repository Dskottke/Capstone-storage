package capstone.storage.backend.models;

public record Item(String id,
                   String name,
                   String categoryName,
                   String issuingCountry,
                   String ean,
                   String storableValue,
                   String itemNumber
) {
}
