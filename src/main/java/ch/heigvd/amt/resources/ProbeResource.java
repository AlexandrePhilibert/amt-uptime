package ch.heigvd.amt.resources;

import ch.heigvd.amt.services.ProbeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.Template;


@Path("/")
public class ProbeResource {

    @Inject
    ProbeService probeService;

    @Inject
    Template indexPage;

    @Inject
    Template registerPage;

    @Inject
    Template probesPage;

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

    @GET
    @Path("/probes")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance probes() {
        var probeList = probeService.listProbes();
        return probesPage.instance().data("probeList", probeList);
    }

    @POST
    @Path("/probes")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance registerProbe(@FormParam("url") String url) {
        var probe = probeService.getOrCreateProbe(url);
        return probesPage.instance().data("probeList", probeService.listProbes());
    }
}
