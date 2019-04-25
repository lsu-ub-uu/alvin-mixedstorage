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
package se.uu.ub.cora.alvin.mixedstorage.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.sqldatabase.DataReader;

public class DataReaderSpy implements DataReader {

	public boolean executePreparedStatementWasCalled = false;
	public String sqlSentToReader;
	public List<Object> valuesSentToReader = new ArrayList<>();

	@Override
	public List<Map<String, Object>> executePreparedStatementQueryUsingSqlAndValues(String sql,
			List<Object> values) {
		executePreparedStatementWasCalled = true;
		sqlSentToReader = sql;
		valuesSentToReader.addAll(values);

		List<Map<String, Object>> listOfRows = new ArrayList<>();
		Map<String, Object> row1 = new HashMap<>();
		row1.put("id", 52);
		// row1.put("lastupdated", '2014-04-17 10:12:52.87');
		row1.put("domain", "'uu");
		row1.put("email", "");
		row1.put("firstname", "SomeFirstName");
		row1.put("lastname", "SomeLastName");
		row1.put("userId", "user52");
		row1.put("groupid", 54);

		listOfRows.add(row1);

		return listOfRows;
	}

	@Override
	public Map<String, Object> readOneRowOrFailUsingSqlAndValues(String sql, List<Object> values) {
		return null;
	}

}
