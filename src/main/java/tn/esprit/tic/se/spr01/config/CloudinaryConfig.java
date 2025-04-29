package tn.esprit.tic.se.spr01.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dvsdi0bta");
        config.put("api_key", "479173856529344");
        config.put("api_secret", "XI-w6utP46yiwZmT_OLT4WW9gKY");
        return new Cloudinary(config);
    }
}
