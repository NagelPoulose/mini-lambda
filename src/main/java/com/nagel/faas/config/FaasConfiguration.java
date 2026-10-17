package com.nagel.faas.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nagel.faas.application.FunctionDeploymentService;
import com.nagel.faas.functionstore.FileSystemFunctionStore;
import com.nagel.faas.functionstore.FunctionStore;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class FaasConfiguration {

	@Bean
	public FunctionStore functionStore(
			@Value("${faas.functions-directory}") String functionsDirectory,
			JsonMapper jsonMapper
	) {
		return new FileSystemFunctionStore(
				Path.of(functionsDirectory),
				jsonMapper
		);
	}

	@Bean
	public FunctionDeploymentService functionDeploymentService(
			FunctionStore functionStore
	) {
		return new FunctionDeploymentService(functionStore);
	}
}
