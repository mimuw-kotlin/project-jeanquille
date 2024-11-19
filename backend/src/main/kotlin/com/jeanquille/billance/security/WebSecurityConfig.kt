package com.jeanquille.billance.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

        @Bean
        fun filterChain(http: HttpSecurity): SecurityFilterChain {
                http {
                        authorizeRequests {
                                authorize("/test", permitAll)
                                authorize("/test/gaming", authenticated)
                        }
                        formLogin {
                                disable()
                        }
                }
                return http.build()
        }
}