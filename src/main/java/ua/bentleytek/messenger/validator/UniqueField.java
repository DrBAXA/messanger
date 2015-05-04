package ua.bentleytek.messenger.validator;


public enum UniqueField {
    NAME("name"),
    EMAIL("email");

    private final String value;

    UniqueField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
