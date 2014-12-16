package com.navercorp.pinpoint.web.applicationmap;

import com.navercorp.pinpoint.common.ServiceType;
import com.navercorp.pinpoint.common.bo.AgentInfoBo;
import com.navercorp.pinpoint.web.applicationmap.link.MatcherGroup;
import com.navercorp.pinpoint.web.applicationmap.rawdata.AgentHistogram;
import com.navercorp.pinpoint.web.applicationmap.rawdata.AgentHistogramList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author emeroad
 * @author minwoo.jung
 */
public class ServerBuilder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AgentHistogramList agentHistogramList;
    private final Set<AgentInfoBo> agentSet;
    private MatcherGroup matcherGroup = null;

    public ServerBuilder(MatcherGroup matcherGroup) {
        this.agentHistogramList = new AgentHistogramList();
        this.agentSet = new HashSet<AgentInfoBo>();
        this.matcherGroup = matcherGroup;
    }

    public void addCallHistogramList(AgentHistogramList agentHistogramList) {
        if (agentHistogramList == null) {
            return;
        }
        this.agentHistogramList.addAgentHistogram(agentHistogramList);
    }

    public void addAgentInfo(Set<AgentInfoBo> agentInfoBo) {
        if (agentInfoBo == null) {
            return;
        }
        this.agentSet.addAll(agentInfoBo);
    }

    public void addServerInstance(ServerBuilder copy) {
        if (copy == null) {
            throw new NullPointerException("copy must not be null");
        }
        addCallHistogramList(copy.agentHistogramList);
        addAgentInfo(copy.agentSet);
    }



    private String getHostName(String instanceName) {
        final int pos = instanceName.indexOf(':');
        if (pos > 0) {
            return instanceName.substring(0, pos);
        } else {
            return instanceName;
        }
    }

    /**
     * 어플리케이션에 속한 물리서버와 서버 인스턴스 정보를 채운다.
     *
     * @param hostHistogram
     */
    public ServerInstanceList buildLogicalServer(final AgentHistogramList hostHistogram) {
        ServerInstanceList serverInstanceList = new ServerInstanceList();
        for (AgentHistogram agentHistogram : hostHistogram.getAgentHistogramList()) {
            final String instanceName = agentHistogram.getId();
            final String hostName = getHostName(agentHistogram.getId());
            final ServiceType serviceType = agentHistogram.getServiceType();

            final ServerInstance serverInstance = new ServerInstance(hostName, instanceName, serviceType);
            serverInstanceList.addServerInstance(serverInstance);
        }
        return serverInstanceList;
    }

    public ServerInstanceList buildPhysicalServer(final Set<AgentInfoBo> agentSet) {
        final ServerInstanceList serverInstanceList = new ServerInstanceList(matcherGroup);
        for (AgentInfoBo agent : agentSet) {
            final ServerInstance serverInstance = new ServerInstance(agent);
            serverInstanceList.addServerInstance(serverInstance);

        }
        return serverInstanceList;
    }



    public ServerInstanceList build() {
        if (!agentSet.isEmpty()) {
            // agent이름이 존재할 경우. 실제 리얼 서버가 존재할 경우
            this.logger.debug("buildPhysicalServer:{}", agentSet);
            return buildPhysicalServer(agentSet);
        } else {
            // 논리 이름으로 구성.
            this.logger.debug("buildLogicalServer");
            return buildLogicalServer(agentHistogramList);
        }
    }


}