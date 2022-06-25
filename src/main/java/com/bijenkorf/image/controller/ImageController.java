package com.bijenkorf.image.controller;

import com.bijenkorf.image.service.ImageService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private final ImageService imageService;

    ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
}
