package com.sydney.au.ethicalaivalidation.controller;

import com.sydney.au.ethicalaivalidation.service.AdminProjectService;
import com.sydney.au.ethicalaivalidation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/projects/projectlist")
public class AdminProjectController {

    private final AdminProjectService adminProjectService;
    private final UserService userService;

    public AdminProjectController(AdminProjectService adminProjectService, UserService userService) {
        this.adminProjectService = adminProjectService;
        this.userService = userService;
    }

    private boolean checkIsNotAdmin(UserDetails userDetails) {
        if (userDetails == null) return false;
        return !userService.getUserType(userDetails.getUsername()).equals(4);
    }

    @GetMapping()
    public @ResponseBody
    ResponseEntity<Object> listAllProject(@AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminProjectService.listAllProject();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("{projectname}")
    public @ResponseBody
    ResponseEntity<Object> getProjectDetail(@PathVariable("projectname") String projectName,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Map<String, Object> res = adminProjectService.getProjectDetail(projectName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{projectname}/addquestion")
    public @ResponseBody
    ResponseEntity<Object> listOtherQuestion(@PathVariable("projectname") String projectName,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminProjectService.listOtherQuestion(projectName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/{projectname}/addquestion")
    public @ResponseBody
    ResponseEntity<Object> addOtherQuestion(@PathVariable("projectname") String projectName,
                                            @RequestBody Map<String, Integer> reqMqp,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (adminProjectService.addOtherQuestion(projectName, reqMqp.get("projectid"), reqMqp.get("subquesid")))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{projectname}/deletequestion/{subquestionid}")
    public @ResponseBody
    ResponseEntity<Object> deleteQuestion(@PathVariable("projectname") String projectName,
                                          @PathVariable("subquestionid") Integer subQuesId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (adminProjectService.deleteQuestion(projectName, subQuesId))
            return new ResponseEntity<>(HttpStatus.OK);
        Map<String, String> res = new TreeMap<>();
        res.put("message", "Wrong information!");
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{projectname}/assignvalidator")
    public @ResponseBody
    ResponseEntity<Object> listAllValidator(@PathVariable("projectname") String projectName,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminProjectService.listAllValidator(projectName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/{projectname}/assignvalidator")
    public @ResponseBody
    ResponseEntity<Object> assignValidator(@PathVariable("projectname") String projectName,
                                           @RequestBody List<Map<String, Integer>> reqList,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (adminProjectService.assignValidator(projectName,
                reqList.parallelStream().map(validator -> validator.get("userid")).collect(Collectors.toList())))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{projectname}/summary")
    public @ResponseBody
    ResponseEntity<Object> summaryTheProject(@PathVariable("projectname") String projectName,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Map<String, Object> res = adminProjectService.summaryTheProject(projectName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{projectname}/process")
    public @ResponseBody
    ResponseEntity<Object> getProjectProcess(@PathVariable("projectname") String projectName,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Map<String, Object> res = adminProjectService.summaryTheProject(projectName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
