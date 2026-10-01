package com.nagel.faas.functionstore;

import java.nio.file.Path;
import java.util.Objects;

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
}
