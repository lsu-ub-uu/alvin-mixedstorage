package se.uu.ub.cora.alvin.mixedstorage.user;

import se.uu.ub.cora.data.converter.DataToJsonConverterFactory;
import se.uu.ub.cora.data.converter.DataToJsonConverterFactoryCreator;

public class DataToJsonConverterFactoryCreatorSpy implements DataToJsonConverterFactoryCreator {

	@Override
	public DataToJsonConverterFactory createFactory() {
		// TODO Auto-generated method stub
		return new DataToJsonConverterFactorySpy();
	}

}
