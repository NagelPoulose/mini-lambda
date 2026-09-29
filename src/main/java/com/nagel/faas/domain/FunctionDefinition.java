package com.nagel.faas.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record FunctionDefinition(
		UUID id,
		String name,
		String source,
		Instant createdAt
) {
	public FunctionDefinition {
		Objects.requireNonNull(id, "id must not be null");
		Objects.requireNonNull(createdAt, "createdAt must not be null");

		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("name must not be blank");
		}

		if (source == null || source.isBlank()) {
			throw new IllegalArgumentException("source must not be blank");
		}
	}
}
