package org.jss.prototype.service;


import org.json.simple.JSONObject;
import org.jss.prototype.domain.Concept;
import org.jss.prototype.repository.AllConcepts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConceptService {
    @Autowired
    private AllConcepts allConcepts;

    public ConceptService(){}

    public ConceptService(AllConcepts concepts) {
        this.allConcepts = concepts;
    }

    @Transactional
    public void createOrUpdateConcept(String name, String json, String type) {
        allConcepts.createOrUpdate(name, json, type);

    }

    @Transactional
    public List<JSONObject> findConcept(String name, String type) {
       return allConcepts.getConceptJsonsByNameAndCategory(name, type);

    }

    public void delete(String name, String category) {
        Concept concept = (Concept) allConcepts.findByNameAndCategory(name, category).get(0);
        allConcepts.delete(concept);
    }
}
