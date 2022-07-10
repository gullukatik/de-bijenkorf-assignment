package com.bijenkorf.image.controller;

import com.bijenkorf.image.service.ImageService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image/")
public class ImageController {

    private final ImageService imageService;

    ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("show/{predefinedTypeName}/{seoName}/")
    public byte[] getOptimizedImage(@PathVariable String predefinedTypeName,
                                    @PathVariable(required = false) String seoName,
                                    @RequestParam(name = "reference") String reference) {
        return imageService.getOptimizedImage(predefinedTypeName, seoName, reference);
    }

    @DeleteMapping("flush/{predefinedTypeName}/{seoName}/")
    public void flushImages(@PathVariable String predefinedTypeName,
                                    @PathVariable(required = false) String seoName,
                                    @RequestParam(name = "reference") String reference) {
        imageService.flushImages(predefinedTypeName, seoName, reference);
    }
}
