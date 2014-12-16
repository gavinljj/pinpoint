package com.navercorp.pinpoint.rpc.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.List;

import org.jboss.netty.channel.ChannelFuture;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navercorp.pinpoint.rpc.Future;
import com.navercorp.pinpoint.rpc.PinpointSocketException;
import com.navercorp.pinpoint.rpc.RecordedStreamChannelMessageListener;
import com.navercorp.pinpoint.rpc.RequestResponseServerMessageListener;
import com.navercorp.pinpoint.rpc.ResponseMessage;
import com.navercorp.pinpoint.rpc.StreamCreateResponse;
import com.navercorp.pinpoint.rpc.TestByteUtils;
import com.navercorp.pinpoint.rpc.client.PinpointSocket;
import com.navercorp.pinpoint.rpc.client.PinpointSocketFactory;
import com.navercorp.pinpoint.rpc.server.PinpointServerSocket;
import com.navercorp.pinpoint.rpc.server.TestSeverMessageListener;


/**
 * @author emeroad
 */
public class PinpointSocketFactoryTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void connectFail() {
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        try {
            pinpointSocketFactory.connect("127.0.0.1", 10234);
            Assert.fail();
        } catch (PinpointSocketException e) {
            Assert.assertTrue(ConnectException.class.isInstance(e.getCause()));
        } finally {
            pinpointSocketFactory.release();
        }
    }

    @Test
    public void reconnectFail() throws InterruptedException {
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        try {
            // api 호출시 error 메시지 간략화 확인용.
            InetSocketAddress remoteAddress = new InetSocketAddress("127.0.0.1", 10234);
            ChannelFuture reconnect = pinpointSocketFactory.reconnect(remoteAddress);
            reconnect.await();
            Assert.assertFalse(reconnect.isSuccess());
            Assert.assertTrue(ConnectException.class.isInstance(reconnect.getCause()));
        } finally {
            pinpointSocketFactory.release();
        }
        Thread.sleep(1000);
    }


    @Test
    public void connect() throws IOException, InterruptedException {
        PinpointServerSocket ss = new PinpointServerSocket();
//        ss.setPipelineFactory(new DiscardPipelineFactory());
        ss.bind("127.0.0.1", 10234);
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        try {
            PinpointSocket socket = pinpointSocketFactory.connect("127.0.0.1", 10234);

            socket.close();
        } finally {
            pinpointSocketFactory.release();
            ss.close();
        }

    }

    @Test
    public void pingInternal() throws IOException, InterruptedException {
        PinpointServerSocket ss = new PinpointServerSocket();
        ss.bind("127.0.0.1", 10234);
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        pinpointSocketFactory.setPingDelay(100);

        try {
            PinpointSocket socket = pinpointSocketFactory.connect("127.0.0.1", 10234);
            Thread.sleep(1000);
            socket.close();
        } finally {
            pinpointSocketFactory.release();
            ss.close();
        }

    }

    @Test
    public void ping() throws IOException, InterruptedException {
        PinpointServerSocket ss = new PinpointServerSocket();
        ss.bind("127.0.0.1", 10234);
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        pinpointSocketFactory.setPingDelay(100);

        try {
            PinpointSocket socket = pinpointSocketFactory.connect("127.0.0.1", 10234);
            socket.sendPing();
            socket.close();
        } finally {
            pinpointSocketFactory.release();
            ss.close();
        }

    }

    @Test
    public void pingAndRequestResponse() throws IOException, InterruptedException {
        PinpointServerSocket ss = new PinpointServerSocket();
        ss.setMessageListener(new RequestResponseServerMessageListener());
        ss.bind("127.0.0.1", 10234);
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        pinpointSocketFactory.setPingDelay(100);

        try {
            PinpointSocket socket = pinpointSocketFactory.connect("127.0.0.1", 10234);
            byte[] randomByte = TestByteUtils.createRandomByte(10);
            Future<ResponseMessage> response = socket.request(randomByte);
            Thread.sleep(1000);
            response.await();
            ResponseMessage result = response.getResult();
            Assert.assertArrayEquals(randomByte, result.getMessage());
            socket.close();
        } finally {
            pinpointSocketFactory.release();
            ss.close();
        }
    }

    @Test
    public void sendSync() throws IOException, InterruptedException {
        PinpointServerSocket ss = new PinpointServerSocket();
//        ss.setPipelineFactory(new DiscardPipelineFactory());
        ss.setMessageListener(new TestSeverMessageListener());
        ss.bind("localhost", 10234);
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        try {
            PinpointSocket socket = pinpointSocketFactory.connect("127.0.0.1", 10234);
            logger.info("send1");
            socket.send(new byte[20]);
            logger.info("send2");
            socket.sendSync(new byte[20]);

            socket.close();
        } finally {
            pinpointSocketFactory.release();
            ss.close();
        }

    }

    @Test
    public void requestAndResponse() throws IOException, InterruptedException {
        PinpointServerSocket ss = new PinpointServerSocket();
//        ss.setPipelineFactory(new DiscardPipelineFactory());
        ss.setMessageListener(new TestSeverMessageListener());
        ss.bind("localhost", 10234);
        PinpointSocketFactory pinpointSocketFactory = new PinpointSocketFactory();
        try {
            PinpointSocket socket = pinpointSocketFactory.connect("127.0.0.1", 10234);

            byte[] bytes = TestByteUtils.createRandomByte(20);
            Future<ResponseMessage> request = socket.request(bytes);
            request.await();
            ResponseMessage message = request.getResult();
            Assert.assertArrayEquals(message.getMessage(), bytes);

            socket.close();
        } finally {
            pinpointSocketFactory.release();
            ss.close();
        }

    }

    @Test
    public void connectTimeout() {
        PinpointSocketFactory pinpointSocketFactory = null;
        try {
            int timeout = 1000;
            pinpointSocketFactory = new PinpointSocketFactory();
            pinpointSocketFactory.setConnectTimeout(timeout);
            int connectTimeout = pinpointSocketFactory.getConnectTimeout();
            Assert.assertEquals(timeout, connectTimeout);
        } finally {
            pinpointSocketFactory.release();
        }


    }
}