package production.bussines_contacts.enums;

public enum Role {
    VIEWER(1L, "Viewer"),
    ADMIN(2L, "Admin"),;

    private final Long id;
    private final String roleName;

    Role(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }
}
