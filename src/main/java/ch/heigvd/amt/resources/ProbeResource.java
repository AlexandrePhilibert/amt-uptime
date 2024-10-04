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

    @Inject
    Template statusPage;

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

    @GET
    @Path("/probes/{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance status(@PathParam("id") Integer id) {
        var probe = probeService.findProbeById(id);
        var statusList = probeService.getProbeStatusList(probe);

        return statusPage.instance()
                .data("probe", probe)
                .data("status", statusList.getFirst())
                .data("statusList", statusList);

    }
}
