package com.energysec.infrastructure.observability;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterFilter blockUnknownEndpointMetrics() {
        return new MeterFilter() {
            @Override
            public MeterFilterReply accept(Meter.Id id) {
                if (id.getName().startsWith("http.server.requests")) {
                    String uri = id.getTag("uri");
                    if ("UNKNOWN".equals(uri) || "root".equals(uri)) {
                        return MeterFilterReply.DENY;
                    }
                }
                return MeterFilterReply.NEUTRAL;
            }
        };
    }
}
