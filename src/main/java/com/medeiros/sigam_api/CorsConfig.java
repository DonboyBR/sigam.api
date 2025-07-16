package com.medeiros.sigam_api; // No seu pacote base, com.medeiros.sigam_api

import org.springframework.context.annotation.Configuration; // Anotação para classes de configuração
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Para configurar o CORS
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Interface para configurar o Spring MVC

@Configuration // Anotação: Indica que esta é uma classe de configuração do Spring
public class CorsConfig implements WebMvcConfigurer { // Implementa WebMvcConfigurer para configurar o CORS

    @Override // Sobrescreve o método addCorsMappings da interface WebMvcConfigurer
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 👈 ATENÇÃO AQUI! Permite CORS para TODOS os endpoints da sua API (/**)
                .allowedOrigins("http://localhost:4200", // Permite a porta padrão do Angular
                        "http://localhost:4201", // Permite outras portas comuns de dev
                        "http://localhost:4202",
                        "http://localhost:4203",
                        "http://localhost:4204",
                        "http://localhost:4205",
                        "http://localhost:8080", // Se o front-end rodar na mesma porta do back-end (raro, mas possível)
                        "http://localhost:56235") // A porta que você estava usando
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite os métodos HTTP comuns
                .allowedHeaders("*") // Permite todos os cabeçalhos na requisição
                .allowCredentials(true); // Permite o envio de credenciais (cookies, headers de autenticação)
    }
}