package com.ING.serviceIMP;

import com.ING.configuration.ElasticsearchClient;
import com.ING.model.StudyElement;
import com.ING.service.ElasticsearchService;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
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

    private QueryBuilder[] makeQuery(String query){
        QueryBuilder queryBuilder[] = { QueryBuilders.matchPhrasePrefixQuery("keyword", query).maxExpansions(10),
                QueryBuilders.wildcardQuery("keyword", query), QueryBuilders.simpleQueryStringQuery(query),
                QueryBuilders.fuzzyQuery("keyword", query).fuzziness(Fuzziness.AUTO) };
        return queryBuilder;
    }

    @Override
    public void inputData() throws IOException, ParseException {
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        JSONParser jsonParser = new JSONParser();

        elasticsearchTemplate.deleteIndex(StudyElement.class);
        elasticsearchTemplate.createIndex(StudyElement.class);
        elasticsearchTemplate.refresh(StudyElement.class);

        Object object = jsonParser.parse(new FileReader("C:/dev/LastProject/ING/src/main/webapp/resources/please.json"));
        JSONObject jsonObject = (JSONObject)object;
        JSONArray jsonArray = (JSONArray) jsonObject.get("bindings");
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
    public String searchData(String query) throws UnknownHostException {
       // QueryBuilder queryBuilder[]= makeQuery(query);
        QueryBuilder reformQuery =  QueryBuilders.matchPhrasePrefixQuery("keyword",query);

        SearchResponse searchResponse = ElasticsearchClient.shareClient().prepareSearch("study")
                .setQuery(reformQuery).execute().actionGet();
        //.setFetchSource(new String[]{"unit","grade"},null)
        ElasticsearchClient.shareClient().close();

        List<StudyElement> units = new ArrayList<StudyElement>();

        for(SearchHit hit : searchResponse.getHits()){
            Map<String, Object> map = hit.getSource();
            String unit = map.get("unit").toString();
            String grade = map.get("grade").toString();
            String schoolClass = map.get("schoolclass").toString();
            String postUnit = map.get("postunit").toString();
            String preUnit = map.get("preunit").toString();
            StudyElement studyElement = new StudyElement(unit,grade,schoolClass,postUnit,preUnit,null);
            units.add(studyElement);
        }
        return new Gson().toJson(units);
    }


}
