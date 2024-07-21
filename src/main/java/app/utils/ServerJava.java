package app.utils;

import app.*;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ServerJava {

    private final Javalin app;
    private static final Logger logger = LoggerFactory.getLogger(ServerJava.class);
    private final Map<String, Champion> champions = new HashMap<>();
    private final Game game = new Game();

    public ServerJava() {
        app = Javalin.create()
                .get("/api/status", ctx -> {
                    logger.debug("Status handler triggered", ctx);
                    ctx.status(200);
                    ctx.json("Hello World");
                })
                .post("/api/create", this::createChampion)
                .post("/api/modify", this::modifyChampion)
                .post("/api/team", this::createTeam)
                .get("/api/begin", this::beginGame)
                .get("/api/prediction", this::predictWinner);
    }

    private void createChampion(io.javalin.http.Context ctx) {
        try {
            Champion champion = ctx.bodyAsClass(Champion.class);
            if (champions.containsKey(champion.getChampionName().toLowerCase())) {
                ctx.status(400).json("Champion already exists");
            } else if (champion.getLifePoints() < 100 || champion.getLifePoints() > 150) {
                ctx.status(400).json("Invalid life points");
            } else {
                champions.put(champion.getChampionName().toLowerCase(), champion);
                ctx.status(200).json("Champion created successfully");
            }
        } catch (Exception e) {
            ctx.status(400).json("Invalid JSON: " + e.getMessage());
        }
    }

    private void modifyChampion(io.javalin.http.Context ctx) {
        try {
            Champion modifiedChampion = ctx.bodyAsClass(Champion.class);
            String championName = modifiedChampion.getChampionName().toLowerCase();
            if (!champions.containsKey(championName)) {
                ctx.status(404).json("Champion not found");
            } else {
                Champion existingChampion = champions.get(championName);
                if (modifiedChampion.getLifePoints() != 0) {
                    existingChampion.setLifePoints(modifiedChampion.getLifePoints());
                }
                if (modifiedChampion.getAbilities() != null && !modifiedChampion.getAbilities().isEmpty()) {
                    existingChampion.getAbilities().addAll(modifiedChampion.getAbilities());
                }
                ctx.status(200).json("Champion modified successfully");
            }
        } catch (Exception e) {
            ctx.status(400).json("Invalid JSON: " + e.getMessage());
        }
    }

    private void createTeam(io.javalin.http.Context ctx) {
        try {
            Team team = ctx.bodyAsClass(Team.class);
            if (team.getChampionsDistribution().size() > 5) {
                ctx.status(400).json("Team size cannot exceed 5 champions");
            } else if (!allChampionsExist(team)) {
                ctx.status(400).json("One or more champions do not exist");
            } else if (hasDuplicatesWithinTeam(team)) {
                ctx.status(400).json("Duplicate champions not allowed within a team");
            } else if (hasDuplicatesBetweenTeams(team)) {
                ctx.status(400).json("Duplicate champions not allowed between teams");
            } else if (game.isStarted()) {
                ctx.status(400).json("Game has already started");
            } else {
                if (team.getTeamName() == TeamName.BLUE) {
                    game.setBlueTeam(team);
                } else {
                    game.setRedTeam(team);
                }
                ctx.status(200).json("Team created successfully");
            }
        } catch (Exception e) {
            ctx.status(400).json("Invalid JSON: " + e.getMessage());
        }
    }

    private boolean allChampionsExist(Team team) {
        for (ChampionDistribution cd : team.getChampionsDistribution()) {
            if (!champions.containsKey(cd.getChampionName().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasDuplicatesWithinTeam(Team team) {
        Set<String> championNames = new HashSet<>();
        for (ChampionDistribution cd : team.getChampionsDistribution()) {
            if (!championNames.add(cd.getChampionName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDuplicatesBetweenTeams(Team newTeam) {
        Team existingTeam = (newTeam.getTeamName() == TeamName.BLUE) ? game.getRedTeam() : game.getBlueTeam();
        if (existingTeam == null) {
            return false;
        }

        Set<String> existingChampionNames = existingTeam.getChampionsDistribution().stream()
                .map(cd -> cd.getChampionName().toLowerCase())
                .collect(Collectors.toSet());

        for (ChampionDistribution cd : newTeam.getChampionsDistribution()) {
            if (existingChampionNames.contains(cd.getChampionName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void beginGame(io.javalin.http.Context ctx) {
        if (game.getBlueTeam() == null || game.getRedTeam() == null) {
            ctx.status(400).json("Both teams must be set before starting the game");
        } else if (game.getBlueTeam().getChampionsDistribution().size() != 5 ||
                game.getRedTeam().getChampionsDistribution().size() != 5) {
            ctx.status(400).json("Both teams must have exactly 5 champions");
        } else {
            game.setStarted(true);
            ctx.status(200).json("Bienvenue sur la Faille de l'Invocateur !");
        }
    }

    private void predictWinner(io.javalin.http.Context ctx) {
        if (!game.isStarted()) {
            ctx.status(400).json("Game has not started yet");
        } else {
            List<Map<String, Object>> predictions = calculatePredictions();
            ctx.status(200).json(predictions);
        }
    }

    private List<Map<String, Object>> calculatePredictions() {
        List<Map<String, Object>> predictions = new ArrayList<>();
        Map<String, Champion> blueChampions = getChampionsByLane(game.getBlueTeam());
        Map<String, Champion> redChampions = getChampionsByLane(game.getRedTeam());

        for (Lane lane : Lane.values()) {
            Champion blueChampion = blueChampions.get(lane.name());
            Champion redChampion = redChampions.get(lane.name());

            TeamName winningTeam = calculateLaneWinner(blueChampion, redChampion);

            Map<String, Object> prediction = new HashMap<>();
            prediction.put("laneName", lane.name());
            prediction.put("winningTeam", winningTeam.toString());
            predictions.add(prediction);
        }

        return predictions;
    }

    private Map<String, Champion> getChampionsByLane(Team team) {
        Map<String, Champion> championsByLane = new HashMap<>();
        for (ChampionDistribution cd : team.getChampionsDistribution()) {
            championsByLane.put(cd.getLane().toString(), champions.get(cd.getChampionName().toLowerCase()));
        }
        return championsByLane;
    }

    private TeamName calculateLaneWinner(Champion blueChampion, Champion redChampion) {
        if (blueChampion == null || redChampion == null) {
            return blueChampion == null ? TeamName.RED : TeamName.BLUE;
        }

        int blueHitsToKill = (int) Math.ceil((double) redChampion.getLifePoints() / blueChampion.getStrongestAbilityDamage());
        int redHitsToKill = (int) Math.ceil((double) blueChampion.getLifePoints() / redChampion.getStrongestAbilityDamage());

        return blueHitsToKill <= redHitsToKill ? TeamName.BLUE : TeamName.RED;
    }


    public Javalin javalinApp() {
        return app;
    }
}
