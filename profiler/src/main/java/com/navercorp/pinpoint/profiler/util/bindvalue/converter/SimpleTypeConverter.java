package com.navercorp.pinpoint.profiler.util.bindvalue.converter;

import com.navercorp.pinpoint.bootstrap.util.StringUtils;

/**
 * @author emeroad
 */
public class SimpleTypeConverter implements Converter {
    @Override
    public String convert(Object[] args) {
        if (args == null) {
            return "null";
        }
        if (args.length == 2) {
            return StringUtils.drop(StringUtils.toString(args[1]));
        } else if (args.length == 3) {
            // TODO 3일때 추가 처리?
            return StringUtils.drop(StringUtils.toString(args[1]));
        }
        return "error";
    }
}