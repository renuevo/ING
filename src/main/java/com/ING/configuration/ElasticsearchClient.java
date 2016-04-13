package com.ING.configuration;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by renuevo on 2016-04-13.
 */
public class ElasticsearchClient {
   public static Client shareClient() throws UnknownHostException {
        // transport client
        //Settings settings = Settings.settingsBuilder().put("cluster.name", "elasticsearch").build();

        Client client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        return client;

    }
}
