package com.fahkap.eoo.quiz.client

import org.springframework.context.annotation.Bean
import java.io.IOException

class OAuth2UserClientFeignConfiguration {

    @Bean(name = ["userFeignClientInterceptor"])
    @Throws(IOException::class)
    fun getUserFeignClientInterceptor() = UserFeignClientInterceptor()
}
