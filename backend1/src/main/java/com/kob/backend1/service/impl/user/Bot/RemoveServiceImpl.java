package com.kob.backend1.service.impl.user.Bot;

import com.kob.backend1.pojo.User;
import com.kob.backend1.service.impl.utils.UserDetailsImpl;
import com.kob.backend1.service.user.bot.RemoveService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class RemoveServiceImpl implements RemoveService {
    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        return null;
    }
}
