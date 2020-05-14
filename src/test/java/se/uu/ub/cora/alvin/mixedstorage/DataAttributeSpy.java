package se.uu.ub.cora.alvin.mixedstorage;

import se.uu.ub.cora.data.DataAttribute;

public class DataAttributeSpy implements DataAttribute {

	public String nameInData;
	public String value;

	public DataAttributeSpy(String id, String value) {
		this.nameInData = id;
		this.value = value;
	}

	@Override
	public String getNameInData() {
		return nameInData;
	}

	@Override
	public String getValue() {
		return value;
	}

}
