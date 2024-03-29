/*
 * Copyright 2018, 2019, 2020 Uppsala University Library
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
package se.uu.ub.cora.alvin.mixedstorage.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.alvin.mixedstorage.NotImplementedException;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.sqldatabase.RecordReader;
import se.uu.ub.cora.sqldatabase.RecordReaderFactory;
import se.uu.ub.cora.sqldatabase.SqlStorageException;
import se.uu.ub.cora.storage.RecordNotFoundException;
import se.uu.ub.cora.storage.RecordStorage;
import se.uu.ub.cora.storage.StorageReadResult;

public final class AlvinDbToCoraRecordStorage implements RecordStorage {

	private AlvinDbToCoraConverterFactory converterFactory;
	private RecordReaderFactory recordReaderFactory;

	private AlvinDbToCoraRecordStorage(RecordReaderFactory recordReaderFactory,
			AlvinDbToCoraConverterFactory converterFactory) {
		this.recordReaderFactory = recordReaderFactory;
		this.converterFactory = converterFactory;
	}

	public static AlvinDbToCoraRecordStorage usingRecordReaderFactoryAndConverterFactory(
			RecordReaderFactory recordReaderFactory,
			AlvinDbToCoraConverterFactory converterFactory) {
		return new AlvinDbToCoraRecordStorage(recordReaderFactory, converterFactory);
	}

	@Override
	public DataGroup read(String type, String id) {
		if ("user".equals(type)) {
			return readAndConvertUser(type, id);
		}
		throw NotImplementedException.withMessage("read is not implemented for type: " + type);
	}

	private DataGroup readAndConvertUser(String type, String id) {
		throwErrorIfIdNotAnIntegerValue(id);
		Map<String, Object> map = tryToReadUserFromDb(id);
		return convertOneMapFromDbToDataGroup(type, map);
	}

	private Map<String, Object> tryToReadUserFromDb(String id) {
		try {
			RecordReader recordReader = recordReaderFactory.factor();
			Map<String, Object> conditions = new HashMap<>();
			conditions.put("id", Integer.valueOf(id));
			return recordReader.readOneRowFromDbUsingTableAndConditions("alvin_seam_user",
					conditions);
		} catch (SqlStorageException e) {
			throw new RecordNotFoundException("User not found: " + id);
		}
	}

	private void throwErrorIfIdNotAnIntegerValue(String id) {
		try {
			Integer.valueOf(id);
		} catch (NumberFormatException ne) {
			throw new RecordNotFoundException("User not found: " + id);
		}
	}

	@Override
	public void create(String type, String id, DataGroup dataRecord, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		throw NotImplementedException.withMessage("create is not implemented");
	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		throw NotImplementedException.withMessage("deleteByTypeAndId is not implemented");
	}

	@Override
	public boolean linksExistForRecord(String type, String id) {
		throw NotImplementedException.withMessage("linksExistForRecord is not implemented");
	}

	@Override
	public void update(String type, String id, DataGroup dataRecord, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		throw NotImplementedException.withMessage("update is not implemented");
	}

	@Override
	public StorageReadResult readList(String type, DataGroup filter) {
		throw NotImplementedException.withMessage("readList is not implemented for type: " + type);
	}

	@Override
	public StorageReadResult readAbstractList(String type, DataGroup filter) {
		if ("user".contentEquals(type)) {
			return readAllUsersFromDbAndConvertToDataGroup(type);
		}
		throw NotImplementedException
				.withMessage("readAbstractList is not implemented for type " + type);
	}

	private StorageReadResult readAllUsersFromDbAndConvertToDataGroup(String type) {
		List<Map<String, Object>> readAllFromTable = readAllUsersFromDb();
		StorageReadResult storageReadResult = new StorageReadResult();
		storageReadResult.listOfDataGroups = convertDataAndAddToList(type, readAllFromTable);
		return storageReadResult;
	}

	private List<DataGroup> convertDataAndAddToList(String type,
			List<Map<String, Object>> readAllFromTable) {
		List<DataGroup> convertedList = new ArrayList<>(readAllFromTable.size());
		for (Map<String, Object> map : readAllFromTable) {
			DataGroup convertedUser = convertOneMapFromDbToDataGroup(type, map);
			convertedList.add(convertedUser);
		}
		return convertedList;
	}

	private DataGroup convertOneMapFromDbToDataGroup(String type, Map<String, Object> map) {
		AlvinDbToCoraConverter dbToCoraConverter = converterFactory.factor(type);
		return dbToCoraConverter.fromMap(map);
	}

	private List<Map<String, Object>> readAllUsersFromDb() {
		RecordReader recordReader = recordReaderFactory.factor();
		return recordReader.readAllFromTable("alvin_seam_user");
	}

	@Override
	public DataGroup readLinkList(String type, String id) {
		throw NotImplementedException.withMessage("readLinkList is not implemented");
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String type, String id) {
		throw NotImplementedException
				.withMessage("generateLinkCollectionPointingToRecord is not implemented");
	}

	@Override
	public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String type,
			String id) {
		if ("user".equals(type)) {
			return userExistsInDb(id);
		}
		throw NotImplementedException.withMessage(
				"recordExistsForAbstractOrImplementingRecordTypeAndRecordId is not implemented for "
						+ type);
	}

	private boolean userExistsInDb(String id) {
		try {
			throwErrorIfIdNotAnIntegerValue(id);
			tryToReadUserFromDb(id);
			return true;
		} catch (RecordNotFoundException e) {
			return false;
		}
	}

	public AlvinDbToCoraConverterFactory getConverterFactory() {
		// needed for test
		return converterFactory;
	}

	public RecordReaderFactory getRecordReaderFactory() {
		// needed for test
		return recordReaderFactory;
	}

	@Override
	public long getTotalNumberOfRecordsForType(String type, DataGroup filter) {
		throw NotImplementedException
				.withMessage("getTotalNumberOfRecordsForType is not implemented for " + type);
	}

	@Override
	public long getTotalNumberOfRecordsForAbstractType(String abstractType,
			List<String> implementingTypes, DataGroup filter) {
		throw NotImplementedException.withMessage(
				"getTotalNumberOfRecordsForAbstractType is not implemented for " + abstractType);
	}

}
