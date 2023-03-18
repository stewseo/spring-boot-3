package org.example.springdatacassandraobservability;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.observability.ObservableCqlSessionFactoryBean;
import org.springframework.data.cassandra.observability.ObservableReactiveSessionFactoryBean;

@Configuration
public class ObservabilityConfiguration {

	@Bean
	public ObservableCqlSessionFactoryBean observableCqlSession(CqlSessionBuilder builder, ObservationRegistry registry) {
		return new ObservableCqlSessionFactoryBean(builder, registry);
	}

	@Bean
	public ObservableReactiveSessionFactoryBean observableReactiveSession(CqlSession session,
			ObservationRegistry registry) {
		return new ObservableReactiveSessionFactoryBean(session, registry);
	}
}
