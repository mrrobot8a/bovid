package com.alcadia.bovid.Event;



import org.springframework.context.ApplicationEvent;


import com.alcadia.bovid.Models.Entity.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sampson Alfred
 */
@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private  User user;
    private String applicationUrl;

    public RegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

   
}