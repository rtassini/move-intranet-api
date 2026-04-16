package util;

import io.quarkus.arc.Arc;
import io.quarkus.qute.TemplateGlobal;
import io.quarkus.security.identity.SecurityIdentity;

public class TemplateGlobals {

    /**
     * Disponibiliza o e-mail do usuário autenticado em todos os templates Qute.
     * Retorna null quando o usuário não está autenticado.
     */
    @TemplateGlobal
    public static String currentUserEmail() {
        SecurityIdentity identity = Arc.container().select(SecurityIdentity.class).get();
        if (identity == null || identity.isAnonymous()) {
            return null;
        }
        return identity.getPrincipal().getName();
    }
}