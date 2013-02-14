package org.jss.prototype.repository;


import org.jss.prototype.IntegrationTest;
import org.jss.prototype.domain.Concept;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
}
