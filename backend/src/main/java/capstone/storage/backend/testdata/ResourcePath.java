package capstone.storage.backend.testdata;

public enum ResourcePath {
    STORAGE_PATH("/storage.json"),
    ITEM_PATH("/item.json");

    private final String path;

    ResourcePath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
