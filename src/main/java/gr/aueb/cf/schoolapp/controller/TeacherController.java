package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.dto.RegionReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.service.IRegionService;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final ITeacherService teacherService;
    private final IRegionService regionService;

//    @Autowired
//    public TeacherController(ITeacherService teacherService) {
//        this.teacherService = teacherService;
//    }

    @GetMapping("/insert")
    public String getTeacherForm(Model model) {
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());
        // model.addAttribute("regionsReadOnlyDTO", regions());
        return "teacher-insert";
    }

    @PostMapping("/insert")
    public String teacherInsert(@Valid @ModelAttribute("teacherInsertDTO")TeacherInsertDTO teacherInsertDTO,
                                BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "teacher-insert";
        }

        try {

            //Save Teacher

            //TeacherReadOnlyDTO teacherReadOnlyDTO = new TeacherReadOnlyDTO("acfd-1234", "Αθανάσιος", "Ανδρούτσος");
            //model.addAttribute("teacherReadOnlyDTO", teacherReadOnlyDTO);
            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.saveTeacher(teacherInsertDTO);

            redirectAttributes.addFlashAttribute("teacherReadOnlyDTO", teacherReadOnlyDTO);
            return "redirect:/teachers/success";

        } catch (EntityAlreadyExistException | EntityInvalidArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "teacher-insert";
        }

    }

    @GetMapping("/success")
    public String teacherSuccess(Model model) {
        return "teacher-success";
    }

   @ModelAttribute("regionsReadOnlyDTO")
    public List<RegionReadOnlyDTO> regions() {

        return regionService.findAllRegionsSortedByName();

//        return List.of(
//                new RegionReadOnlyDTO(1L, "Αθήνα"),
//                new RegionReadOnlyDTO(2L, "Βόλος"),
//                new RegionReadOnlyDTO(3L, "Θεσσαλονίκη"));
   }
}
