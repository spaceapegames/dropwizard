package com.yammer.dropwizard.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

/**
 * Space Ape Games
 */
public class LogstashAppender extends AppenderBase<ILoggingEvent> {

    private LogstashEncoder encoder;
    private String hostName;
    private int port;

    public LogstashAppender(String hostName, int port, List<LogstashParam> params){
        this.hostName = hostName;
        this.port = port;
        this.encoder = new LogstashEncoder(params);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(hostName);
            byte[] sendData = encoder.doEncode(eventObject);

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
            clientSocket.close();

        }catch(Exception e){
            addWarn("Failed to send message to logstash",e);
        }
    }
}
