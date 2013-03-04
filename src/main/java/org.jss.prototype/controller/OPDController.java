package org.jss.prototype.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.jss.prototype.service.ConceptService;
import org.jss.prototype.service.OPDDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class OPDController {


    private static Logger logger =Logger.getLogger("OPDController") ;
    private ConceptService conceptService;
    private OPDDataService opdDataService;

    @Autowired
    public OPDController(ConceptService conceptService,OPDDataService opdDataService){
        this.conceptService = conceptService;
        this.opdDataService = opdDataService;
    }

    @RequestMapping(value = "/concept/create", method = RequestMethod.POST,headers="Accept=application/json")
    public String createConcept(@RequestParam String name, @RequestParam String json,@RequestParam String type) throws IOException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        conceptService.createConcept(name,json,type);

        return "redirect:/";
    }

    @RequestMapping(value="/concept", method=RequestMethod.GET,headers="Accept=application/json")
    public @ResponseBody
    List<JSONObject> findConcepts(@RequestParam String name, @RequestParam String category) {
        long startTime = System.currentTimeMillis();
        List<JSONObject> concept = conceptService.findConcept(name, category);
        logger.info("Controller:Returning concepts.Time taken: "+ (System.currentTimeMillis()-startTime));
        return concept;
    }

    @RequestMapping(value="/data/setup", method=RequestMethod.GET,headers="Accept=application/json")
    public @ResponseBody ResponseEntity<String> setup() {
        HttpHeaders responseHeaders = new HttpHeaders();
        opdDataService.setupData();
        return new ResponseEntity<String>(responseHeaders,HttpStatus.OK)  ;

    }




}


