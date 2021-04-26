package com.sydney.au.ethicalaivalidation.controller;

import com.sydney.au.ethicalaivalidation.service.SupplierAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SupplierAdminController {

    private final SupplierAdminService supplierAdminService;

    public SupplierAdminController(SupplierAdminService supplierAdminService) {
        this.supplierAdminService = supplierAdminService;
    }

    @GetMapping(path = "/project")
    public @ResponseBody
    ResponseEntity<Object> getProject(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return new ResponseEntity<>("token is null",HttpStatus.BAD_REQUEST);
        List<Map<String, Object>> res = supplierAdminService.getProject(userDetails.getUsername());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/newproject")
    public @ResponseBody
    ResponseEntity<List> getPrinciple() {
        List<Map<String, Object>> res = supplierAdminService.getPrinciple();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/newproject")
    public @ResponseBody
    ResponseEntity<Object> postNewProject(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody Map<String, Object> req) {
        if (userDetails == null) return new ResponseEntity<>("token is null",HttpStatus.BAD_REQUEST);
        List<String> principleList = (List<String>) req.get("ethicalconcerns");
        Boolean res = supplierAdminService.newProject(userDetails.getUsername(),
                (String) req.get("projectname"),
                (String) req.get("description"),
                principleList.stream().map(Integer::parseInt).collect(Collectors.toList()));
        if (res) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/project/{projectname}")
    public @ResponseBody
    ResponseEntity<Object> getProjectInfo(@PathVariable("projectname") String projectName,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return new ResponseEntity<>("token is null",HttpStatus.BAD_REQUEST);
        if (!supplierAdminService.isUserCreateProject(userDetails.getUsername(), projectName)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> res = supplierAdminService.getProjectInfo(projectName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/assignproject/{projectname}")
    public @ResponseBody
    ResponseEntity<Object> getAllSupplier(@PathVariable("projectname") String projectName,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return new ResponseEntity<>("token is null",HttpStatus.BAD_REQUEST);
        if (!supplierAdminService.isUserCreateProject(userDetails.getUsername(), projectName)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Map<String, Object>> res = supplierAdminService.getAllSupplier();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/assignproject/{projectname}")
    public @ResponseBody
    ResponseEntity<Object> postAssignSupplier(@PathVariable("projectname") String projectName,
                                           @AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody List<Integer> req) {
        if (userDetails == null) return new ResponseEntity<>("token is null",HttpStatus.BAD_REQUEST);
        if (!supplierAdminService.isUserCreateProject(userDetails.getUsername(), projectName)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean res = supplierAdminService.postAssignSupplier(projectName, req);
        if (res) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
