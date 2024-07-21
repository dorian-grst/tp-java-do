package app;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Champion {
    private String championName;
    private Role role;
    private int lifePoints;
    private List<Ability> abilities;

    public String getChampionName() {
        return championName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(Object lifePoints) {
        if (lifePoints instanceof Integer) {
            this.lifePoints = (Integer) lifePoints;
        } else if (lifePoints instanceof String) {
            try {
                this.lifePoints = Integer.parseInt((String) lifePoints);
            } catch (NumberFormatException e) {
                // Ignore invalid values
            }
        }
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public void setChampionName(String championName) {
        this.championName = capitalizeFirstLetter(championName);
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public int getStrongestAbilityDamage() {
        return abilities.stream().mapToInt(Ability::getDamage).max().orElse(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Champion champion = (Champion) o;
        return Objects.equals(championName, champion.championName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(championName);
    }
}
