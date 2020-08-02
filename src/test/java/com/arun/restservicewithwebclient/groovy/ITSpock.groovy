package com.arun.restservicewithwebclient.groovy

import com.arun.restservicewithwebclient.component.WebclientForMockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * @author arun on 8/1/20
 */

@SpringBootTest
@Stepwise
class ITSpock extends Specification {

    @Autowired
    private WebclientForMockService webclientForMockService;

    def "Assert Bean Creation for Component"() {
        expect: "Bean created successfully"
        webclientForMockService != null
    }

    def "Check the status of MockService"() {
        expect: "UP"
        def status = webclientForMockService.healthOfMockService
        when: "Check for Health"
        then:
        status == "UP"
    }
}
