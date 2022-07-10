package com.bijenkorf.image.service.impl;

import com.bijenkorf.image.config.PredefinedImageHelper;
import com.bijenkorf.image.service.ImageService;
import com.bijenkorf.image.service.helper.S3Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.bijenkorf.image.config.PredefinedImageHelper.*;
import static com.bijenkorf.image.util.FileUtils.generateFilePath;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String ORIGINAL = "original";

    private final Logger logger = LogManager.getLogger();

    List<PredefinedImageType> predefinedImageTypes;

    private final S3Helper s3Helper;

    ImageServiceImpl(PredefinedImageHelper predefinedImageHelper,
                     S3Helper s3Helper) {
        predefinedImageTypes = predefinedImageHelper.getTypes();
        this.s3Helper = s3Helper;
    }

    @Override
    public byte[] getOptimizedImage(String predefinedTypeName, String dummySeoName, String reference) {

        //TODO: Validate request

        String optimizedImagePath = generateFilePath(reference, predefinedTypeName);

        if (imageExist(optimizedImagePath)) {
            return s3Helper.downloadFile(optimizedImagePath, reference);
        } else {
            String originalImagePath = generateFilePath(reference, ORIGINAL);
            byte[] originalImage;
            if (imageExist(originalImagePath)) {
                originalImage = s3Helper.downloadFile(originalImagePath, reference);
            }
            else {
                originalImage = downloadImageFromOriginalSource(reference);
            }
            return optimizeImage(originalImage, optimizedImagePath, reference);
        }
    }

    @Override
    public void flushImages(String predefinedTypeName, String dummySeoName, String reference) {
        if (predefinedTypeName.equals(ORIGINAL)) {
            predefinedImageTypes.forEach(predefinedImageType -> {
                String imagePath = generateFilePath(reference, predefinedImageType.getName());
                if (imageExist(imagePath)) {
                    s3Helper.deleteFile(imagePath);
                }
            });
        }

        String imagePath = generateFilePath(reference, predefinedTypeName);
        s3Helper.deleteFile(imagePath);
    }

    //Mock
    private byte[] optimizeImage(byte[] originalImage, String filePath, String reference) {
        try {
            BufferedImage image = ImageIO.read(new File("src/main/resources/images/arandompicture_optimized.jpg"));
            ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outStreamObj);
            byte [] optimizedImage = outStreamObj.toByteArray();
            s3Helper.uploadFile(optimizedImage, filePath, reference);
            return optimizedImage;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Image can not be optimized");
        }
    }

    private boolean imageExist(String path) {
        return !s3Helper.getObjectList(path).isEmpty();
    }

    //Mock
    private byte[] downloadImageFromOriginalSource(String reference) {
        String imagePath = generateFilePath(reference, ORIGINAL);
        try {
            BufferedImage image = ImageIO.read(new File("src/main/resources/images/arandompicture.jpg"));
            ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outStreamObj);
            byte [] byteArray = outStreamObj.toByteArray();
            s3Helper.uploadFile(byteArray, imagePath, reference);
            return byteArray;
        } catch (IOException e) {
            logger.error(String.format("Image can not be downloaded from original source: %s", reference));
            throw new RuntimeException("Image can not be downloaded from original source");
        }
    }
}
