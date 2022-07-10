package com.bijenkorf.image.service;

public interface ImageService {

    byte[] getOptimizedImage(String predefinedTypeName, String dummySeoName, String reference);

    void flushImages(String predefinedTypeName, String dummySeoName, String reference);
}
