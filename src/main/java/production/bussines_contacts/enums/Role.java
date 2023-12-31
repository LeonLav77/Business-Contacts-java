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

    public static Role fromRoleName(String roleName) {
        for (Role role : Role.values()) {
            if (role.getRoleName().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role with name: " + roleName);
    }

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }
}
