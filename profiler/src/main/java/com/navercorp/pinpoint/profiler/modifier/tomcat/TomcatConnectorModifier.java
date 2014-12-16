package com.navercorp.pinpoint.profiler.modifier.tomcat;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.Agent;
import com.navercorp.pinpoint.bootstrap.instrument.ByteCodeInstrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.interceptor.Interceptor;
import com.navercorp.pinpoint.profiler.modifier.AbstractModifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tomcat connector 정보를 수집하기 위한 modifier
 *
 * @author netspider
 */
public class TomcatConnectorModifier extends AbstractModifier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TomcatConnectorModifier(ByteCodeInstrumentor byteCodeInstrumentor, Agent agent) {
        super(byteCodeInstrumentor, agent);
    }

    public String getTargetClass() {
        return "org/apache/catalina/connector/Connector";
    }

    public byte[] modify(ClassLoader classLoader, String javassistClassName, ProtectionDomain protectedDomain, byte[] classFileBuffer) {
        if (logger.isInfoEnabled()) {
            logger.info("Modifing. {}", javassistClassName);
        }
        
        try {
			// initialize()할 때 protocol과 port번호를 저장해둔다.
			Interceptor interceptor = byteCodeInstrumentor.newInterceptor(classLoader, protectedDomain,
                    "com.navercorp.pinpoint.profiler.modifier.tomcat.interceptor.ConnectorInitializeInterceptor", null, null);
			InstrumentClass connector = byteCodeInstrumentor.getClass(classLoader, javassistClassName, classFileBuffer);

			// Tomcat 6
			if (connector.hasDeclaredMethod("initialize", null)) {
				connector.addInterceptor("initialize", null, interceptor);
			}
			// Tomcat 7
			else if (connector.hasDeclaredMethod("initInternal", null)) {
				connector.addInterceptor("initInternal", null, interceptor);
			}

            if (this.logger.isInfoEnabled()) {
                this.logger.info("{} class is converted.", javassistClassName);
            }

            return connector.toBytecode();
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage(), e);
            }
            return null;
        }
    }
}