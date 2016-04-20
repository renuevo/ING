package com.ING.serviceIMP;

import com.ING.configuration.ElasticsearchClient;
import com.ING.model.StudyElement;
import com.ING.service.ElasticsearchService;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ElasticsearchServiceIMP implements ElasticsearchService{

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;


    @Override
    public void inputData() throws IOException, ParseException {
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        JSONParser jsonParser = new JSONParser();

        elasticsearchTemplate.deleteIndex(StudyElement.class);
        elasticsearchTemplate.createIndex(StudyElement.class);
        elasticsearchTemplate.refresh(StudyElement.class);

        String filePath = new ClassPathResource("./json/middle _math.json").getFile().getAbsolutePath();
        Object object = jsonParser.parse(new FileReader(filePath));
        JSONObject jsonObject = (JSONObject)object;
        JSONArray jsonArray = (JSONArray) jsonObject.get("content");
        for(Object data : jsonArray){
            JSONObject JsonData = (JSONObject) data;
            String unit = JsonData.get("unit").toString();
            String grade =  JsonData.get("grade").toString();
            String schoolClass = JsonData.get("class").toString();
            String postUnit = JsonData.get("postunit").toString();
            String preUnit =  JsonData.get("preunit").toString();
            String keyword = JsonData.get("keyword").toString();

            StudyElement studyElement = new StudyElement(unit,grade,schoolClass,postUnit,preUnit,keyword);
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(studyElement.getUnit())
                    .withObject(studyElement)
                    .build();

            indexQueries.add(indexQuery);
        }
        elasticsearchTemplate.refresh(StudyElement.class);
        elasticsearchTemplate.bulkIndex(indexQueries);

    }

    @Override
    public List<Map> searchData(String query) throws UnknownHostException {
        QueryBuilder reformQuery =  QueryBuilders.matchPhrasePrefixQuery("keyword",query);
        SearchResponse searchResponse = ElasticsearchClient.shareClient().prepareSearch("study")
                .setQuery(reformQuery).execute().actionGet();
        ElasticsearchClient.shareClient().close();
        List<Map> units = new ArrayList<Map>();
        for(SearchHit hit : searchResponse.getHits()){
            Map<String, Object> map = hit.getSource();
            units.add(map);
        }
        return units;
    }


}
