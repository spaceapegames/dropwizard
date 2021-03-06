package com.yammer.dropwizard.logging.tests;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.yammer.dropwizard.logging.LogstashAppender;
import com.yammer.dropwizard.logging.LogstashParam;
import org.junit.Test;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Space Ape Games
 */
public class LogstashTest {

    @Test
    public void canDeserializeAHostAndPort() throws Exception {
        List<LogstashParam> params = new ArrayList<LogstashParam>();
        params.add(new LogstashParam("host","peterpan"));
        LogstashAppender underTest = new LogstashAppender("logstash.apelabs.net",9999,params);
        underTest.start();
        LoggingEvent event = new LoggingEvent();
        event.setLevel(Level.INFO);
        event.setTimeStamp(System.currentTimeMillis());
        event.setLoggerName("thebruvsinthehouse");
        event.setThreadName("dog");
        event.setMessage("TEST TEST TEST 123");
        event.setThrowableProxy(new ch.qos.logback.classic.spi.ThrowableProxy(new IllegalArgumentException("Hello")));
        underTest.doAppend(event);



    }

}
