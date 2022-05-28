package com.miage.hospital.web;

import com.miage.hospital.entities.Patient;
import com.miage.hospital.repositories.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Controller
public class PatientController {

    private PatientRepository patientRepository;


    @GetMapping( "/") //La page par défaut
    public String home(){
        return "home";
    }

    @GetMapping("/user/index")
    public String patients(Model model,
                           @RequestParam(name = "page",defaultValue = "0") int page,
                           @RequestParam(name = "size",defaultValue = "4") int size,
                           @RequestParam(name = "keyword",defaultValue = "") String keyword){
        //List<Patient> patients = patientRepository.findAll();
        //Page<Patient> pagePatients = patientRepository.findAll(PageRequest.of(page,size));
        Page<Patient> pagePatients = patientRepository.findByNomContains(keyword,PageRequest.of(page,size));//J'appelle findByContains au lieu de findAll à cause de la méthode de recherche
        //model.addAttribute("listPatients",patients);
        model.addAttribute("listPatients",pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()]);//Faire la pagination
        model.addAttribute("currentPage",page);//Stockage de la page courante
        model.addAttribute("keyword",keyword);
        return "patients";
    }

    //Supprimer
    @GetMapping("/admin/delete")
    public String delete(Long id,String keyword,int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    //Formulaire patient
    @GetMapping("/admin/formPatients")
    public String formPatients(Model model) {
        model.addAttribute("patient",new Patient());
        return "formPatient";
    }

    //Sauvegarder patient
    @PostMapping("/admin/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page){ //valid : si les données sont valide | s'il y a des erreurs bindingResult prend le relais
        if (bindingResult.hasErrors()) {
            return "formPatient";
        }
        patientRepository.save(patient);
        return "redirect:/user/index?page="+page+"&keyword="+keyword; //La redirection va également permettre de vider les champs une fois les données validés et sauvegardés
    }

    //Editer patient
    @GetMapping("/admin/editPatient")
    public String editPatient(Model model,Long id ,String keyword,int page) {
        Patient patient = patientRepository.findById(id).orElse(null);//avec orElse: cherche moi ce patient,s'il n'existe pas return null | on peut aussi utiliser .get() à la  fin
        if (patient == null) {
            throw new RuntimeException("Patient introuvable");
        }
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("patient",patient);
        return "editPatient";
    }

    //API : exemple de retour des données au format JSON
    @GetMapping("/user/patients")
    @ResponseBody
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }

}
