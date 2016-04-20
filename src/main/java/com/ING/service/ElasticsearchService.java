package com.ING.service;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

public interface ElasticsearchService {
    public void inputData() throws IOException, ParseException;
    public List<Map> searchData(String query) throws UnknownHostException;
}
