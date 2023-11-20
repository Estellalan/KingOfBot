package com.kob.backend1.controller.user.bot;

import com.kob.backend1.pojo.Bot;
import com.kob.backend1.service.user.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetListController {
    @Autowired
    private GetListService getListService;

   @GetMapping("/user/bot/getlist")
    public List<Bot> getList(){
        return getListService.getList();
    }

}
