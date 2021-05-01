package com.sydney.au.ethicalaivalidation.controller;

import com.sydney.au.ethicalaivalidation.service.AdminQuestionsService;
import com.sydney.au.ethicalaivalidation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/admin/questions")
public class AdminQuestionsController {

    private final AdminQuestionsService adminQuestionsService;
    private final UserService userService;

    public AdminQuestionsController(AdminQuestionsService adminQuestionsService, UserService userService) {
        this.adminQuestionsService = adminQuestionsService;
        this.userService = userService;
    }

    private boolean checkIsNotAdmin(UserDetails userDetails) {
        if (userDetails == null) return false;
        return !userService.getUserType(userDetails.getUsername()).equals(4);
    }


    @GetMapping("/questionlist")
    public @ResponseBody
    ResponseEntity<Object> listAllQuestion(@AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminQuestionsService.listAllQuestion();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/principles")
    public @ResponseBody
    ResponseEntity<Object> listAllPrinciple(@AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminQuestionsService.listAllPrinciple();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{principleid}/segments")
    public @ResponseBody
    ResponseEntity<Object> listSegmentByPrinciple(@PathVariable("principleid") Integer principleId,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminQuestionsService.listSegmentByPrinciple(principleId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{principleid}/{segmentid}/questions")
    public @ResponseBody
    ResponseEntity<Object> listQuestionBySegment(@PathVariable("segmentid") Integer segmentId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Map<String, Object>> res = adminQuestionsService.listQuestionBySegment(segmentId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/addprinciple")
    public @ResponseBody
    ResponseEntity<Object> checkAdmin(@AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addprinciple")
    public @ResponseBody
    ResponseEntity<Object> addPrinciple(@RequestBody Map<String, Object> reqMap,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (adminQuestionsService.addPrinciple(String.valueOf(reqMap.get("principle"))))
            return new ResponseEntity<>(HttpStatus.OK);
        else {
            TreeMap<String, Object> res = new TreeMap<>();
            res.put("message", "This principle name already exists, please use another name!");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addsegment")
    public @ResponseBody
    ResponseEntity<Object> addSegment(@RequestBody Map<String, Object> reqMap,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (adminQuestionsService.addSegment((Integer) reqMap.get("principleid"), String.valueOf(reqMap.get("segment"))))
            return new ResponseEntity<>(HttpStatus.OK);
        else {
            TreeMap<String, Object> res = new TreeMap<>();
            res.put("message", "This segment name already exists, please use another name!");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addquestion")
    public @ResponseBody
    ResponseEntity<Object> addQuestion(@RequestBody Map<String, Object> reqMap,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (adminQuestionsService.addQuestion(
                (Integer) reqMap.get("segmentid"),
                String.valueOf(reqMap.get("question"))))
            return new ResponseEntity<>(HttpStatus.OK);
        else {
            TreeMap<String, Object> res = new TreeMap<>();
            res.put("message", "This question name already exists, please enter another question!");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addsubquestion")
    public @ResponseBody
    ResponseEntity<Object> addSubQuestion(@RequestBody Map<String, Object> reqMap,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (adminQuestionsService.addSubQuestion(
                (Integer) reqMap.get("questionid"),
                (Integer) reqMap.get("questiontypeid"),
                String.valueOf(reqMap.get("subquescontent")),
                (Integer) reqMap.get("answer")))
            return new ResponseEntity<>(HttpStatus.OK);
        else {
            TreeMap<String, Object> res = new TreeMap<>();
            res.put("message", "This subquestion already exists, please enter other content!");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/questiontype")
    public @ResponseBody
    ResponseEntity<Object> listQuestionType(@AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(adminQuestionsService.listAllQuestionType(), HttpStatus.OK);
    }

    @GetMapping("/{subquestionid}")
    public @ResponseBody
    ResponseEntity<Object> getSubQuestionDetail(@PathVariable("subquestionid") Integer subQuestionId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Map<String, Object> res = adminQuestionsService.getSubQuestionDetail(subQuestionId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{subquestionid}")
    public @ResponseBody
    ResponseEntity<Object> updateSubQuestionDetail(@PathVariable("subquestionid") Integer subQuestionId,
                                                   @RequestBody Map<String, Object> reqMap,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        boolean res = adminQuestionsService.updateSubQuestion(
                (Integer) reqMap.get("principleid"),
                (Integer) reqMap.get("segmentid"),
                (Integer) reqMap.get("questionid"),
                subQuestionId,
                String.valueOf(reqMap.get("subquestion")),
                (Integer) reqMap.get("questiontype"),
                (Integer) reqMap.get("answer"));
        if (res) return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            TreeMap<String, String> message = new TreeMap<>();
            message.put("message", "Information conflict!");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{subquestionid}")
    public @ResponseBody
    ResponseEntity<Object> deleteSubQuestion(@PathVariable("subquestionid") Integer subQuestionId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (checkIsNotAdmin(userDetails))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (adminQuestionsService.deleteSubQuestion(subQuestionId)) return new ResponseEntity<>(HttpStatus.OK);
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
