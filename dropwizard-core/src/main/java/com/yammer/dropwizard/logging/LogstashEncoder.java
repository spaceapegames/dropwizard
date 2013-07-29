package com.yammer.dropwizard.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Space Ape Games
 */
public class LogstashEncoder {

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

    private DateFormat df;
    private NumberFormat nf = new DecimalFormat("000000000");
    private List<LogstashParam> params;

    public LogstashEncoder(List<LogstashParam> params){
        this.params = params;
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.");

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public byte[] doEncode(ILoggingEvent event) throws IOException {

        ObjectNode eventNode = MAPPER.createObjectNode();
        long nanoseconds = System.nanoTime() % 100000000;
        String timestamp = df.format(new Date(event.getTimeStamp()))+nf.format(nanoseconds) + "Z";

        eventNode.put("@timestamp", timestamp);
        eventNode.put("@message", event.getFormattedMessage());
        eventNode.put("@fields", createFields(event));

        return MAPPER.writeValueAsBytes(eventNode);
    }

    private ObjectNode createFields(ILoggingEvent event) {

        ObjectNode fieldsNode = MAPPER.createObjectNode();
        fieldsNode.put("logger_name", event.getLoggerName());
        fieldsNode.put("thread_name", event.getThreadName());
        fieldsNode.put("level", event.getLevel().toString());
        fieldsNode.put("level_value", event.getLevel().toInt());
        fieldsNode.put("source", event.getLevel().toInt());

        for(LogstashParam param : params){
            fieldsNode.put(param.getName(),param.getValue());
        }

        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            String message = ThrowableProxyUtil.asString(throwableProxy);
            //message = message.replaceAll("\r","\\\\r");
            //message = message.replaceAll("\n","\\\\n");
            //message = message.replaceAll("\t","\\\\t");

            fieldsNode.put("stack_trace", message);
        }

        Map<String, String> mdc = event.getMDCPropertyMap();

        for (Map.Entry<String, String> entry : mdc.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            fieldsNode.put(key, value);
        }

        return fieldsNode;
    }

}
