package se.uu.ub.cora.alvin.mixedstorage.fedora;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkSpy;
import se.uu.ub.cora.data.DataRecordLink;
import se.uu.ub.cora.data.DataRecordLinkFactory;

public class DataRecordLinkFactorySpy implements DataRecordLinkFactory {

	public List<String> usedNameInDatas = new ArrayList<>();

	@Override
	public DataRecordLink factorUsingNameInData(String nameInData) {
		usedNameInDatas.add(nameInData);
		return new DataRecordLinkSpy(nameInData);
	}

	@Override
	public DataRecordLink factorAsLinkWithNameInDataTypeAndId(String nameInData, String recordType,
			String recordId) {
		usedNameInDatas.add(nameInData);
		return new DataRecordLinkSpy(nameInData, recordType, recordId);
	}

}
