package ch.heigvd.amt.services;

import ch.heigvd.amt.entities.Probe;
import ch.heigvd.amt.entities.Status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class ProbeService {
    @Inject
    EntityManager entityManger;

    @Transactional
    public List<Probe> listProbes() {
        return entityManger.createQuery("SELECT p from Probe p", Probe.class).getResultList();
    }

    @Transactional
    public Probe getOrCreateProbe(String url) {
        List<Probe> probes = entityManger.createQuery("SELECT p FROM Probe p WHERE p.url = :url", Probe.class)
                .setParameter("url", url)
                .getResultList();

        if (probes.isEmpty()) {
            Probe probe = new Probe();
            probe.setUrl(url);
            entityManger.persist(probe);
            return probe;
        }

        return probes.getFirst();
    }

    public void executeProbe(String url) {
        Instant start = Instant.now();

        try (var client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).connectTimeout(Duration.ofSeconds(2)).build()) {
            var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .header("User-Agent", "uptime/0.0.1")
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Expires", "0")
                    .build();

            Instant end = Instant.now();
            long duration = Duration.between(start, end).toMillis();

            var response = client.send(request, HttpResponse.BodyHandlers.discarding()).statusCode();

            Probe probe = getOrCreateProbe(url);
            Status status = new Status(probe, start, response, (int) duration);

            entityManger.persist(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Probe findProbeById(Integer id) {
        return entityManger.find(Probe.class, id);
    }

    public List<Status> getProbeStatusList(Probe probe) {
        return entityManger.createQuery("SELECT s FROM Status s WHERE s.probe = :probe ORDER BY s.timestamp DESC", Status.class)
                .setParameter("probe", probe)
                .getResultList();
    }


}
