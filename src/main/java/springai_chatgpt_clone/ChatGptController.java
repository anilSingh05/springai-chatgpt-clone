package springai_chatgpt_clone;

import groovy.util.logging.Slf4j;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@Slf4j
public class ChatGptController {

    private static final Logger log = LoggerFactory.getLogger(ChatGptController.class);
    private  final ChatClient chatClient;

    public ChatGptController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @GetMapping("")
    public String home() {
        return "index";
    }

    @HxRequest
    @PostMapping("/api/chat")
    public HtmxResponse generate(@RequestParam String message, Model model) {
        log.info("User Message: {}", message);
        String response = chatClient.prompt()
                .user(message)
                .call()
                .content();

        model.addAttribute("response",response);
        model.addAttribute("message",message);

        return HtmxResponse.builder()
                .view("response :: responseFragment")
                .view("recent-message-list :: messageFragment")
                .build();
    }

    /*@GetMapping("/chatOn")
    public   String getChatWithMovieBuzz(@RequestParam(value = "input",  defaultValue = "Tell me  a  dailouge  from Sholey movie")
                                         String input){
           return chatClient
                    .prompt()
                    .user(input)
                    .call().content();
    }*/
}
