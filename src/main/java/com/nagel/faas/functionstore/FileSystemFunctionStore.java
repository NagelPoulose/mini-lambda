package com.nagel.faas.functionstore;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.nagel.faas.domain.FunctionDefinition;
import tools.jackson.databind.json.JsonMapper;

public final class FileSystemFunctionStore {

	private final Path functionsDirectory;
	private final JsonMapper jsonMapper;

	public FileSystemFunctionStore(Path functionsDirectory, JsonMapper jsonMapper) {
		this.functionsDirectory = Objects.requireNonNull(
				functionsDirectory,
				"functionsDirectory must not be null"
		);
		this.jsonMapper = Objects.requireNonNull(jsonMapper, "jsonMapper must not be null");
	}

	public void save(FunctionDefinition function) {
		Objects.requireNonNull(function, "function must not be null");

		Path functionDirectory = functionsDirectory.resolve(function.id().toString());

		try {
			Files.createDirectories(functionsDirectory);
			Files.createDirectory(functionDirectory);
			jsonMapper.writeValue(
					functionDirectory.resolve("metadata.json").toFile(),
					new FunctionMetadata(function.id(), function.name(), function.createdAt())
			);
			Files.writeString(
					functionDirectory.resolve("function.py"),
					function.source()
			);
		} catch (IOException exception) {
			throw new FunctionStoreException(
					"Failed to save function " + function.id(),
					exception
			);
		}
	}

	private record FunctionMetadata(UUID id, String name, Instant createdAt) {
	}
}
