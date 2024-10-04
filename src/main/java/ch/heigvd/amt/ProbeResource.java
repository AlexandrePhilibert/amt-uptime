package ch.heigvd.amt;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.Template;

@Path("/")
public class ProbeResource {

    @Inject
    Template indexPage;

    @Inject
    Template registerPage;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return indexPage.instance();
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance register() {
        return registerPage.instance();
    }
}
