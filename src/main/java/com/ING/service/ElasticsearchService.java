package com.ING.service;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.UnknownHostException;

public interface ElasticsearchService {
    public void inputData() throws IOException, ParseException;
    public String searchData(String query) throws UnknownHostException;
}
