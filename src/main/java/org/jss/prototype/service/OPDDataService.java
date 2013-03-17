package org.jss.prototype.service;

import org.apache.log4j.Logger;
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
public class OPDDataService {
    private static Logger logger =Logger.getLogger("OPDDataService") ;

    @Autowired
    private AllConcepts allConcepts;

    @Autowired
    public OPDDataService(AllConcepts allConcepts) {
        this.allConcepts = allConcepts;
    }

    public void setupData() {
        JSONParser parser = new JSONParser();
        setupDbData(parser,"/json/allDiagnoses.json","Diagnosis");
        setupDbData(parser,"/json/medicineDetails.json","Medicine");
        setupDbData(parser,"/json/allInstructions.json","Instruction");
        setupDbData(parser,"/json/allConcepts.json","Concept");
        setupDbData(parser,"/json/allExaminations.json","Examination");
    }
    @Transactional
    private void setupDbData(JSONParser parser,String jsonFileName,String category) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream(
                            jsonFileName)));
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray)obj;
            Iterator<JSONObject> iterator = jsonArray.iterator();
            Concept concept;
            while (iterator.hasNext()) {
                JSONObject jsonObject = iterator.next();
                String name = (String) jsonObject.get("name");

                if (name == null || name.trim().length() == 0) {
                    logger.warn(String.format("No name found for category:%s %s", category, jsonObject.toJSONString()));
                    continue;

                }
                concept = new Concept(name,jsonObject.toJSONString(),category);

                allConcepts.create(concept);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
