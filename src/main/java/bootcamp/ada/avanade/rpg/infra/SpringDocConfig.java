package bootcamp.ada.avanade.rpg.infra;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("RPG-D&D API")
                        .description("A API RPG-D&D tem como objetivo controlar o fluxo de um jogo de RPG e persistir dados. Foi realizado academicamente durante o Bootcamp Java Academy, iniciativa da Ada em parceria com a Avanade."));
    }
}
