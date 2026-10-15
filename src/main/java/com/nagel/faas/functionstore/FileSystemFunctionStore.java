package com.nagel.faas.functionstore;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.nagel.faas.domain.FunctionDefinition;
import tools.jackson.databind.json.JsonMapper;

public final class FileSystemFunctionStore implements FunctionStore {

	private final Path functionsDirectory;
	private final JsonMapper jsonMapper;

	public FileSystemFunctionStore(Path functionsDirectory, JsonMapper jsonMapper) {
		this.functionsDirectory = Objects.requireNonNull(
				functionsDirectory,
				"functionsDirectory must not be null"
		);
		this.jsonMapper = Objects.requireNonNull(jsonMapper, "jsonMapper must not be null");
	}

	@Override
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

	@Override
	public Optional<FunctionDefinition> findById(UUID id) {
		Objects.requireNonNull(id, "id must not be null");

		Path functionDirectory = functionsDirectory.resolve(id.toString());
		if (Files.notExists(functionDirectory)) {
			return Optional.empty();
		}

		try {
			FunctionMetadata metadata = jsonMapper.readValue(
					functionDirectory.resolve("metadata.json").toFile(),
					FunctionMetadata.class
			);
			String source = Files.readString(
					functionDirectory.resolve("function.py")
			);

			return Optional.of(
					new FunctionDefinition(
							metadata.id(),
							metadata.name(),
							source,
							metadata.createdAt()
					)
			);
		} catch (IOException exception) {
			throw new FunctionStoreException(
					"Failed to load function " + id,
					exception
			);
		}
	}

	private record FunctionMetadata(UUID id, String name, Instant createdAt) {
	}
}
