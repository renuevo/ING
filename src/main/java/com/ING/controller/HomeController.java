package com.ING.controller;

import com.ING.service.ElasticsearchService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by renuevo on 2016-04-13.
 */
@Controller
public class HomeController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @RequestMapping(value="/")
    public ModelAndView home()
    {
        ModelAndView model = new ModelAndView("home");

        return model;
    }

    @RequestMapping(value="/create")
    public ModelAndView createIndex() throws IOException, ParseException {
        ModelAndView model = new ModelAndView("home");
        elasticsearchService.inputData();
        return model;
    }

    @RequestMapping(value="/search",method = RequestMethod.GET)
    public void search(HttpServletRequest request, HttpServletResponse response) throws UnknownHostException {
        String query = request.getParameter("query");
        String resultJson = elasticsearchService.searchData(query);
        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(resultJson);
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }




}
