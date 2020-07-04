package com.fahkap.eoo.quiz.client

import java.io.IOException
import org.springframework.context.annotation.Bean

class OAuth2UserClientFeignConfiguration {

    @Bean(name = ["userFeignClientInterceptor"])
    @Throws(IOException::class)
    fun getUserFeignClientInterceptor() = UserFeignClientInterceptor()
}
