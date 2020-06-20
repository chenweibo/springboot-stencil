package com.chen.stencil;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.chen.stencil.mapper")
public class StencilApplication {

    public static void main(String[] args) {
        SpringApplication.run(StencilApplication.class, args);
    }

    @Bean
    public ServletWebServerFactory tomcatEmbedded() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            // connector other settings...
            // configure maxSwallowSize
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                // -1 means unlimited, accept bytes
                ((AbstractHttp11Protocol<?>)
                        connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return tomcat;
    }

}
