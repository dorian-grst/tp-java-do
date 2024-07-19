import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UseCase2IntegrationTest {

    Javalin app = new ServerJava().javalinApp(); // inject any dependencies you might have

    @Test
    void useCase2_createTeam_OK_nominalCase() throws IOException {

    }
}
