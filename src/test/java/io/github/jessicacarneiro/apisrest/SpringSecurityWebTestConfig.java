package io.github.jessicacarneiro.apisrest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class SpringSecurityWebTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        List<SimpleGrantedAuthority> managerUserGrantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        User managerUser = new User("manager@company.com", "password1", managerUserGrantedAuthorities);

        List<SimpleGrantedAuthority> basicUserGrantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        User basicUser = new User("user@company.com", "password2", basicUserGrantedAuthorities);


        return new InMemoryUserDetailsManager(Arrays.asList(
                managerUser, basicUser
        ));
    }
}
