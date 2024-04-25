
package org.thuandq.spring.starter.config;


import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return Optional.empty();
        String name = ""; // todo get userName from Authen
        return Optional.of(!Strings.isBlank(name) ? name : "GUEST");
    }
}

