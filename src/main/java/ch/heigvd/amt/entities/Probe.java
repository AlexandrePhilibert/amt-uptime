package ch.heigvd.amt.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"url"})
        }
)
public class Probe {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @URL
    private String url;

    public Probe() {

    }

    public Probe(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}