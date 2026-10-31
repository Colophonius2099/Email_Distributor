package ScoregDataSctructure;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.type.LogicalType;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder.withCoercionConfig(
                LogicalType.POJO,
                config -> config.setCoercion(
                        CoercionInputShape.EmptyString,
                        CoercionAction.AsNull
                )
        );
    }
}