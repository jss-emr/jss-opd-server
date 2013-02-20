package org.jss.prototype.repository;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jss.prototype.domain.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional
public class AllConcepts {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private DataAccessTemplate template;

    public void create(String name, String json, String category) {
        create(new Concept(name,json,category));
    }

    public void create(Concept concept) {
        template.saveOrUpdate(concept);
    }

    @Transactional
    public List findByName(String name) {
        String q  = "select concept.json from Concept concept where concept.name LIKE '%"+name+"%'";

        return template.find(q) ;
    }

    @Transactional
    public List<Concept> findByNameAndCategory(String name, String category) {
        String q  = "select concept.json from Concept concept where concept.category=? and concept.name LIKE '%"+name+"%'";
        List jsonList=template.find(q,category);

        return jsonList;
    }
    public @Value("${limitValue}") int limitValue;

    @Transactional
    public List<JSONObject> getConceptJsonsByNameAndCategory(String name, String category) {
        String q="";


        if(name.length()<=3){
            q  = "select concept.json from Concept concept where concept.category=? and concept.name LIKE '"+name+"%'";
        }
        else{
        q  = "select concept.json from Concept concept where concept.category=? and concept.name LIKE '%"+name+"%'";
        }
        long startTime = System.currentTimeMillis();
        template.setMaxResults(limitValue);
        List jsonList=template.find(q,category);
        logger.info("Time spent for query " + (System.currentTimeMillis() - startTime));

        return parseJson(jsonList);
    }

    private List<JSONObject> parseJson(List jsonList) {
        Iterator itr = jsonList.iterator();
        JSONParser parser = new JSONParser();
        List<JSONObject> conceptList = new ArrayList<JSONObject>();
        JSONObject conceptJson =null;
        while(itr.hasNext()){
            String conceptStr = (String) itr.next();
            try {
                conceptJson = (JSONObject)parser.parse(conceptStr);
                conceptList.add(conceptJson);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        return conceptList;
    }

    public void delete(Concept concept){
        template.delete(concept);
        template.flush();
    }

}
