package com.recargapay.wallet_service.core.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;

@Configuration
public class SwaggerConfiguration {

    private static Contact getContact() {
        var contact = new Contact();
        contact.setEmail("support@recargapay.com");

        contact.setName("RecargaPay");

        contact.setUrl("https://recargapay.com.br/");

        return contact;
    }

    private static Server getServer() {
        var server = new Server();

        server.setUrl("http://localhost:8080/");

        server.setDescription("Localhost");
        return server;
    }

    private static Info getInfo(Contact contact) {
        return new Info()
                .title("Wallet Service")
                .contact(contact)
                .version("1.0")
                .description("Wallet Service");
    }

    @Bean
    public OpenAPI openAPI() {

        var server = getServer();
        var contact = getContact();
        var info = getInfo(contact);

        return new OpenAPI()
                .info(info)
                .servers(singletonList(server));
    }

}