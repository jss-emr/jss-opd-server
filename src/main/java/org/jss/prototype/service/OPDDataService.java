package org.jss.prototype.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jss.prototype.domain.Concept;
import org.jss.prototype.repository.AllConcepts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

@Service
@Transactional
public class OPDDataService {

    @Autowired
    private AllConcepts allConcepts;

    public OPDDataService() {
    }

    public void setupData() {
        JSONParser parser = new JSONParser();
        setupDiagnoses(parser);
    }

    private void setupDiagnoses(JSONParser parser) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream(
                            "/json/allDiagnoses.json")));
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray)obj;
            Iterator<JSONObject> iterator = jsonArray.iterator();
            Concept concept;
            while (iterator.hasNext()) {
                JSONObject jsonObject = iterator.next();
                String name = (String) jsonObject.get("name");
                concept = new Concept(name,jsonObject.toJSONString(),"Diagnosis");
                allConcepts.create(concept);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
