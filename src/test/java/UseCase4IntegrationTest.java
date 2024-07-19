import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UseCase4IntegrationTest {

    Javalin app = new ServerJava().javalinApp(); // inject any dependencies you might have

    @Test
    void useCase1_IntegrationTest() {
    }
}
