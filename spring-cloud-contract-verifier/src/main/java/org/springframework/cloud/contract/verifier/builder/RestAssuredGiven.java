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
import java.util.LinkedList;
import java.util.List;

import org.springframework.cloud.contract.verifier.file.SingleContractMetadata;

class RestAssuredGiven implements Given, BodyMethodVisitor, RestAssuredAcceptor {

	private final BlockBuilder blockBuilder;

	private final GeneratedClassMetaData generatedClassMetaData;

	private final List<Given> requestGivens = new LinkedList<>();

	private final List<Given> bodyGivens = new LinkedList<>();

	RestAssuredGiven(BlockBuilder blockBuilder, GeneratedClassMetaData generatedClassMetaData, BodyParser bodyParser) {
		this.blockBuilder = blockBuilder;
		this.generatedClassMetaData = generatedClassMetaData;
		this.requestGivens.addAll(Arrays.asList(new MockMvcRequestGiven(blockBuilder, generatedClassMetaData),
				new SpockMockMvcRequestGiven(blockBuilder, generatedClassMetaData),
				new ExplicitRequestGiven(blockBuilder, generatedClassMetaData),
				new WebTestClientRequestGiven(blockBuilder, generatedClassMetaData)));
		this.bodyGivens
				.addAll(Arrays.asList(new MockMvcHeadersGiven(blockBuilder), new MockMvcCookiesGiven(blockBuilder),
						new MockMvcBodyGiven(blockBuilder, generatedClassMetaData, bodyParser),
						new JavaMultipartGiven(blockBuilder, generatedClassMetaData, bodyParser),
						new SpockMockMvcMultipartGiven(blockBuilder, generatedClassMetaData, bodyParser)));
	}

	@Override
	public MethodVisitor<Given> apply(SingleContractMetadata singleContractMetadata) {
		startBodyBlock(this.blockBuilder, "given:");
		this.blockBuilder
				.addLineWithEnding(String.format("var name = \"%s\"", singleContractMetadata.getContract().getName()));
		this.blockBuilder.addLineWithEnding(
				String.format("var description = \"%s\"", singleContractMetadata.getContract().getDescription()));
		if (null != singleContractMetadata.getContract().getRequest().getBefore()) {
			this.blockBuilder.addLineWithEnding(
					singleContractMetadata.getContract().getRequest().getBefore().getExecutionCommand());
		}
		this.blockBuilder.addEndingIfNotPresent();
		addRequestGivenLine(singleContractMetadata);
		String baseUri = singleContractMetadata.getContract().getRequest().getBaseUri();
		this.blockBuilder.addEmptyLine().addIndented("\t\t.log().everything()");
		if (null != baseUri) {
			this.blockBuilder.addEmptyLine().addIndented(String.format("\t\t.baseUri(renderContent(\"%s\"))", baseUri));
		}
		else {
			this.blockBuilder.addEmptyLine().addIndented("\t\t.baseUri(BASE_URI)");
		}
		indentedBodyBlock(this.blockBuilder, this.bodyGivens, singleContractMetadata);
		this.blockBuilder.addEmptyLine();
		return this;
	}

	private void addRequestGivenLine(SingleContractMetadata singleContractMetadata) {
		this.requestGivens.stream().filter(given -> given.accept(singleContractMetadata)).findFirst().orElseThrow(
				() -> new IllegalStateException("No matching request building Given implementation for Rest Assured"))
				.apply(singleContractMetadata);
	}

	@Override
	public boolean accept(SingleContractMetadata singleContractMetadata) {
		return acceptType(this.generatedClassMetaData, singleContractMetadata);
	}

}
