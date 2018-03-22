package be.kdg.musicmaker.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Random;

@Controller
public class ChatController {

    @MessageMapping("/chat/{chatid}")
    @SendTo("/topic/chat/{chatid}")
    public ServerMessage process(@DestinationVariable long chatid, ChatMessage message) throws Exception {
        //generate random lag
        Random rn = new Random();
        Thread.sleep((rn.nextInt(5) + 1) * 1000);
        return MessageProcessor.build(message);
    }

    @Autowired
    SimpMessagingTemplate template;


//    @Scheduled(fixedDelay = 60000)
//    public void setNotification() throws Exception {
//        LocalTime now = LocalTime.now();
//        this.template.convertAndSend("/topic/chat", new ServerMessage("[server]: It is " + now.getHour() + ":" + now.getMinute()));
//    }
}
