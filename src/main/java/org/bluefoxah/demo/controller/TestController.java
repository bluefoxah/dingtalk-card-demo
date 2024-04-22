package org.bluefoxah.demo.controller;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.bluefoxah.demo.model.Request;
import org.bluefoxah.demo.model.Response;
import org.bluefoxah.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping(value = "/createGroup", produces = "application/json")
    public Response createGroup(@RequestBody Request request) {
        log.info("createGroup request: {}", JSON.toJSONString(request));
        testService.createGroup();
        return Response.builder().success(true).build();
    }

    @PostMapping(value = "/createGroupByTemplate", produces = "application/json")
    public Response createGroupByTemplate(@RequestBody Request request) {
        log.info("createGroup request: {}", JSON.toJSONString(request));
        testService.createGroupByTemplate();
        return Response.builder().success(true).build();
    }

    @PostMapping(value = "/createCard", produces = "application/json")
    public Response createCard(@RequestBody Request request) {
        log.info("createCard request: {}", JSON.toJSONString(request));
        testService.createCard();
        return Response.builder().success(true).build();
    }

    @PostMapping(value = "/deliverCardToGroup", produces = "application/json")
    public Response deliverCardToGroup(@RequestBody Request request) {
        log.info("deliverCardToGroup request: {}", JSON.toJSONString(request));
        testService.deliverCardToGroup();
        return Response.builder().success(true).build();
    }

    @PostMapping(value = "/deliverCardToTop", produces = "application/json")
    public Response deliverCardToTop(@RequestBody Request request) {
        log.info("deliverCardToTop request: {}", JSON.toJSONString(request));
        testService.deliverCardToTop();
        return Response.builder().success(true).build();
    }

    @PostMapping(value = "/updateCard", produces = "application/json")
    public Response updateCard(@RequestBody Request request) {
        log.info("updateCard request: {}", JSON.toJSONString(request));
        testService.updateCard();
        return Response.builder().success(true).build();
    }
}
