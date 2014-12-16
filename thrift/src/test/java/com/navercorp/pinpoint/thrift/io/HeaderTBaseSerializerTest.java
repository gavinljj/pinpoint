package com.navercorp.pinpoint.thrift.io;

import com.navercorp.pinpoint.thrift.dto.TAgentInfo;
import com.navercorp.pinpoint.thrift.io.Header;
import com.navercorp.pinpoint.thrift.io.HeaderTBaseDeserializer;
import com.navercorp.pinpoint.thrift.io.HeaderTBaseDeserializerFactory;
import com.navercorp.pinpoint.thrift.io.HeaderTBaseSerializer;
import com.navercorp.pinpoint.thrift.io.HeaderTBaseSerializerFactory;

import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author emeroad
 */
public class HeaderTBaseSerializerTest {
    private final Logger logger = LoggerFactory.getLogger(HeaderTBaseSerializerTest.class.getName());


    @Test
    public void testSerialize1() throws Exception {
    	HeaderTBaseSerializer serializer = new HeaderTBaseSerializerFactory(false).createSerializer();
    	HeaderTBaseDeserializer deserializer = new HeaderTBaseDeserializerFactory().createDeserializer();
    	
    	test(serializer, deserializer);
    }
    
    @Test
    public void testSerialize2() throws Exception {
    	HeaderTBaseSerializer serializer = new HeaderTBaseSerializerFactory().createSerializer();
    	HeaderTBaseDeserializer deserializer = new HeaderTBaseDeserializerFactory().createDeserializer();
    	
    	test(serializer, deserializer);
    }
    
    private void test(HeaderTBaseSerializer serializer, HeaderTBaseDeserializer deserializer) throws TException {

        Header header = new Header();
        // 10 을 JVMInfoThriftDTO type
        header.setType((short) 10);

        TAgentInfo tAgentInfo = new TAgentInfo();
        tAgentInfo.setAgentId("agentId");
        tAgentInfo.setHostname("host");
        tAgentInfo.setApplicationName("applicationName");

        byte[] serialize = serializer.serialize(tAgentInfo);
        dump(serialize);

        TAgentInfo deserialize = (TAgentInfo) deserializer.deserialize(serialize);
        logger.debug("deserializer:{}", deserialize.getClass());

        Assert.assertEquals(deserialize, tAgentInfo);
    }

    public void dump(byte[] data) {
        String s = Arrays.toString(data);
        logger.debug("size:{} data:{}", data.length, s);
    }
}