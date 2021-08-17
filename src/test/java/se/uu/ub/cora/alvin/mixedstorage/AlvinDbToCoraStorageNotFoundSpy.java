package se.uu.ub.cora.alvin.mixedstorage;

import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.storage.RecordNotFoundException;
import se.uu.ub.cora.storage.RecordStorage;
import se.uu.ub.cora.storage.StorageReadResult;

public class AlvinDbToCoraStorageNotFoundSpy implements RecordStorage {
	public RecordStorageSpyData data = new RecordStorageSpyData();
	public boolean readWasCalled = false;

	@Override
	public DataGroup read(String type, String id) {
		readWasCalled = true;
		throw new RecordNotFoundException("User not found: " + id);
	}

	@Override
	public void create(String type, String id, DataGroup dataRecord, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean linksExistForRecord(String type, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup dataRecord, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public StorageReadResult readList(String type, DataGroup filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageReadResult readAbstractList(String type, DataGroup filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGroup readLinkList(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public boolean recordsExistForRecordType(String type) {
	// // TODO Auto-generated method stub
	// return false;
	// }

	@Override
	public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String type,
			String id) {
		data.type = type;
		data.id = id;
		data.calledMethod = "recordExistsForAbstractOrImplementingRecordTypeAndRecordId";
		data.answer = false;
		return false;
	}

	@Override
	public long getTotalNumberOfRecordsForAbstractType(String arg0, List<String> arg1,
			DataGroup arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalNumberOfRecordsForType(String arg0, DataGroup arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

}
