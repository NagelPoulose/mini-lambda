package com.nagel.faas.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class FunctionDefinitionTests {

	@Test
	void rejectsBlankName() {
		assertThatThrownBy(() -> new FunctionDefinition(
				UUID.randomUUID(),
				" ",
				"print('{}')",
				Instant.now()
		))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("name must not be blank");
	}

	@Test
	void rejectsBlankSource() {
		assertThatThrownBy(() -> new FunctionDefinition(
				UUID.randomUUID(),
				"hello",
				" ",
				Instant.now()
		))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("source must not be blank");
	}
}
