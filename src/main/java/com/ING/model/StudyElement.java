package com.ING.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by renuevo on 2016-04-13.
 */
@Document(indexName="study")
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class StudyElement {
    private String unit;
    private String grade;
    private String schoolclass;
    private String postunit;
    private String preunit;
    private String keyword;
}
