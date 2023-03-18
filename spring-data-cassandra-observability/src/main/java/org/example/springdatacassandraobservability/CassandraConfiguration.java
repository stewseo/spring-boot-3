package org.example.springdatacassandraobservability;

import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfiguration {

	@Bean
	CqlSessionBuilderCustomizer cqlSessionBuilderCustomizer() {
		return cqlSessionBuilder -> cqlSessionBuilder //
				.withLocalDatacenter("datacenter1") //
				.withKeyspace("TEST");
	}
}
