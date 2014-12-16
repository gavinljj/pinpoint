package com.navercorp.pinpoint.web.view;

import com.navercorp.pinpoint.common.ServiceType;
import com.navercorp.pinpoint.common.bo.AgentInfoBo;
import com.navercorp.pinpoint.web.applicationmap.ServerBuilder;
import com.navercorp.pinpoint.web.applicationmap.ServerInstanceList;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

/**
 * @author emeroad
 */
public class ServerInstanceListSerializerTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerialize() throws Exception {
        AgentInfoBo.Builder agentInfoBuilder = new AgentInfoBo.Builder();
        agentInfoBuilder.agentId("agentId");
        agentInfoBuilder.serviceType(ServiceType.TOMCAT);
        agentInfoBuilder.hostName("testcomputer");

        AgentInfoBo agentInfoBo = agentInfoBuilder.build();

        HashSet<AgentInfoBo> set = new HashSet<AgentInfoBo>();
        set.add(agentInfoBo);

        ServerBuilder builder = new ServerBuilder(null);
        builder.addAgentInfo(set);
        ServerInstanceList serverInstanceList = builder.build();
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(serverInstanceList);
        logger.debug(json);
    }
}