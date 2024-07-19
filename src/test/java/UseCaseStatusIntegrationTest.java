import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UseCaseStatusIntegrationTest {

    Javalin app = new ServerJava().javalinApp(); // inject any dependencies you might have

    /**
     * Cas nominal pour l'use case 1 : pas de soucis, code retour 200
     */
    @Test
    void useCase1_IntegrationTest_nominalCase() {
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.get("/api/status").code()).isEqualTo(200);
            assertThat(client.get("/api/status").body().string()).isEqualTo("Hello World");
        });
    }
}
