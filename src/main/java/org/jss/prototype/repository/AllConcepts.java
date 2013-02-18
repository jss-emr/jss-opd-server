package org.jss.prototype.repository;

import org.jss.prototype.domain.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AllConcepts {
    @Autowired
    private DataAccessTemplate template;

    public void create(String name, String json, String category) {
        create(new Concept(name,json,category));
    }

    public void create(Concept concept) {
        template.saveOrUpdate(concept);
    }

    public List findByName(String name) {
        String q  = "select concept.json from Concept concept where concept.name LIKE '%"+name+"%' ";

        return template.find(q) ;
    }

    public List findByNameAndCategory(String name, String category) {
        String q  = "select concept.json from Concept concept where concept.category=? and concept.name LIKE '%"+name+"%' ";
        return template.find(q,category);
    }

    public void delete(Concept concept){
        template.delete(concept);
        template.flush();
    }

}
