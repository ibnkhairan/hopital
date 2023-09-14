package fr.ael.hopital.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;



    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

    }

    public DataSource h2DataSource(){
        return  new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("hopital-db")
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        return  jdbcUserDetailsManager;
    }

    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        String pw = passwordEncoder.encode("1234");
        System.out.println(pw);
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
                User.withUsername("user1").password(pw).roles("USER").build(),// password("{noop}1234") ==> save le password en clair
                User.withUsername("admin").password(passwordEncoder.encode("3333")).roles("USER","ADMIN").build(),
                User.withUsername("user2").password(passwordEncoder.encode("4444")).roles("USER").build()
        );
        return  inMemoryUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
      httpSecurity.formLogin();

        httpSecurity
                .authorizeHttpRequests((authorize) -> authorize
               // .requestMatchers(antMatcher("/admin/**")).hasRole("ADMIN")
              //  .requestMatchers(antMatcher("/user/**")).hasRole("USER")
                .anyRequest().authenticated()
                );

        httpSecurity.exceptionHandling((exception)-> exception.accessDeniedPage("/notAuthorized"));
        return httpSecurity.build();
    }
}
