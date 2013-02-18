package org.jss.prototype.service;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class OPDDataServiceTest {

    @Autowired
    OPDDataService opdDataService;

    @Test
    public void testSetupData() throws Exception {
        opdDataService.setupData();
    }
}
