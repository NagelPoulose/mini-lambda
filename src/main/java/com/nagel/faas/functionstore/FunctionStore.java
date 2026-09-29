package com.nagel.faas.functionstore;

import java.util.Optional;
import java.util.UUID;

import com.nagel.faas.domain.FunctionDefinition;

public interface FunctionStore {

	void save(FunctionDefinition function);

	Optional<FunctionDefinition> findById(UUID id);
}
