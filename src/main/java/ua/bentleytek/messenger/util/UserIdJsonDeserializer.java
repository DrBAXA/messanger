package ua.bentleytek.messenger.util;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.UserService;

import java.io.IOException;

/**
 *
 */
public class UserIdJsonDeserializer extends JsonDeserializer<User>{

    @Autowired
    UserService userService;

    //Autowire components on creating;
    public UserIdJsonDeserializer() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        ObjectCodec userCodec = jsonParser.getCodec();
        JsonNode userNode = userCodec.readTree(jsonParser);

        return userService.getUser(userNode.get("id").asInt());

    }
}
