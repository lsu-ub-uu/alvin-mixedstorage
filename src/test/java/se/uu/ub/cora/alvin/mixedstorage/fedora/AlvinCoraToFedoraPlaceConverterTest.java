/*
 * Copyright 2019 Uppsala University Library
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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.alvin.mixedstorage.DataAtomicSpy;
import se.uu.ub.cora.alvin.mixedstorage.DataGroupSpy;
import se.uu.ub.cora.alvin.mixedstorage.resource.ResourceReader;
import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataAttributeFactory;
import se.uu.ub.cora.data.DataAttributeProvider;
import se.uu.ub.cora.data.DataGroup;

public class AlvinCoraToFedoraPlaceConverterTest {

	private DataAttributeFactory dataAttributeFactory;

	@BeforeMethod
	public void setUp() {
		dataAttributeFactory = new DataAttibuteFactorySpy();
		DataAttributeProvider.setDataAttributeFactory(dataAttributeFactory);
	}

	@Test
	public void testConvertToFedoraXML() throws Exception {

		HttpHandlerFactorySpy httpHandlerFactory = new HttpHandlerFactorySpy();
		httpHandlerFactory.responseCodes.add(201);
		httpHandlerFactory.responseTexts.add(ResourceReader.readResourceAsString("place/679.xml"));

		String fedoraURL = "someFedoraURL";
		AlvinCoraToFedoraConverter converter = AlvinCoraToFedoraPlaceConverter
				.usingHttpHandlerFactoryAndFedoraUrl(httpHandlerFactory, fedoraURL);
		DataGroup dataRecord = createPlaceDataGroupUsingPid("alvin-place:679");

		String xml = converter.toXML(dataRecord);
		assertEquals(httpHandlerFactory.factoredHttpHandlers.size(), 1);
		assertEquals(httpHandlerFactory.urls.get(0),
				fedoraURL + "objects/alvin-place:679/datastreams/METADATA/content");

		HttpHandlerSpy httpHandler = httpHandlerFactory.factoredHttpHandlers.get(0);
		assertEquals(httpHandler.requestMethod, "GET");

		assertEquals(xml, ResourceReader.readResourceAsString("place/expectedUpdated679.xml"));

	}

	private DataGroup createPlaceDataGroupUsingPid(String id) {
		DataGroup dataRecord = new DataGroupSpy("authority");
		dataRecord.addAttributeByIdWithValue("type", "place");
		DataGroup recordInfo = new DataGroupSpy("recordInfo");
		dataRecord.addChild(recordInfo);
		recordInfo.addChild(new DataAtomicSpy("id", id));

		DataGroup createdBy = new DataGroupSpy("createdBy");
		recordInfo.addChild(createdBy);
		createdBy.addChild(new DataAtomicSpy("linkedRecordType", "coraUser"));
		createdBy.addChild(new DataAtomicSpy("linkedRecordId", "user1234"));

		recordInfo.addChild(new DataAtomicSpy("tsCreated", "2019-03-11 09:27:22.306"));

		DataGroup authorizedNameGroup = new DataGroupSpy("name");
		dataRecord.addChild(authorizedNameGroup);
		authorizedNameGroup.addAttributeByIdWithValue("type", "authorized");

		DataGroup defaultNameGroup = new DataGroupSpy("namePart");
		authorizedNameGroup.addChild(defaultNameGroup);
		defaultNameGroup.addAttributeByIdWithValue("type", "defaultName");

		DataAtomic defaultNameValue = new DataAtomicSpy("value", "CORA_Uppsala_CORA");
		defaultNameGroup.addChild(defaultNameValue);

		return dataRecord;
	}

	@Test
	public void testConvertToNewFedoraXML() throws Exception {
		HttpHandlerFactorySpy httpHandlerFactory = new HttpHandlerFactorySpy();

		String fedoraURL = "someFedoraURL";
		AlvinCoraToFedoraPlaceConverter converter = AlvinCoraToFedoraPlaceConverter
				.usingHttpHandlerFactoryAndFedoraUrl(httpHandlerFactory, fedoraURL);

		DataGroup dataRecord = createPlaceDataGroupUsingPid("alvin-place:680");

		String xml = converter.toNewXML(dataRecord);

		assertEquals(xml, ResourceReader.readResourceAsString("place/expectedCreated680.xml"));

	}

}
