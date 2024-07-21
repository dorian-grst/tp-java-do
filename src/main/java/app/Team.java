package app;

import java.util.List;

public class Team {
    private TeamName teamName;
    private List<ChampionDistribution> championsDistribution;

    public TeamName getTeamName() {
        return teamName;
    }

    public List<ChampionDistribution> getChampionsDistribution() {
        return championsDistribution;
    }
}
