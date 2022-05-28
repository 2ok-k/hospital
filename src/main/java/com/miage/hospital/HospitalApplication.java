package com.miage.hospital;

import com.miage.hospital.entities.Patient;
import com.miage.hospital.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }

    //@Bean //En ignorant l'annotation  précédente la méthode qui suit ne sera donc exécutée
    CommandLineRunner commandLineRunner(PatientRepository patientRepository){
        return args -> {
            patientRepository.save(new Patient(null,"Shanks",new Date(),false,112));
            patientRepository.save(new Patient(null,"TraffagarLaw",new Date(),true,116));
            patientRepository.save(new Patient(null,"Dofi",new Date(),false,138));
            patientRepository.save(new Patient(null,"Luffy",new Date(),true,120));

            patientRepository.findAll().forEach(patient -> {
                System.out.println(patient.getNom());
            });
        };
    }

}
