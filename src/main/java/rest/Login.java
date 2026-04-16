package rest;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;

public class Login extends Controller {

    @Inject
    SecurityIdentity identity;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index();
    }

    public TemplateInstance index() {
        if (!identity.isAnonymous()) {
            redirect(Home.class).index();
            return null;
        }
        return Templates.index();
    }
}