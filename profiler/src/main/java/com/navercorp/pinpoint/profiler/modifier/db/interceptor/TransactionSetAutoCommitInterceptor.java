package com.navercorp.pinpoint.profiler.modifier.db.interceptor;

import com.navercorp.pinpoint.bootstrap.context.DatabaseInfo;
import com.navercorp.pinpoint.bootstrap.context.RecordableTrace;
import com.navercorp.pinpoint.bootstrap.interceptor.*;
import com.navercorp.pinpoint.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValueUtils;

/**
 * @author emeroad
 */
public class TransactionSetAutoCommitInterceptor extends SpanEventSimpleAroundInterceptor {


    public TransactionSetAutoCommitInterceptor() {
        super(TransactionSetAutoCommitInterceptor.class);
    }


    @Override
    public void doInBeforeTrace(RecordableTrace trace, Object target, Object[] args) {
        trace.markBeforeTime();
    }

    @Override
    protected void doInAfterTrace(RecordableTrace trace, Object target, Object[] args, Object result, Throwable throwable) {
        DatabaseInfo databaseInfo = DatabaseInfoTraceValueUtils.__getTraceDatabaseInfo(target, UnKnownDatabaseInfo.INSTANCE);

        trace.recordServiceType(databaseInfo.getType());
        trace.recordEndPoint(databaseInfo.getMultipleHost());
        trace.recordDestinationId(databaseInfo.getDatabaseId());


        trace.recordApi(getMethodDescriptor(), args);
        trace.recordException(throwable);

        trace.markAfterTime();
    }

}