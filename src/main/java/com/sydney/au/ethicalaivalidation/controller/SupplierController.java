package com.sydney.au.ethicalaivalidation.controller;

import com.sydney.au.ethicalaivalidation.service.SupplierService;
import com.sydney.au.ethicalaivalidation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Moment on 7/4/2021
 * @package: com.sydney.au.ethicalaivalidation.controller
 * @version: 1.0
 * <b>Description:</b>
 * <p>AI Supplier requests</p>
 */
@RestController
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(UserService userService, SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    //Show all projects that are assigned to this supplier
    @GetMapping(path = "/projectlist")
    public @ResponseBody ResponseEntity<List> getProjectList(@AuthenticationPrincipal UserDetails userDetails) {
        List<Map<String, String>> res = supplierService.listProject(userDetails.getUsername());
        if (res.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //find an unlocked question and its first unfinished subquestion.
    @GetMapping(path = "/answer/{projectname}")
    public @ResponseBody ResponseEntity<Map> getQuestion(@PathVariable("projectname") String projectName,@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, String> res = supplierService.getQuestion(projectName, userDetails.getUsername());
        if (res.isEmpty()) {
            res.put("message", "project name is wrong");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //Show a question page and user can answer
    @GetMapping(path = "/answer/{projectname}/{questionid}/{subquesid}")
    public @ResponseBody ResponseEntity<Map> getQuestionPage(@PathVariable("projectname") String projectName,
                                                             @PathVariable("questionid") String questionId,
                                                             @PathVariable("subquesid") String subquesId,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> res = supplierService.getQuestionPage(projectName,userDetails.getUsername(),questionId,subquesId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //Answer the question
    @PostMapping(path = "/answer/{projectname}/{questionid}/{subquesid}")
    public @ResponseBody ResponseEntity<Map> postAnswer(@PathVariable("projectname") String projectName,
                                                        @PathVariable("questionid") String questionId,
                                                        @PathVariable("subquesid") String subquesId,
                                                        @RequestBody Map<String,Object> req) {
        Map<String, Object> res = supplierService.postAnswer(projectName, Integer.parseInt(questionId), Integer.parseInt(subquesId), req);
        if (res.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //Show all message (project assignment and feedbacks)
    @GetMapping(path = "/feedback")
    public @ResponseBody ResponseEntity<List> getAllMessage(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println(username);
        List<Map<String, Object>> res = supplierService.getAllMessage(username);
        if (res.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //Feedback Detail page: validator feedback
    @GetMapping(path = "/feedback/projectfeedback/{projectname}/{validatorname}/{feedbacktime}")
    public @ResponseBody ResponseEntity<List> getValidatorFeedback(@PathVariable("projectname") String projectName,
                                                                   @PathVariable("validatorname") String validatorName,
                                                                   @PathVariable("feedbacktime") String feedbackTime) {
        List<Map<String, Object>> res = supplierService.getValidatorFeedback(projectName,validatorName,feedbackTime);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //Feedback Detail page: assign project to this user
    @GetMapping(path = "/feedback/projectassignment/{projectname}")
    public @ResponseBody ResponseEntity<Map> getAssignFeedback(@PathVariable("projectname") String projectName) {
        Map<String, Object> res = supplierService.getAssignFeedback(projectName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    //Show a report after the project's status is finished.
    @GetMapping(path = "/report/{projectname}")
    public @ResponseBody
    ResponseEntity<Map> getReport(@PathVariable("projectname") String projectName) {
        Map<String, Object> res = supplierService.getReport(projectName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
