package ch.heigvd.amt.services;

import ch.heigvd.amt.entities.Probe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

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
}
