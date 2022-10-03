package org.launchcode.techjobs.mvc.controllers;

import org.launchcode.techjobs.mvc.models.Job;
import org.launchcode.techjobs.mvc.models.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.launchcode.techjobs.mvc.controllers.ListController.columnChoices;


/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("search")
public class SearchController {


    @RequestMapping(value = "")
    public String search(Model model) {
        model.addAttribute("columns", columnChoices);
        return "search";
    }


    @RequestMapping(value = "results")
    @PostMapping("results")
    public String displaySearchResults(Model model, @RequestParam String searchType,@RequestParam String searchTerm) {
        ArrayList<Job> jobs = JobData.findByColumnAndValue(searchType, searchTerm);
        if (searchTerm.equals("")) {
            jobs = JobData.findAll();
        } else if (searchTerm.equalsIgnoreCase("all")) {
            jobs = JobData.findAll();
        }

        model.addAttribute("columns", columnChoices);
        model.addAttribute("title", "All Jobs");
        model.addAttribute("column", searchType);
        model.addAttribute("jobs", jobs);
        return "search";
    }

}