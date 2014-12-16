package com.navercorp.pinpoint.web.service;

import com.navercorp.pinpoint.common.bo.SpanBo;
import com.navercorp.pinpoint.web.vo.*;
import com.navercorp.pinpoint.web.vo.scatter.ApplicationScatterScanResult;
import com.navercorp.pinpoint.web.vo.scatter.Dot;
import com.navercorp.pinpoint.web.vo.scatter.ScatterScanResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author emeroad
 */
public class DotExtractor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Range range;

    private Map<Application, List<Dot>> dotMap = new HashMap<Application, List<Dot>>();

    public DotExtractor(Range range) {
        if (range == null) {
            throw new NullPointerException("range must not be null");
        }
        this.range = range;
    }

    public void addDot(SpanBo span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }

        Application spanApplication = new Application(span.getApplicationId(), span.getServiceType());
        final List<Dot> dotList = getDotList(spanApplication);

        final TransactionId transactionId = new TransactionId(span.getTraceAgentId(), span.getTraceAgentStartTime(), span.getTraceTransactionSequence());
        final Dot dot = new Dot(transactionId, span.getCollectorAcceptTime(), span.getElapsed(), span.getErrCode(), span.getAgentId());
        dotList.add(dot);
        logger.trace("Application:{} Dot:{}", spanApplication, dot);
    }

    private List<Dot> getDotList(Application spanApplication) {
        List<Dot> dotList = this.dotMap.get(spanApplication);
        if(dotList == null) {
            dotList = new ArrayList<Dot>();
            this.dotMap.put(spanApplication, dotList);
        }
        return dotList;
    }

    public List<ApplicationScatterScanResult> getApplicationScatterScanResult() {
        List<ApplicationScatterScanResult> applicationScatterScanResult = new ArrayList<ApplicationScatterScanResult>();
        for (Map.Entry<Application, List<Dot>> entry : this.dotMap.entrySet()) {
            List<Dot> dotList = entry.getValue();
            Application application = entry.getKey();
            ScatterScanResult scatterScanResult = new ScatterScanResult(range.getFrom(), range.getTo(), dotList);
            applicationScatterScanResult.add(new ApplicationScatterScanResult(application, scatterScanResult));
        }
        return applicationScatterScanResult;
    }
}