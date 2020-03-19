package com.vignyaandvaarnirman.apigateway.controller;

import com.vignyaandvaarnirman.apigateway.model.*;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.vignyaandvaarnirman.apigateway.ApiGatewayApplication;

@RestController
@EnableRabbit
@RequestMapping("/users")
public class APIController {

    @PostMapping(value = "/login")
    public String postUserLoginDetails(@RequestBody LoginUser user) {
        //TODO: Add User Management
        RestTemplate restTemplate = new RestTemplate();
         return restTemplate.postForObject("http://usermanagement:5000/users/login",user, String.class);
    }
    @GetMapping(value = "/profile")
    public ProfileUser getUserDetails(@RequestBody JWToken jwToken) {
        //TODO : Add User Management
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://usermanagement:5000/users/profile",ProfileUser.class,jwToken);
    }

    @PostMapping(value = "/register")
    public String postUserRegisterationDetails(@RequestBody RegUser regUser) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("Inside register: "+regUser.getfirst_Name());
        String response = restTemplate.postForObject("http://usermanagement:5000/users/register",regUser, String.class);
        return response;
    }

    @PostMapping(value = "/session")
    public String getSessionDetails(@RequestBody User_Id user_id) throws Exception {
          System.out.println(user_id);
          RestTemplate restTemplate = new RestTemplate();
          String ans = restTemplate.postForObject("http://sessionmanagement:7000/sessions",user_id, String.class);
          System.out.println(ans);
          return ans;
    }

    @PostMapping(value = "/dashboardsearch")
    public String getBottomData(@RequestBody Data data) {
        System.out.println("Dashboard data: "+ data.toString());
        ToDataR toDataR = new ToDataR();
        toDataR.send(data);
        String path = ApiGatewayApplication.getPath();
        if(path!=null)
            System.out.println("Sent to front-end:image");
        else
            System.out.println("Sent to front-end:null");
        return path;
    }

}
