package capstone.storage.backend.item.models;

public record Product(
        String name,
        String ean,
        String categoryName,
        String issuingCountry) {
}
