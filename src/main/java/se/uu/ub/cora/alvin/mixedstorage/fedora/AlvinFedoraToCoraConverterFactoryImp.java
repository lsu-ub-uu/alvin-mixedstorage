/*
 * Copyright 2018, 2019, 2021 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.alvin.mixedstorage.fedora;

import se.uu.ub.cora.alvin.mixedstorage.NotImplementedException;
import se.uu.ub.cora.httphandler.HttpHandlerFactoryImp;
import se.uu.ub.cora.xmlutils.transformer.CoraTransformation;
import se.uu.ub.cora.xmlutils.transformer.CoraTransformationFactory;

public class AlvinFedoraToCoraConverterFactoryImp implements AlvinFedoraConverterFactory {

	private static final String XSLT_PATH = "alvinxslt/AlvinFedoraToCoraPlace.xsl";
	private String fedoraURL;
	private CoraTransformationFactory transformationFactory;

	public static AlvinFedoraToCoraConverterFactoryImp usingFedoraURLAndTransformationFactory(String fedoraURL,
			CoraTransformationFactory transformationFactory) {
		return new AlvinFedoraToCoraConverterFactoryImp(fedoraURL, transformationFactory);
	}

	private AlvinFedoraToCoraConverterFactoryImp(String fedoraURL,
			CoraTransformationFactory transformationFactory) {
		this.fedoraURL = fedoraURL;
		this.transformationFactory = transformationFactory;
	}

	@Override
	public AlvinFedoraToCoraConverter factorToCoraConverter(String type) {
		if ("place".equals(type)) {
			CoraTransformation transformation = transformationFactory.factor(XSLT_PATH);
			return new AlvinFedoraToCoraPlaceConverter(transformation);
		}
		throw NotImplementedException.withMessage("No converter implemented for: " + type);
	}

	@Override
	public AlvinCoraToFedoraConverter factorToFedoraConverter(String type) {
		if ("place".equals(type)) {
			return createCoraToFedoraConverter();
		}
		throw NotImplementedException
				.withMessage("No to Fedora converter implemented for: " + type);
	}

	private AlvinCoraToFedoraConverter createCoraToFedoraConverter() {
		HttpHandlerFactoryImp httpHandlerFactory = new HttpHandlerFactoryImp();
		return AlvinCoraToFedoraPlaceConverter
				.usingHttpHandlerFactoryAndFedoraUrl(httpHandlerFactory, fedoraURL);
	}

	public String getFedoraURL() {
		// needed for test
		return fedoraURL;
	}

	public CoraTransformationFactory getCoraTransformerFactory() {
		// needed for test
		return transformationFactory;
	}
}
