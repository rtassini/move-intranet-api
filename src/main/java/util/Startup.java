package util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class Startup {

    public void start(@Observes StartupEvent evt) {
    }
}