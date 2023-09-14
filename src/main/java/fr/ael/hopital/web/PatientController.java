package fr.ael.hopital.web;

import fr.ael.hopital.entities.Patient;
import fr.ael.hopital.repository.PatientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping("/user/index")
    @PreAuthorize("hasRole('USER')")
    public String index(Model model,
                         @RequestParam(name ="page",defaultValue = "0") int page,
                         @RequestParam(name ="size",defaultValue = "4")int size,
                         @RequestParam(name ="keyword",defaultValue = "")String kw){
        Page<Patient> patientPage = patientRepository.findByNomContains(kw,PageRequest.of(page,size));
        model.addAttribute("listPatients",patientPage.getContent());
        model.addAttribute("pages",new int[patientPage.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",kw);
        return "patients"; //retourne la page patients.html
    }

    @GetMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@RequestParam(name ="id") Long id,
                         @RequestParam(name ="keyword",defaultValue = "") String keyword,
                         @RequestParam(name ="page",defaultValue = "0") int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public String home(){
        return "redirect:/user/index";
    }

    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ADMIN')")
    public String formPatients(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }

    @PostMapping("/admin/savePatient")
    @PreAuthorize("hasRole('ADMIN')")
    public String savePatient(@Valid Patient patient, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "formPatients";
        }
        System.out.println(patient);
        patientRepository.save(patient);
        return "redirect:/user/index?keyword="+patient.getNom();
    }
    @GetMapping("/admin/editerPatient")
    @PreAuthorize("hasRole('ADMIN')")
    public String editerPatient(Model model,
                                @RequestParam(name = "id") Long id)
    {
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient",patient);
        return "editerPatient";
    }

}
