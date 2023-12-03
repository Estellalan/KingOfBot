package com.kob.backend1.controller.user.account;

import com.kob.backend1.service.user.account.InforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {

    @Autowired
    private InforService inforService;

    @GetMapping("/user/account/info/")
    public Map<String,String> getinfo(){
        return inforService.getinfo();
    }
}
