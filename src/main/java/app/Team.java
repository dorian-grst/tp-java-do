package app;

import java.util.List;

public class Team {
    private TeamName teamName;
    private List<ChampionDistribution> championsDistribution;

    public TeamName getTeamName() {
        return teamName;
    }

    public void setTeamName(TeamName teamName) {
        this.teamName = teamName;
    }

    public List<ChampionDistribution> getChampionsDistribution() {
        return championsDistribution;
    }

    public void setChampionsDistribution(List<ChampionDistribution> championsDistribution) {
        this.championsDistribution = championsDistribution;
    }
}
