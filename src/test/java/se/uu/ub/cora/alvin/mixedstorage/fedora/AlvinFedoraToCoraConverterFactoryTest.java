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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.alvin.mixedstorage.NotImplementedException;
import se.uu.ub.cora.httphandler.HttpHandlerFactoryImp;

public class AlvinFedoraToCoraConverterFactoryTest {
	private AlvinFedoraToCoraConverterFactoryImp alvinToCoraConverterFactoryImp;
	private String fedoraURL = "someFedoraURL";
	private TransformationFactorySpy transformationFactory;

	@BeforeMethod
	public void beforeMethod() {
		transformationFactory = new TransformationFactorySpy();
		alvinToCoraConverterFactoryImp = AlvinFedoraToCoraConverterFactoryImp
				.usingFedoraURLAndTransformationFactory(fedoraURL, transformationFactory);
	}

	@Test(expectedExceptions = NotImplementedException.class, expectedExceptionsMessageRegExp = ""
			+ "No converter implemented for: someType")
	public void factorUnknownTypeThrowsException() throws Exception {
		alvinToCoraConverterFactoryImp.factorToCoraConverter("someType");
	}

	@Test
	public void testFactoryPlace() throws Exception {
		AlvinFedoraToCoraPlaceConverter converter = (AlvinFedoraToCoraPlaceConverter) alvinToCoraConverterFactoryImp
				.factorToCoraConverter("place");
		assertNotNull(converter.getCoraTransformation());
		assertSame(converter.getCoraTransformation(), transformationFactory.transformationSpy);
		assertEquals(transformationFactory.xsltPath, "alvinxslt/AlvinFedoraToCoraPlace.xsl");
	}

	@Test(expectedExceptions = NotImplementedException.class, expectedExceptionsMessageRegExp = ""
			+ "No to Fedora converter implemented for: someUnknownType")
	public void testFactorToFedoraConverterUnkownTypeThrowsException() throws Exception {
		alvinToCoraConverterFactoryImp.factorToFedoraConverter("someUnknownType");
	}

	@Test
	public void testFactorToFedoraForPlaceIsInstanceOfCorrectType() throws Exception {
		AlvinCoraToFedoraConverter converter = alvinToCoraConverterFactoryImp
				.factorToFedoraConverter("place");
		assertTrue(converter instanceof AlvinCoraToFedoraPlaceConverter);
	}

	@Test
	public void testFactorToFedoraForPlaceHasCorrectDependencies() throws Exception {
		AlvinCoraToFedoraPlaceConverter converter = (AlvinCoraToFedoraPlaceConverter) alvinToCoraConverterFactoryImp
				.factorToFedoraConverter("place");
		assertTrue(converter.getHttpHandlerFactory() instanceof HttpHandlerFactoryImp);
		assertEquals(converter.getFedorURL(), fedoraURL);
	}

	@Test
	public void testGetFedoraURLNeededForTests() throws Exception {
		assertEquals(alvinToCoraConverterFactoryImp.getFedoraURL(), fedoraURL);
	}
}
