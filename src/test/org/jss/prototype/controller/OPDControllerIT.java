package org.jss.prototype.controller;

import org.jss.prototype.service.ConceptService;
import org.jss.prototype.util.MVCTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class OPDControllerIT {

    @Autowired
    OPDController controller;
    @Autowired
    ConceptService conceptService;

    @Test
    public void shouldReturnConceptsFilteredByNameAndCategory() throws Exception {

        //conceptService.createConcept("paracetamol","{name: \"Paracetamol (SYP)\", type: \"SYP\", specs: [\"60ml\"]}","Drug");

        MVCTestUtils.mockMvc(controller)
                .perform(post("/opd/concepts")
                        .param("name", "para")
                        .param("category", "Drug")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(content().type(MediaType.APPLICATION_JSON));

        //conceptService.delete("paracetamol","Drug");


    }
}
