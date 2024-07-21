package app;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    SUPPORT, COMBATTANT, MAGE, TIREUR, ASSASSIN, AUTRE;

    @JsonCreator
    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return AUTRE;
    }
}
