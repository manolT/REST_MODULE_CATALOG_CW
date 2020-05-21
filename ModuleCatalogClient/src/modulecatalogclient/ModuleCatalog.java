/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulecatalogclient;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:ModuleCatalog [/service]<br>
 * USAGE:
 * <pre>
 *        ModuleCatalog client = new ModuleCatalog();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Manol
 */
public class ModuleCatalog {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ModuleCatalog/webresources";

    public ModuleCatalog() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("service");
    }

    public String setActiveStatusOfModule(Object requestEntity, String subject, String level, String name) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("setActive/{0}/{1}/{2}", new Object[]{subject, level, name})).request(javax.ws.rs.core.MediaType.TEXT_PLAIN).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.TEXT_PLAIN), String.class);
    }

    public String createModule(String subject, String level, String code, String name) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("create/{0}/{1}/{2}/{3}", new Object[]{subject, level, code, name})).request().post(null, String.class);
    }

    public String list(String level, String subject, String isActive) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (level != null) {
            resource = resource.queryParam("level", level);
        }
        if (subject != null) {
            resource = resource.queryParam("subject", subject);
        }
        if (isActive != null) {
            resource = resource.queryParam("isActive", isActive);
        }
        resource = resource.path("list");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public String deleteModule(String subject, String level, String name) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("delete/{0}/{1}/{2}", new Object[]{subject, level, name})).request().delete(String.class);
    }

    public void close() {
        client.close();
    }
    
}
