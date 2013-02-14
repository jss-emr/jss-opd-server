package org.jss.prototype.service;


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

    public void createConcept(String name, String json, String type) {
        allConcepts.create(name,json,type);

    }

    public List findConcept(String name, String type) {
       return allConcepts.findByNameAndCategory(name, type);

    }

    public void delete(String name, String category) {
        Concept concept = (Concept) allConcepts.findByNameAndCategory(name, category).get(0);
        allConcepts.delete(concept);
    }
}
