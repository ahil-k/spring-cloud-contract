/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.contract.verifier.builder;

import java.util.Arrays;

import org.springframework.cloud.contract.verifier.config.TestFramework;

class JUnit5Imports implements Imports {

	private final BlockBuilder blockBuilder;

	private final GeneratedClassMetaData generatedClassMetaData;

	private static final String[] IMPORTS = { "org.junit.jupiter.api.Test",
			"org.junit.jupiter.api.extension.ExtendWith", "org.junit.jupiter.api.Tag", "org.junit.jupiter.api.Timeout",
			"java.util.concurrent.TimeUnit", "org.junit.jupiter.params.ParameterizedTest",
			"org.junit.jupiter.params.provider.ArgumentsSource", "org.junit.jupiter.api.RepeatedTest",
			"org.junit.jupiter.api.*", "org.junit.jupiter.api.parallel.Execution",
			"org.junit.jupiter.api.parallel.ExecutionMode" };

	JUnit5Imports(BlockBuilder blockBuilder, GeneratedClassMetaData generatedClassMetaData) {
		this.blockBuilder = blockBuilder;
		this.generatedClassMetaData = generatedClassMetaData;
	}

	@Override
	public Imports call() {
		Arrays.stream(IMPORTS).forEach(s -> this.blockBuilder.addLineWithEnding("import " + s));
		return this;
	}

	@Override
	public boolean accept() {
		return this.generatedClassMetaData.configProperties.getTestFramework() == TestFramework.JUNIT5;
	}

}
