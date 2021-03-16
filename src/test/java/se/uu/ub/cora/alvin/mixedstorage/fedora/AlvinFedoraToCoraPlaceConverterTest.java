/*
 * Copyright 2018 Uppsala University Library
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
import static org.testng.Assert.assertFalse;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.alvin.mixedstorage.DataAtomicFactorySpy;
import se.uu.ub.cora.alvin.mixedstorage.DataGroupFactorySpy;
import se.uu.ub.cora.alvin.mixedstorage.log.LoggerFactorySpy;
import se.uu.ub.cora.data.DataAtomicFactory;
import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataAttribute;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupFactory;
import se.uu.ub.cora.data.DataGroupProvider;
import se.uu.ub.cora.data.DataRecordLinkFactory;
import se.uu.ub.cora.data.DataRecordLinkProvider;
import se.uu.ub.cora.logger.LoggerProvider;

public class AlvinFedoraToCoraPlaceConverterTest {

	private AlvinFedoraToCoraPlaceConverter converter;
	private DataGroupFactory dataGroupFactory;
	private DataAtomicFactory dataAtomicFactory;
	private DataRecordLinkFactory dataRecordLinkFactory;
	private LoggerFactorySpy loggerFactorySpy;

	@BeforeMethod
	public void beforeMethod() {
		loggerFactorySpy = new LoggerFactorySpy();
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		dataGroupFactory = new DataGroupFactorySpy();
		DataGroupProvider.setDataGroupFactory(dataGroupFactory);
		dataRecordLinkFactory = new DataRecordLinkFactorySpy();
		DataRecordLinkProvider.setDataRecordLinkFactory(dataRecordLinkFactory);
		dataAtomicFactory = new DataAtomicFactorySpy();
		DataAtomicProvider.setDataAtomicFactory(dataAtomicFactory);
		converter = new AlvinFedoraToCoraPlaceConverter();
	}

	@Test
	public void convertFromXML() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place22XML);
		assertEquals(placeDataGroup.getNameInData(), "authority");

		String attributeValueForType = placeDataGroup.getAttribute("type").getValue();
		assertEquals(attributeValueForType, "place");

		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");
		DataGroup type = recordInfo.getFirstGroupWithNameInData("type");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordType"), "recordType");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordId"), "place");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType"), "system");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "alvin");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "alvin-place:22");

		DataGroup createdBy = recordInfo.getFirstGroupWithNameInData("createdBy");
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordType"), "user");
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordId"), "12345");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("tsCreated"),
				"2014-12-18T20:20:38.346000Z");

		List<DataGroup> updatedList = recordInfo.getAllGroupsWithNameInData("updated");
		assertEquals(updatedList.size(), 2);

		assertCorrectUpdateWithRepeatIdAndTsUpdated(updatedList.get(0), "0",
				"2014-12-18T20:20:38.346000Z");

		assertCorrectUpdateWithRepeatIdAndTsUpdated(updatedList.get(1), "1",
				"2014-12-18T20:21:20.880000Z");

		DataGroup defaultName = placeDataGroup.getFirstGroupWithNameInData("name");
		assertEquals(defaultName.getAttribute("type").getValue(), "authorized");
		DataGroup defaultNamePart = defaultName.getFirstGroupWithNameInData("namePart");
		assertEquals(defaultNamePart.getAttribute("type").getValue(), "defaultName");
		assertEquals(defaultNamePart.getFirstAtomicValueWithNameInData("value"), "Linköping");

		DataGroup coordinates = placeDataGroup.getFirstGroupWithNameInData("coordinates");
		assertEquals(coordinates.getFirstAtomicValueWithNameInData("latitude"), "58.42");
		assertEquals(coordinates.getFirstAtomicValueWithNameInData("longitude"), "15.62");

		String country = placeDataGroup.getFirstAtomicValueWithNameInData("country");
		assertEquals(country, "SE");

	}

	@Test
	public void convertFnromXML24() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place24XML);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");
		DataGroup type = recordInfo.getFirstGroupWithNameInData("type");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordType"), "recordType");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordId"), "place");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType"), "system");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "alvin");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "alvin-place:24");

		DataGroup createdBy = recordInfo.getFirstGroupWithNameInData("createdBy");
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordType"), "user");
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordId"), "12345");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("tsCreated"),
				"2014-12-18T22:16:44.623000Z");

		List<DataGroup> updatedList = recordInfo.getAllGroupsWithNameInData("updated");
		assertEquals(updatedList.size(), 3);
		assertCorrectUpdateWithRepeatIdAndTsUpdated(updatedList.get(0), "0",
				"2014-12-18T22:16:44.623000Z");
		assertCorrectUpdateWithRepeatIdAndTsUpdated(updatedList.get(1), "1",
				"2014-12-18T22:18:01.276000Z");

		assertCorrectUpdateWithRepeatIdAndTsUpdated(updatedList.get(2), "2",
				"2016-02-12T10:29:43.147000Z");

		DataGroup defaultName = placeDataGroup.getFirstGroupWithNameInData("name");
		assertEquals(defaultName.getAttribute("type").getValue(), "authorized");
		DataGroup defaultNamePart = defaultName.getFirstGroupWithNameInData("namePart");
		assertEquals(defaultNamePart.getAttribute("type").getValue(), "defaultName");
		assertEquals(defaultNamePart.getFirstAtomicValueWithNameInData("value"), "Lund");

		DataGroup coordinates = placeDataGroup.getFirstGroupWithNameInData("coordinates");
		assertEquals(coordinates.getFirstAtomicValueWithNameInData("latitude"), "55.7");
		assertEquals(coordinates.getFirstAtomicValueWithNameInData("longitude"), "13.18");

		String country = placeDataGroup.getFirstAtomicValueWithNameInData("country");
		assertEquals(country, "SE");
	}

	private void assertCorrectUpdateWithRepeatIdAndTsUpdated(DataGroup updated, String repeatId,
			String tsUpdated) {
		assertEquals(updated.getRepeatId(), repeatId);

		DataGroup updatedBy = updated.getFirstGroupWithNameInData("updatedBy");
		assertEquals(updatedBy.getFirstAtomicValueWithNameInData("linkedRecordType"), "user");
		assertEquals(updatedBy.getFirstAtomicValueWithNameInData("linkedRecordId"), "12345");

		assertEquals(updated.getFirstAtomicValueWithNameInData("tsUpdated"), tsUpdated);
	}

	@Test
	public void convertFromXMLNoCountry() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place22_noCountry_XML);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");
		DataGroup type = recordInfo.getFirstGroupWithNameInData("type");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordType"), "recordType");
		assertEquals(type.getFirstAtomicValueWithNameInData("linkedRecordId"), "place");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordType"), "system");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "alvin");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "alvin-place:22_2");

		DataGroup createdBy = recordInfo.getFirstGroupWithNameInData("createdBy");
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordType"), "user");
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordId"), "12345");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("tsCreated"),
				"2014-12-18T20:20:38.346000Z");

		List<DataGroup> updatedList = recordInfo.getAllGroupsWithNameInData("updated");
		assertEquals(updatedList.size(), 2);
		assertCorrectUpdateWithRepeatIdAndTsUpdated(updatedList.get(0), "0",
				"2014-12-18T20:20:38.346000Z");
		assertCorrectUpdateWithRepeatIdAndTsUpdated(updatedList.get(1), "1",
				"2014-12-18T20:21:20.880000Z");

		DataGroup defaultName = placeDataGroup.getFirstGroupWithNameInData("name");
		assertEquals(defaultName.getAttribute("type").getValue(), "authorized");
		DataGroup defaultNamePart = defaultName.getFirstGroupWithNameInData("namePart");
		assertEquals(defaultNamePart.getAttribute("type").getValue(), "defaultName");
		assertEquals(defaultNamePart.getFirstAtomicValueWithNameInData("value"), "Linköping");

		DataGroup coordinates = placeDataGroup.getFirstGroupWithNameInData("coordinates");
		assertEquals(coordinates.getFirstAtomicValueWithNameInData("latitude"), "58.42");
		assertEquals(coordinates.getFirstAtomicValueWithNameInData("longitude"), "15.62");

		assertFalse(placeDataGroup.containsChildWithNameInData("country"));
	}

	@Test
	public void convertFromXMLHistoricCountry() {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place22_historicCountry_XML);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "alvin-place:22_historic");

		assertFalse(placeDataGroup.containsChildWithNameInData("country"));

		String country = placeDataGroup.getFirstAtomicValueWithNameInData("historicCountry");
		assertEquals(country, "gaul");
	}

	@Test
	public void convertFromXMLHistoricCountryNonCamelCasedId() {
		DataGroup placeDataGroup = converter
				.fromXML(TestDataProvider.place22_historicCountryNonCamelCased_XML);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "alvin-place:22_historic");

		assertFalse(placeDataGroup.containsChildWithNameInData("country"));

		String country = placeDataGroup.getFirstAtomicValueWithNameInData("historicCountry");
		assertEquals(country, "duchy_of_saxe_coburg_meiningen");
	}

	@Test
	public void convertFromXMLHistoricCountryNonNormalized() {
		DataGroup placeDataGroup = converter
				.fromXML(TestDataProvider.place22_historicCountryNonNormalized_XML);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "alvin-place:22_historic");

		assertFalse(placeDataGroup.containsChildWithNameInData("country"));

		String country = placeDataGroup.getFirstAtomicValueWithNameInData("historicCountry");
		assertEquals(country, "lordship_trcka_lipa");
	}

	@Test
	public void convertFromXMLHistoricCountryNonCamelCalsedAndNonNormalized() {
		DataGroup placeDataGroup = converter
				.fromXML(TestDataProvider.place22_historicCountryNonCamelCasedAndNonNormalized_XML);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "alvin-place:22_historic");

		assertFalse(placeDataGroup.containsChildWithNameInData("country"));

		String country = placeDataGroup.getFirstAtomicValueWithNameInData("historicCountry");
		assertEquals(country, "lordship_trcka_lipa");
	}

	@Test
	public void convertFromXMLNoCoordinates() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place24NoCoordinates);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"),
				"alvin-place:24_noCoordinates");

		assertFalse(placeDataGroup.containsChildWithNameInData("coordinates"));
	}

	@Test
	public void convertFromXMLNoLatitude() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place24NoLatitudeXML);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"),
				"alvin-place:24_noLatitude");

		assertFalse(placeDataGroup.containsChildWithNameInData("coordinates"));
	}

	@Test
	public void convertFromXMLNoLongitude() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place24NoLongitudeXML);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"),
				"alvin-place:24_noLongitude");

		assertFalse(placeDataGroup.containsChildWithNameInData("coordinates"));
	}

	@Test
	public void convertFromXMLNoUpdatedInfoSetsToSameAsCreatedInfo() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place5NoTsUpdated);
		assertEquals(placeDataGroup.getNameInData(), "authority");
		DataGroup recordInfo = placeDataGroup.getFirstGroupWithNameInData("recordInfo");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "alvin-place:5");

		DataGroup createdBy = recordInfo.getFirstGroupWithNameInData("createdBy");
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordType"), "user");
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordId"), "12345");

		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("tsCreated"),
				"2017-10-27T22:36:51.991000Z");

		List<DataGroup> updatedList = recordInfo.getAllGroupsWithNameInData("updated");
		assertEquals(updatedList.size(), 1);
		assertCorrectUpdateWithRepeatIdAndTsUpdated(updatedList.get(0), "0",
				"2017-10-27T22:36:51.991000Z");
	}

	@Test
	public void convertFromXMLNoAlternativeName() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place22_noCountry_XML);
		DataAttribute alternativeAttribute = new DataAttributeSpy("type", "alternative");

		List<DataGroup> alternativeNames = (List<DataGroup>) placeDataGroup
				.getAllGroupsWithNameInDataAndAttributes("name", alternativeAttribute);
		assertEquals(alternativeNames.size(), 0);
	}

	@Test
	public void convertAlternativeNameFromXML24() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place24XML);

		DataGroup defaultName = placeDataGroup.getFirstGroupWithNameInData("name");
		assertEquals(defaultName.getAttribute("type").getValue(), "authorized");
		DataGroup defaultNamePart = defaultName.getFirstGroupWithNameInData("namePart");
		assertEquals(defaultNamePart.getAttribute("type").getValue(), "defaultName");
		assertEquals(defaultNamePart.getFirstAtomicValueWithNameInData("value"), "Lund");
		DataAttribute alternativeAttribute = new DataAttributeSpy("type", "alternative");

		List<DataGroup> alternativeNames = (List<DataGroup>) placeDataGroup
				.getAllGroupsWithNameInDataAndAttributes("name", alternativeAttribute);
		assertEquals(alternativeNames.size(), 1);

		DataGroup alternativeName = alternativeNames.get(0);
		assertEquals(alternativeName.getRepeatId(), "0");
		assertCorrectAlternativeName("lat", "Londini Gothorum", alternativeName);

	}

	@Test
	public void convertTwoAlternativeNamesFromXML24() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place24DoublePlacesXML);

		DataGroup defaultName = placeDataGroup.getFirstGroupWithNameInData("name");
		assertEquals(defaultName.getAttribute("type").getValue(), "authorized");
		DataGroup defaultNamePart = defaultName.getFirstGroupWithNameInData("namePart");
		assertEquals(defaultNamePart.getAttribute("type").getValue(), "defaultName");
		assertEquals(defaultNamePart.getFirstAtomicValueWithNameInData("value"), "Lund");
		DataAttribute alternativeAttribute = new DataAttributeSpy("type", "alternative");

		List<DataGroup> alternativeNames = (List<DataGroup>) placeDataGroup
				.getAllGroupsWithNameInDataAndAttributes("name", alternativeAttribute);
		assertEquals(alternativeNames.size(), 2);

		DataGroup alternativeName = alternativeNames.get(0);
		assertEquals(alternativeName.getRepeatId(), "0");
		assertCorrectAlternativeName("lat", "Londini Gothorum", alternativeNames.get(0));

		DataGroup otherAlternativeName = alternativeNames.get(1);
		assertEquals(otherAlternativeName.getRepeatId(), "1");
		assertCorrectAlternativeName("swe", "Ankeborg", otherAlternativeName);

	}

	private void assertCorrectAlternativeName(String languageCode, String name,
			DataGroup alternativeName) {
		String language = alternativeName.getFirstAtomicValueWithNameInData("language");
		assertEquals(language, languageCode);
		DataGroup alternativeNamePart = alternativeName.getFirstGroupWithNameInData("namePart");
		assertEquals(alternativeNamePart.getAttribute("type").getValue(), "defaultName");
		assertEquals(alternativeNamePart.getFirstAtomicValueWithNameInData("value"), name);
	}

	@Test
	public void convertFromXMLNoLocalIdentifiers() throws Exception {
		DataGroup placeDataGroup = converter
				.fromXML(TestDataProvider.place22_noLocalIdentifiers_XML);
		assertFalse(placeDataGroup.containsChildWithNameInData("identifier"));
	}

	@Test
	public void convertFromXMLLocalIdentifier() throws Exception {
		DataGroup placeDataGroup = converter.fromXML(TestDataProvider.place22XML);
		DataGroup identifierGroup = placeDataGroup.getFirstGroupWithNameInData("identifier");
		assertCorrectIdentifierGroup(identifierGroup, "0", "waller", "1367");
	}

	protected void assertCorrectIdentifierGroup(DataGroup identifierGroup, String repeatId,
			String type, String value) {
		assertEquals(identifierGroup.getRepeatId(), repeatId);
		assertEquals(identifierGroup.getFirstAtomicValueWithNameInData("identifierType"), type);
		assertEquals(identifierGroup.getFirstAtomicValueWithNameInData("identifierValue"), value);
	}

	@Test
	public void convertFromXMLTwoLocalIdentifier() throws Exception {
		DataGroup placeDataGroup = converter
				.fromXML(TestDataProvider.place22_twoLocalIdentifiers_XML);

		List<DataGroup> identifiers = placeDataGroup.getAllGroupsWithNameInData("identifier");

		assertCorrectIdentifierGroup(identifiers.get(0), "0", "waller", "1367");
		assertCorrectIdentifierGroup(identifiers.get(1), "1", "waller", "666");
	}
}
