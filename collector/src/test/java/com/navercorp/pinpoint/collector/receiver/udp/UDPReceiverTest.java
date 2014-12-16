package com.navercorp.pinpoint.collector.receiver.udp;

import java.io.IOException;
import java.net.*;

import com.navercorp.pinpoint.collector.receiver.DataReceiver;
import com.navercorp.pinpoint.collector.receiver.DispatchHandler;
import com.navercorp.pinpoint.collector.receiver.udp.BaseUDPReceiver;

import junit.framework.Assert;

import org.apache.thrift.TBase;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author emeroad
 */
public class UDPReceiverTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    @Ignore
	public void startStop() {
		try {
			DataReceiver receiver = new BaseUDPReceiver("test", new DispatchHandler() {
                @Override
                public void dispatchSendMessage(TBase<?, ?> tBase, byte[] packet, int offset, int length) {
                }

				@Override
				public TBase dispatchRequestMessage(TBase<?, ?> tBase, byte[] packet, int offset, int length) {
					// TODO Auto-generated method stub
					return null;
				}
				
            }, "127.0.0.1", 10999, 1024, 1, 10);
//			receiver.start();
//            start 타이밍을 spring안으로 변경하였음.
            // start시점을 좀더 정확히 알수 있어야 될거 같음.
            // start한 다음에 바로 셧다운하니. receive thread에서 localaddress를 제대로 못찾는 문제가 있음.
//            Thread.sleep(1000);

//			receiver.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

    @Test
    public void hostNullCheck() {
        InetSocketAddress address = new InetSocketAddress((InetAddress) null, 90);
        logger.debug(address.toString());
    }

    @Test
    public void socketBufferSize() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket();
        int receiveBufferSize = datagramSocket.getReceiveBufferSize();
        logger.debug("{}", receiveBufferSize);

        datagramSocket.setReceiveBufferSize(64*1024*10);
        logger.debug("{}", datagramSocket.getReceiveBufferSize());

        datagramSocket.close();
    }

    @Test
    public void sendSocketBufferSize() throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[0], 0, 0);

        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.connect(new InetSocketAddress("127.0.0.1", 9995));

        datagramSocket.send(datagramPacket);
        datagramSocket.close();
    }
}