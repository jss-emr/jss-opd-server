package org.jss.prototype.repository;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jss.prototype.domain.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

@Repository
@Transactional
public class AllConcepts {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private DataAccessTemplate template;

    public void createOrUpdate(String name, String json, String category) {
        createOrUpdate(new Concept(name, json, category));
    }

    public void create(Concept concept) {
        template.saveOrUpdate(concept);
    }

    public void createOrUpdate(Concept concept) {
        List<Concept> existingConcepts = this.findByNameAndCategory(concept.getName(), concept.getCategory());
        if (existingConcepts.isEmpty()) {
            template.saveOrUpdate(concept);
        } else {
            Concept existingConcept = existingConcepts.get(0);
            existingConcept.setJson(concept.getJson());
            template.saveOrUpdate(existingConcept);
        }
    }

    @Transactional
    public List findByName(String name) {
        String q = "select concept.json from Concept concept where concept.name LIKE '%" + name + "%'";

        return template.find(q);
    }

    @Transactional
    public List<Concept> findByNameAndCategory(String name, String category) {
        Session session = template.getSessionFactory().getCurrentSession();
        Query query = session.createQuery("from Concept where category = :category and name = :name");
        query.setParameter("category", category);
        query.setParameter("name", name);

        return query.list();
    }

    public
    @Value("${limitValue}")
    int limitValue;
    public
    @Value("${searchByAlphaLength}")
    int searchByAlphaLength;

    @Transactional
    public List<JSONObject> getConceptJsonsByNameAndCategory(String name, String category) {
        String q = "";
        long startTime = System.currentTimeMillis();
        template.setMaxResults(limitValue);
        List jsonList;



        q = "select concept.json from Concept concept where concept.category=? and concept.name LIKE '" + name + "%'";
        jsonList = template.find(q, category);
        q =  "select concept.json from Concept concept where concept.category=? and concept.name NOT LIKE '" + name + "%' and " + containsQueryCondition(name);
        jsonList.addAll(template.find(q, category));
        logger.info("Time spent for query " + (System.currentTimeMillis() - startTime));
        return parseJson(jsonList);
    }

    private List<String> splitIntoWords(String name) {
        List<String> result = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(name, " ", false);
        while (stringTokenizer.hasMoreElements())
            result.add(stringTokenizer.nextToken());
        return result;
    }

    private String containsQueryCondition(String key) {
        List<String> queryParts = new ArrayList<>();
        List<String> keywords = splitIntoWords(key);
        for (String keyword : keywords) {
            queryParts.add("concept.name like '%" + keyword + "%'");
        }
        return StringUtils.collectionToDelimitedString(queryParts, " and ");
    }

    private List<JSONObject> parseJson(List jsonList) {
        Iterator itr = jsonList.iterator();
        JSONParser parser = new JSONParser();
        List<JSONObject> conceptList = new ArrayList<JSONObject>();
        JSONObject conceptJson = null;
        while (itr.hasNext()) {
            String conceptStr = (String) itr.next();
            try {
                conceptJson = (JSONObject) parser.parse(conceptStr);
                conceptList.add(conceptJson);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        return conceptList;
    }

    public void delete(Concept concept) {
        template.delete(concept);
        template.flush();
    }

}
