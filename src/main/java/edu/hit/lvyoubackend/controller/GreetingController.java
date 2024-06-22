package edu.hit.lvyoubackend.controller;


import edu.hit.lvyoubackend.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Message greeting(Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Message("Hello, " + message.getMessage() + "!");
    }

    @SendTo("/topic/greetings")
    public Message sendMessage(){
        return new Message("i can send message to you!");
    }
}
