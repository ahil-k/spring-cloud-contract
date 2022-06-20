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
import org.springframework.cloud.contract.verifier.file.SingleContractMetadata;

class JUnit5MethodAnnotation implements MethodAnnotations {

	private final BlockBuilder blockBuilder;

	private final GeneratedClassMetaData generatedClassMetaData;

	private static final String[] ANNOTATIONS = { "@Test" };

	private static final String[] PARAMETERIZED_ANNOTATIONS = { "@ParameterizedTest" };

	private static final String REPEATED_ANNOTATION = "@RepeatedTest(%d)";

	JUnit5MethodAnnotation(BlockBuilder blockBuilder, GeneratedClassMetaData generatedClassMetaData) {
		this.blockBuilder = blockBuilder;
		this.generatedClassMetaData = generatedClassMetaData;
	}

	@Override
	public MethodVisitor<MethodAnnotations> apply(SingleContractMetadata singleContractMetadata) {
		int repeat = singleContractMetadata.getContract().getRepeat();
		if (singleContractMetadata.getContract().getParameters() != null) {
			Arrays.stream(PARAMETERIZED_ANNOTATIONS).forEach(this.blockBuilder::addIndented);
			String parametersName = singleContractMetadata.getContract().getParameters().getName();
			if (parametersName != null) {
				this.blockBuilder.append(String.format("(name = \"%s\")", parametersName));
			}
		}
		else if (repeat != 0) {
			this.blockBuilder.addIndented(String.format(REPEATED_ANNOTATION, repeat));
		}
		else {
			Arrays.stream(ANNOTATIONS).forEach(this.blockBuilder::addIndented);
		}
		return this;
	}

	@Override
	public boolean accept(SingleContractMetadata singleContractMetadata) {
		return this.generatedClassMetaData.configProperties.getTestFramework() == TestFramework.JUNIT5;
	}

}
