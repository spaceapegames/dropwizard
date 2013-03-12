package com.yammer.dropwizard.logging.tests;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.yammer.dropwizard.logging.LogstashAppender;
import org.junit.Test;

/**
 * Space Ape Games
 */
public class LogstashTest {

    public void canDeserializeAHostAndPort() throws Exception {
        LogstashAppender underTest = new LogstashAppender("logstash.apelabs.net",9999);
        underTest.start();
        LoggingEvent event = new LoggingEvent();
        event.setLevel(Level.INFO);
        event.setTimeStamp(System.currentTimeMillis());
        event.setLoggerName("thebruvsinthehouse");
        event.setThreadName("dog");
        event.setMessage("TEST TEST TEST 123");

        underTest.doAppend(event);
    }

}
