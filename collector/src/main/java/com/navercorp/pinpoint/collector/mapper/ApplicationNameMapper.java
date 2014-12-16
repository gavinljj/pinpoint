package com.navercorp.pinpoint.collector.mapper;

import com.navercorp.pinpoint.common.util.BytesUtils;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class ApplicationNameMapper implements RowMapper<String> {
	@Override
	public String mapRow(Result result, int rowNum) throws Exception {
        if (result.isEmpty()) {
            return null;
        }
		KeyValue[] raw = result.raw();

		if (raw.length == 0) {
			return null;
		}

		String[] ret = new String[raw.length];
		int index = 0;

		for (KeyValue kv : raw) {
			ret[index++] = BytesUtils.toString(kv.getQualifier());
		}

		return ret[0];
	}
}