/*
 * Copyright 2018, 2019 Uppsala University Library
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
package se.uu.ub.cora.alvin.mixedstorage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class AlvinMixedRecordStorageTest {
	private RecordStorageSpy basicStorage;
	private RecordStorageSpy alvinFeodraToCoraStorage;
	private RecordStorageSpy alvinDbToCoraStorage;
	private RecordStorage alvinMixedRecordStorage;

	@BeforeMethod
	public void beforeMethod() {
		basicStorage = new RecordStorageSpy();
		alvinFeodraToCoraStorage = new RecordStorageSpy();
		alvinDbToCoraStorage = new RecordStorageSpy();
		alvinMixedRecordStorage = AlvinMixedRecordStorage.usingBasicAndFedoraAndDbStorage(
				basicStorage, alvinFeodraToCoraStorage, alvinDbToCoraStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(alvinMixedRecordStorage);
	}

	@Test
	public void alvinMixedStorageImplementsRecordStorage() {
		assertTrue(alvinMixedRecordStorage instanceof RecordStorage);
	}

	@Test
	public void readGoesToBasicStorage() throws Exception {
		assertNoInteractionWithStorage(basicStorage);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);

		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.id = "someId";
		expectedData.answer = alvinMixedRecordStorage.read(expectedData.type, expectedData.id);

		expectedData.calledMethod = "read";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);

	}

	private void assertNoInteractionWithStorage(RecordStorageSpy recordStorageSpy) {
		assertNull(recordStorageSpy.data.type);
		assertNull(recordStorageSpy.data.id);
		assertNull(recordStorageSpy.data.calledMethod);
	}

	private void assertExpectedDataSameAsInStorageSpy(RecordStorageSpy recordStorageSpy,
			RecordStorageSpyData data) {
		RecordStorageSpyData spyData = recordStorageSpy.data;
		assertEquals(spyData.type, data.type);
		assertEquals(spyData.id, data.id);
		assertEquals(spyData.calledMethod, data.calledMethod);
		assertEquals(spyData.filter, data.filter);
		assertEquals(spyData.record, data.record);
		assertEquals(spyData.collectedTerms, data.collectedTerms);
		assertEquals(spyData.linkList, data.linkList);
		assertEquals(spyData.dataDivider, data.dataDivider);

		assertEquals(spyData.answer, data.answer);
	}

	@Test
	public void readPlaceGoesToFedoraStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "place";
		expectedData.id = "someId";
		expectedData.answer = alvinMixedRecordStorage.read(expectedData.type, expectedData.id);

		expectedData.calledMethod = "read";
		assertNoInteractionWithStorage(basicStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
		assertExpectedDataSameAsInStorageSpy(alvinFeodraToCoraStorage, expectedData);
	}

	@Test
	public void readUserNotGuestGoesToAlvinToCoraStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "user";
		expectedData.id = "someUserId";
		expectedData.answer = alvinMixedRecordStorage.read(expectedData.type, expectedData.id);

		expectedData.calledMethod = "read";
		assertNoInteractionWithStorage(basicStorage);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertExpectedDataSameAsInStorageSpy(alvinDbToCoraStorage, expectedData);
	}

	@Test
	public void readUserGUESTGoesToAlvinToCoraStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "user";
		expectedData.id = "coraUser:5368244264733286";
		expectedData.answer = alvinMixedRecordStorage.read(expectedData.type, expectedData.id);

		expectedData.calledMethod = "read";
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
	}

	@Test
	public void readListGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.filter = DataGroup.withNameInData("filter");
		expectedData.answer = alvinMixedRecordStorage.readList(expectedData.type,
				expectedData.filter).listOfDataGroups;

		expectedData.calledMethod = "readList";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void readPlaceListGoesToAlvinToCoraStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "place";
		expectedData.filter = DataGroup.withNameInData("filter");
		expectedData.answer = alvinMixedRecordStorage.readList(expectedData.type,
				expectedData.filter).listOfDataGroups;

		expectedData.calledMethod = "readList";
		assertNoInteractionWithStorage(basicStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
		assertExpectedDataSameAsInStorageSpy(alvinFeodraToCoraStorage, expectedData);
	}

	@Test
	public void createGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.id = "someId";
		expectedData.record = DataGroup.withNameInData("dummyRecord");
		expectedData.collectedTerms = DataGroup.withNameInData("collectedTerms");
		expectedData.linkList = DataGroup.withNameInData("linkList");
		expectedData.dataDivider = "someDataDivider";
		alvinMixedRecordStorage.create(expectedData.type, expectedData.id, expectedData.record,
				expectedData.collectedTerms, expectedData.linkList, expectedData.dataDivider);

		expectedData.calledMethod = "create";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void createPlaceGoesToAlvinToCoraStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "place";
		alvinMixedRecordStorage.create(expectedData.type, expectedData.id, expectedData.record,
				expectedData.collectedTerms, expectedData.linkList, expectedData.dataDivider);

		expectedData.calledMethod = "create";
		assertNoInteractionWithStorage(basicStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
		assertExpectedDataSameAsInStorageSpy(alvinFeodraToCoraStorage, expectedData);
	}

	@Test
	public void deleteByTypeAndIdGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.id = "someId";
		alvinMixedRecordStorage.deleteByTypeAndId(expectedData.type, expectedData.id);

		expectedData.calledMethod = "deleteByTypeAndId";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void linksExistForRecordGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.id = "someId";
		expectedData.answer = alvinMixedRecordStorage.linksExistForRecord(expectedData.type,
				expectedData.id);

		expectedData.calledMethod = "linksExistForRecord";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void updateGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.id = "someId";
		expectedData.record = DataGroup.withNameInData("dummyRecord");
		expectedData.collectedTerms = DataGroup.withNameInData("collectedTerms");
		expectedData.linkList = DataGroup.withNameInData("linkList");
		expectedData.dataDivider = "someDataDivider";
		alvinMixedRecordStorage.update(expectedData.type, expectedData.id, expectedData.record,
				expectedData.collectedTerms, expectedData.linkList, expectedData.dataDivider);

		expectedData.calledMethod = "update";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void updatePlaceGoesToFedoraStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "place";
		expectedData.id = "someId";
		expectedData.record = DataGroup.withNameInData("dummyRecord");
		expectedData.collectedTerms = DataGroup.withNameInData("collectedTerms");
		expectedData.linkList = DataGroup.withNameInData("linkList");
		expectedData.dataDivider = "someDataDivider";
		alvinMixedRecordStorage.update(expectedData.type, expectedData.id, expectedData.record,
				expectedData.collectedTerms, expectedData.linkList, expectedData.dataDivider);

		expectedData.calledMethod = "update";
		assertExpectedDataSameAsInStorageSpy(alvinFeodraToCoraStorage, expectedData);
		assertNoInteractionWithStorage(basicStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);

	}

	@Test
	public void readAbstractListGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.filter = DataGroup.withNameInData("filter");
		expectedData.answer = alvinMixedRecordStorage.readAbstractList(expectedData.type,
				expectedData.filter).listOfDataGroups;

		expectedData.calledMethod = "readAbstractList";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void readLinkListGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.id = "someId";
		expectedData.answer = alvinMixedRecordStorage.readLinkList(expectedData.type,
				expectedData.id);

		expectedData.calledMethod = "readLinkList";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void generateLinkCollectionPointingToRecordGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.id = "someId";
		expectedData.answer = alvinMixedRecordStorage
				.generateLinkCollectionPointingToRecord(expectedData.type, expectedData.id);

		expectedData.calledMethod = "generateLinkCollectionPointingToRecord";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void recordsExistForRecordTypeGoesToBasicStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.answer = alvinMixedRecordStorage.recordsExistForRecordType(expectedData.type);

		expectedData.calledMethod = "recordsExistForRecordType";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void recordExistsForAbstractOrImplementingRecordTypeAndRecordIdGoesToBasicStorage()
			throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "someType";
		expectedData.id = "someId";
		expectedData.answer = alvinMixedRecordStorage
				.recordExistsForAbstractOrImplementingRecordTypeAndRecordId(expectedData.type,
						expectedData.id);

		expectedData.calledMethod = "recordExistsForAbstractOrImplementingRecordTypeAndRecordId";
		assertExpectedDataSameAsInStorageSpy(basicStorage, expectedData);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertNoInteractionWithStorage(alvinDbToCoraStorage);
	}

	@Test
	public void readAbstractListForUserGoesToAlvinToCoraStorage() throws Exception {
		RecordStorageSpyData expectedData = new RecordStorageSpyData();
		expectedData.type = "user";
		expectedData.filter = DataGroup.withNameInData("filter");
		expectedData.answer = alvinMixedRecordStorage.readAbstractList(expectedData.type,
				expectedData.filter).listOfDataGroups;

		expectedData.calledMethod = "readAbstractList";
		assertNoInteractionWithStorage(basicStorage);
		assertNoInteractionWithStorage(alvinFeodraToCoraStorage);
		assertExpectedDataSameAsInStorageSpy(alvinDbToCoraStorage, expectedData);
	}
}
