package com.navercorp.pinpoint.web.view;

import com.navercorp.pinpoint.web.applicationmap.ServerInstance;
import com.navercorp.pinpoint.web.applicationmap.ServerInstanceList;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author emeroad
 * @author minwoo.jung
 */
public class ServerInstanceListSerializer extends JsonSerializer<ServerInstanceList> {

    @Override
    public void serialize(ServerInstanceList serverInstanceList, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        Map<String,List<ServerInstance>> map = serverInstanceList.getServerInstanceList();
        for (Map.Entry<String, List<ServerInstance>> entry : map.entrySet()) {
            jgen.writeFieldName(entry.getKey());
            jgen.writeStartObject();

            jgen.writeStringField("name", entry.getKey());
            jgen.writeStringField("status", null);

            
            Map<String, String> linkInfo = serverInstanceList.getLink(entry.getKey());
            jgen.writeStringField("linkName", linkInfo.get("linkName"));
            jgen.writeStringField("linkURL", linkInfo.get("linkURL"));
            
            jgen.writeFieldName("instanceList");
            writeInstanceList(jgen, entry.getValue());

            jgen.writeEndObject();
        }


        jgen.writeEndObject();

    }

    private void writeInstanceList(JsonGenerator jgen, List<ServerInstance> serverList) throws IOException {
        jgen.writeStartObject();
        for (ServerInstance serverInstance : serverList) {
            jgen.writeFieldName(serverInstance.getName());
            jgen.writeObject(serverInstance);
        }

        jgen.writeEndObject();
    }


}