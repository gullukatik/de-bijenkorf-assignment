package com.bijenkorf.image.config;

import com.bijenkorf.image.model.ImageType;
import com.bijenkorf.image.model.ScaleType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "predefined.image")
public class PredefinedImageHelper {

    List<PredefinedImageType> types;

    @Data
    public static class PredefinedImageType {
        private String name;
        private int height;
        private int width;
        private int quality;
        private ScaleType scaleType;
        private String fillColor;
        private ImageType type;
    }
}
