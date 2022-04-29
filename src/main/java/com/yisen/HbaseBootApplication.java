package com.yisen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@EntityScan(basePackages = "com.yisen.hbaseboot.entity")

public class HbaseBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(HbaseBootApplication.class, args);
    }
//@Bean
//    public ServletWebServerFactory servletContainer(){
//    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
//        @Override
//        protected void postProcessContext(Context context){
//            SecurityConstraint securityConstraint = new SecurityConstraint();
//            securityConstraint.setUserConstraint("CONFIDENTIAL");
//            SecurityCollection securityCollection = new SecurityCollection();
//            securityCollection.addPattern("/*");
//            securityConstraint.addCollection(securityCollection);
//            context.addConstraint(securityConstraint);
//        }
//    };
//    tomcat.addAdditionalTomcatConnectors(redirectConnector());
//    return tomcat;
//}
//    private Connector redirectConnector(){
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8080);
//        connector.setSecure(false);
//        connector.setRedirectPort(443);
//        return connector;
//    }
}

