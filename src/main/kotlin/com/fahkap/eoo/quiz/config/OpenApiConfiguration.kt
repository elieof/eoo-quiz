package com.fahkap.eoo.quiz.config

import com.google.common.base.Predicates
import io.github.jhipster.config.JHipsterConstants
import io.github.jhipster.config.JHipsterProperties
import io.github.jhipster.config.apidoc.customizer.SwaggerCustomizer
import java.nio.ByteBuffer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_SWAGGER)
class OpenApiConfiguration {

    @Bean
    fun noApiFirstCustomizer() = SwaggerCustomizer { docket -> docket.select()
            .apis(Predicates.not(RequestHandlerSelectors.basePackage("com.fahkap.eoo.quiz.web.api")))
    }

    @Bean
    fun apiFirstDocket(jHipsterProperties: JHipsterProperties): Docket {
        val properties = jHipsterProperties.swagger
        val contact = Contact(
            properties.contactName,
            properties.contactUrl,
            properties.contactEmail
        )

        val apiInfo = ApiInfo(
            "API First " + properties.title,
            properties.description,
            properties.version,
            properties.termsOfServiceUrl,
            contact,
            properties.license,
            properties.licenseUrl,
            mutableListOf()
        )

        return Docket(DocumentationType.SWAGGER_2)
            .groupName("openapi")
            .host(properties.host)
            .protocols(properties.protocols.toSet())
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(properties.isUseDefaultResponseMessages)
            .forCodeGeneration(true)
            .directModelSubstitute(ByteBuffer::class.java, String::class.java)
            .genericModelSubstitutes(ResponseEntity::class.java)
            .ignoredParameterTypes(Pageable::class.java)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.fahkap.eoo.quiz.web.api"))
            .paths(regex(properties.defaultIncludePattern))
            .build()
    }
}
