package app.utils;

import app.App;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ServerJava {

    private final Javalin app;
    private static final Logger logger = LoggerFactory.getLogger(ServerJava.class);

    public ServerJava() {
        // TODO : compléter les endpoints pour que les tests passent au vert !
        app = Javalin.create()
                .get("/api/status", ctx -> {
                    logger.debug("Status handler triggered", ctx);
                    ctx.status(200);
                    ctx.json("Hello World");
                })
                // USE CASE 1.1 - Création de champion
                .post("/api/create", ctx -> {
                    // Champion champion = ctx.bodyAsClass(Champion.class);
                })
                // USE CASE 1.2 - Modification de champion
                .post("/api/modify", ctx -> {
                    //ModifyChampionRequest request = ctx.bodyAsClass(ModifyChampionRequest.class);
                })
                // USE CASE 2 - Remplissage de l'équipe
                .get("/api/team", ctx -> {
                    // Team teamNames = ctx.bodyAsClass(Team.class);
                })
                // USE CASE 3 - Lancement de la partie
                .get("/api/begin", ctx -> {
                })
                // USE CASE 4 - Rechercher les champions par lane
                .get("/api/prediction", ctx -> {
                })

        ;
    }

    public Javalin javalinApp() {
        return app;
    }
}
