package rest;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;

public class Home extends Controller {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index();
    }

    public TemplateInstance index() {
        return Templates.index();
    }
}