package com.navercorp.pinpoint.web.alarm.checker;

import com.navercorp.pinpoint.web.alarm.collector.MapStatisticsCallerDataCollector;
import com.navercorp.pinpoint.web.alarm.collector.MapStatisticsCallerDataCollector.DataCategory;
import com.navercorp.pinpoint.web.alarm.vo.Rule;

/**
 * @author minwoo.jung
 */
public class SlowCountToCalleChecker extends AlarmChecker {
    
    public SlowCountToCalleChecker(MapStatisticsCallerDataCollector dataCollector, Rule rule) {
        super(rule, "", dataCollector);
    }
    
    @Override
    protected long getDetectedValue() {
        String calleName = rule.getNotes();
        return ((MapStatisticsCallerDataCollector)dataCollector).getCount(calleName, DataCategory.SLOW_COUNT);
    }
    
    @Override
    public String getEmailMessage() {
        return String.format("%s value is %s%s during the past 5 mins.(Threshold : %s%s) %s For From $s To $s.<br>", rule.getCheckerName(), getDetectedValue(), unit, rule.getThreshold(), unit, rule.getCheckerName(), rule.getApplicationId(), rule.getNotes());
    };

}