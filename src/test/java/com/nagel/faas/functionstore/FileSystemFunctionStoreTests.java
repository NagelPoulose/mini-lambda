package com.nagel.faas.functionstore;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.nagel.faas.domain.FunctionDefinition;
import tools.jackson.databind.json.JsonMapper;

class FileSystemFunctionStoreTests {

	@TempDir
	Path temporaryDirectory;

	@Test
	void savesMetadataAndSourceInUuidDirectory() throws IOException {
		JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
		FileSystemFunctionStore store = new FileSystemFunctionStore(temporaryDirectory, jsonMapper);
		UUID id = UUID.fromString("2d27b95f-0d78-4f5b-8722-732cd2e601f2");
		Instant createdAt = Instant.parse("2026-10-03T16:30:00Z");
		String source = "print('{}')";
		FunctionDefinition function = new FunctionDefinition(id, "hello", source, createdAt);

		store.save(function);

		Path functionDirectory = temporaryDirectory.resolve(id.toString());
		String metadata = Files.readString(functionDirectory.resolve("metadata.json"));
		String savedSource = Files.readString(functionDirectory.resolve("function.py"));

		assertThat(metadata).contains(id.toString(), "hello", createdAt.toString());
		assertThat(savedSource).isEqualTo(source);
	}
}
