package com.nagel.faas.application;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.nagel.faas.domain.FunctionDefinition;
import com.nagel.faas.functionstore.FunctionStore;

public final class FunctionDeploymentService {

	private final FunctionStore functionStore;

	public FunctionDeploymentService(FunctionStore functionStore) {
		this.functionStore = Objects.requireNonNull(
				functionStore,
				"functionStore must not be null"
		);
	}

	public FunctionDefinition deploy(String name, String source) {
		FunctionDefinition function = new FunctionDefinition(
				UUID.randomUUID(),
				name,
				source,
				Instant.now()
		);

		functionStore.save(function);
		return function;
	}
}
