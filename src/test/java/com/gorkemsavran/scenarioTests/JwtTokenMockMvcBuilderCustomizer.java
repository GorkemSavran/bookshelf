package com.gorkemsavran.scenarioTests;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;

public class JwtTokenMockMvcBuilderCustomizer implements MockMvcConfigurer {

    private final String jwtToken;

    public JwtTokenMockMvcBuilderCustomizer(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public void afterConfigurerAdded(ConfigurableMockMvcBuilder<?> builder) {
        RequestBuilder jwtTokenRequestBuilder = MockMvcRequestBuilders.get("/*")
                .header("Authorization", "Bearer " + jwtToken);
        builder.defaultRequest(jwtTokenRequestBuilder);
    }
}
