package com.nagel.faas.functionstore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

	@Test
	void findsSavedFunctionById() {
		JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
		FileSystemFunctionStore store = new FileSystemFunctionStore(
				temporaryDirectory,
				jsonMapper
		);
		UUID id = UUID.fromString("2d27b95f-0d78-4f5b-8722-732cd2e601f2");
		FunctionDefinition function = new FunctionDefinition(
				id,
				"hello",
				"print('{}')",
				Instant.parse("2026-10-03T16:30:00Z")
		);

		store.save(function);

		assertThat(store.findById(id)).contains(function);
	}

	@Test
	void returnsEmptyWhenFunctionDoesNotExist() {
		JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
		FileSystemFunctionStore store = new FileSystemFunctionStore(
				temporaryDirectory,
				jsonMapper
		);
		UUID missingId = UUID.fromString("88c6c0ef-f23a-4017-ad4c-ab729089229f");

		assertThat(store.findById(missingId)).isEmpty();
	}

	@Test
	void rejectsSavingTheSameFunctionTwice() {
		JsonMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
		FileSystemFunctionStore store = new FileSystemFunctionStore(
				temporaryDirectory,
				jsonMapper
		);
		FunctionDefinition function = new FunctionDefinition(
				UUID.fromString("2d27b95f-0d78-4f5b-8722-732cd2e601f2"),
				"hello",
				"print('{}')",
				Instant.parse("2026-10-03T16:30:00Z")
		);

		store.save(function);

		assertThatThrownBy(() -> store.save(function))
				.isInstanceOf(FunctionStoreException.class);
	}
}
