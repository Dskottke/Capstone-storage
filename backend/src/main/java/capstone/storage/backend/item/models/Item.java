package capstone.storage.backend.item.models;

public record Item(String id,
                   String name,
                   String categoryName,
                   String issuingCountry,
                   String ean,
                   String storableValue,
                   String itemNumber
) {
}
