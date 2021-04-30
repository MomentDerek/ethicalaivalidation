package com.sydney.au.ethicalaivalidation.controller;

import com.sydney.au.ethicalaivalidation.service.ValidatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/questionnairelist")
public class ValidatorController {

    private final ValidatorService validatorService;

    public ValidatorController(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @GetMapping()
    public @ResponseBody
    ResponseEntity<Object> getProject(@AuthenticationPrincipal UserDetails userDetails) {
        List<Map<String, Object>> res = validatorService.getProjectList(userDetails.getUsername());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{projectname}")
    public @ResponseBody
    ResponseEntity<Object> getProjectDetail(@PathVariable("projectname") String projectName, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> res = validatorService.getProjectDetail(projectName, userDetails.getUsername());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/{projectname}/addcomment")
    public @ResponseBody
    ResponseEntity<Object> addComment(@PathVariable("projectname") String projectName,
                                      @RequestBody Map<String, Object> req,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        Integer subQuestionId = (Integer) req.get("subquesid");
        String comment = (String) req.get("comment");
        Boolean passed = req.get("passed").equals(1);
        boolean res = validatorService.addComment(projectName, userDetails.getUsername(), subQuestionId, comment, passed);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{projectname}/addsummary")
    public @ResponseBody
    ResponseEntity<Object> addSummary(@PathVariable("projectname") String projectName,
                                      @RequestBody Map<String, Object> req,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        Integer segmentId = (Integer) req.get("segmentid");
        String comment = (String) req.get("comment");
        boolean res = validatorService.addSummary(projectName, userDetails.getUsername(), segmentId, comment);
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        TreeMap<String, String> resMap = new TreeMap<>();
        resMap.put("message", "Not all questions in this segment passed.");
        return new ResponseEntity<>(resMap, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{projectname}/pass")
    public @ResponseBody
    ResponseEntity<Object> passProject(@PathVariable("projectname") String projectName,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        boolean res = validatorService.passProject(projectName, userDetails.getUsername());
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{projectname}/sendfeedback")
    public @ResponseBody
    ResponseEntity<Object> sendFeedback(@PathVariable("projectname") String projectName,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        boolean res = validatorService.sendFeedback(projectName, userDetails.getUsername());
        if (res) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
