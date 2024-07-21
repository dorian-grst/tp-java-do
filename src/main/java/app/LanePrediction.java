package app;

public class LanePrediction {
    private String laneName;
    private TeamName winningTeam;

    public LanePrediction(String laneName, TeamName winningTeam) {
        this.laneName = laneName;
        this.winningTeam = winningTeam;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public TeamName getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(TeamName winningTeam) {
        this.winningTeam = winningTeam;
    }
}
