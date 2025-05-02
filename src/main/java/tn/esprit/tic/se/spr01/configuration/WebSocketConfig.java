package tn.esprit.tic.se.spr01.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configuration de l'endpoint WebSocket avec la gestion CORS
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200") // Autoriser les connexions depuis Angular
                .withSockJS(); // SockJS pour la gestion de la reconnexion
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Préfixe pour les messages entrants
        config.setApplicationDestinationPrefixes("/app"); // Préfixe pour les messages sortants
    }
}
