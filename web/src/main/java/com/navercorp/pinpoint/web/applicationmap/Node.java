package com.navercorp.pinpoint.web.applicationmap;

import java.io.IOException;

import com.navercorp.pinpoint.common.ServiceType;
import com.navercorp.pinpoint.web.applicationmap.histogram.NodeHistogram;
import com.navercorp.pinpoint.web.view.NodeSerializer;
import com.navercorp.pinpoint.web.vo.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * node map에서 application을 나타낸다.
 * 
 * @author netspider
 * @author emeroad
 */
@JsonSerialize(using = NodeSerializer.class)
public class Node {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String NODE_DELIMITER = "^";

    private final Application application;

    private ServerInstanceList serverInstanceList;

    private NodeHistogram nodeHistogram;
    // 임시로 생성.
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public Node(Application application) {
        if (application == null) {
            throw new NullPointerException("application must not be null");
        }
        this.application = application;
    }

    public Node(Node copyNode) {
        if (copyNode == null) {
            throw new NullPointerException("copyNode must not be null");
        }
        this.application = copyNode.application;
    }

    public String getApplicationTextName() {
        if (application.getServiceType().isUser()) {
            return "USER";
        } else {
            return application.getName();
        }
    }


    public void setServerInstanceList(ServerInstanceList serverInstanceList) {
        this.serverInstanceList = serverInstanceList;
    }

    public ServerInstanceList getServerInstanceList() {
		return serverInstanceList;
	}

    @JsonIgnore
    public String getNodeJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public String getJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("\"applicationName\" : \"").append(getApplicationTextName()).append("\",");
        sb.append("\"serviceType\" : \"").append(application.getServiceType()).append("\",");
        sb.append("\"serviceTypeCode\" : \"").append(application.getServiceTypeCode()).append("\"");
        sb.append(" }");
        return sb.toString();
    }



    public Application getApplication() {
        return application;
    }


	public String getNodeName() {
		return application.getName() + NODE_DELIMITER + application.getServiceType();
	}

	public ServiceType getServiceType() {
		return application.getServiceType();
	}

    public NodeHistogram getNodeHistogram() {
        return nodeHistogram;
    }

    public void setNodeHistogram(NodeHistogram nodeHistogram) {
        this.nodeHistogram = nodeHistogram;
    }

	@Override
	public String toString() {
		return "Node [" + application + "]";
	}
}