package org.jss.prototype.repository;


import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.jss.prototype.IntegrationTest;
import org.jss.prototype.domain.Concept;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AllConceptsTest extends IntegrationTest {
    @Autowired
    AllConcepts allConcepts;

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @Test
    public void tearDown() throws Exception {
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {
        Concept concept=new Concept("paracetamol","{name: \"Paracetamol (SYP)\", type: \"SYP\", specs: [\"60ml\"]}","Drug");
        allConcepts.create(concept);

        Assert.notEmpty(allConcepts.findByName("paracetamol"));

        allConcepts.delete(concept);
    }

    @Test
    @Transactional
    public void shouldReturnConceptForSearchByDrugAndName() throws Exception {
        Concept concept=new Concept("paracetamol","{name: \"Paracetamol (SYP)\", type: \"SYP\", specs: [\"60ml\"]}","Drug");
        allConcepts.create(concept);

        Assert.notEmpty(allConcepts.findByNameAndCategory("paracetamol", "Drug"));

        allConcepts.delete(concept);
    }

    @Test
    @Transactional
    public void shouldNotReturnConceptForSearchByWrongCategory() throws Exception {
        Concept concept=new Concept("paracetamol","{name: \"Paracetamol (SYP)\", type: \"SYP\", specs: [\"60ml\"]}","Drug");
        allConcepts.create(concept);

        Assert.isTrue(allConcepts.findByNameAndCategory("paracetamol", "Drug123").size() == 0);
        allConcepts.delete(concept);
    }

    @Test
    @Transactional
    public void shouldSearchMultipleWordsIrrespectiveOfInputOrder() throws IOException {
        Concept concept1=new Concept("duration of illness","{\"name\":\"duration of illness\"}", "Concept");
        Concept concept2=new Concept("illness duration","{\"name\":\"illness duration\"}", "Concept");
        Concept concept3=new Concept("Fever duration","{\"name\":\"Fever duration\"}", "Concept");
        Concept concept4=new Concept("duration of illness in childhood","{\"name\":\"duration of illness in childhood\"}", "Concept");
        Concept concept5=new Concept("on illness duration in adulthood","{\"name\":\"on illness duration in adulthood\"}", "Concept");
        allConcepts.create(concept1);
        allConcepts.create(concept2);
        allConcepts.create(concept3);
        allConcepts.create(concept4);
        allConcepts.create(concept5);

        List<JSONObject> jsonList = allConcepts.getConceptJsonsByNameAndCategory("illness   duration", "Concept");
        List<DummyConcept> concepts = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (JSONObject jsonObject : jsonList) {
            concepts.add(objectMapper.readValue(jsonObject.toString(), DummyConcept.class));
        }

        assertEquals(4, jsonList.size());
        assertTrue(concepts.contains(new DummyConcept(concept1.getName())));
        assertTrue(concepts.contains(new DummyConcept(concept2.getName())));
        assertTrue(concepts.contains(new DummyConcept(concept4.getName())));
        assertTrue(concepts.contains(new DummyConcept(concept5.getName())));
        assertFalse(concepts.contains(new DummyConcept(concept3.getName())));
    }


    public static class DummyConcept {
        public DummyConcept() {
        }

        public DummyConcept(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DummyConcept that = (DummyConcept) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}
