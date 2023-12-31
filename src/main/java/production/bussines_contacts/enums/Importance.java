package production.bussines_contacts.enums;

public enum Importance {
    HIGH(1, "High"),
    MEDIUM(2, "Medium"),
    LOW(3, "Low");

    private final Integer id;
    private final String description;

    Importance(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Importance fromDescription(String description) {
        for (Importance importance : Importance.values()) {
            if (importance.getDescription().equalsIgnoreCase(description)) {
                return importance;
            }
        }
        throw new IllegalArgumentException("No importance with description: " + description);
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
