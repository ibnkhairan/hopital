package fr.ael.hopital;

import fr.ael.hopital.entities.Patient;
import fr.ael.hopital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Date;
import java.util.logging.Logger;

@SpringBootApplication
public class HopitalApplication implements CommandLineRunner {

    @Autowired
     PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(HopitalApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Patient patient = new Patient();
        patient.setId(null);
        patient.setNom("Mohamed");
        patient.setDateNaissance(new Date());
        patient.setMalade(false);
        patient.setScore(123);

        Patient patient2 = new Patient(null,"Yassine",new Date(),false,123);

        Patient patient3 = Patient.builder()
                .nom("Imane")
                .dateNaissance(new Date())
                .score(256)
                .malade(true)
                .build();
        patientRepository.save(patient);
        patientRepository.save(patient2);
        patientRepository.save(patient3);

    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

   //1234
   @Bean
    CommandLineRunner commandLineRunnerJdbcUsers(JdbcUserDetailsManager jdbcUserDetailsManager){
        PasswordEncoder passwordEncoder= passwordEncoder();
        return args -> {
                jdbcUserDetailsManager.createUser(
                        User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build()
                );
            jdbcUserDetailsManager.createUser(
                    User.withUsername("user2").password(passwordEncoder.encode("1234")).roles("USER").build()
            );
            jdbcUserDetailsManager.createUser(
                    User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()
            );
        };
    }

}
