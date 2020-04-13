package com.fahkap.eoo.quiz.cucumber

import com.fahkap.eoo.quiz.EooquizApp
import io.cucumber.java.Before
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = [EooquizApp::class])
class CucumberContextConfiguration {

    @Before
    fun setup_cucumber_spring_context() {
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
    }
}
