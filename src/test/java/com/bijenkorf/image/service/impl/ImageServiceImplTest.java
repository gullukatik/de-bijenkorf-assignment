package com.bijenkorf.image.service.impl;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bijenkorf.image.config.PredefinedImageHelper;
import com.bijenkorf.image.service.helper.S3Helper;
import com.bijenkorf.image.util.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ImageServiceImplTest {

    @Mock
    PredefinedImageHelper predefinedImageHelper;

    @Mock
    private S3Helper s3Helper;

    @InjectMocks
    ImageServiceImpl imageService;

    private ImageServiceImpl imageService1;

    private static final String REFERENCE = "test_image_name.png";
    private static final String THUMBNAIL = "thumbnail";
    private static final String ORIGINAL = "original";

    @Before
    public void setUp() {
        ImageServiceImpl imageServiceImpl = new ImageServiceImpl(predefinedImageHelper, s3Helper);
        imageService1 = Mockito.spy(imageServiceImpl);
    }

    @Test
    public void getOptimizedImageAndOriginalImageDoNotExist() {

        byte[] optimizedImage = new byte[]{1, 2, 3};
        byte[] image = new byte[]{1, 2, 3, 4, 5};

        String imagePath = FileUtils.generateFilePath(REFERENCE, ORIGINAL);
        String optimizedImagePath = FileUtils.generateFilePath(REFERENCE, THUMBNAIL);

        when(s3Helper.downloadFile(imagePath, REFERENCE)).thenReturn(image);
        when(s3Helper.downloadFile(optimizedImagePath, REFERENCE)).thenReturn(optimizedImage);
        when(s3Helper.getObjectList(optimizedImagePath)).thenReturn(new ArrayList<>());
        when(s3Helper.getObjectList(imagePath)).thenReturn(new ArrayList<>());
        doReturn(optimizedImage).when(imageService1).optimizeImage(image, optimizedImagePath, REFERENCE);
        doReturn(image).when(imageService1).downloadImageFromOriginalSource(REFERENCE);
        doNothing().when(s3Helper).uploadFile(optimizedImage, optimizedImagePath, REFERENCE);
        doNothing().when(s3Helper).uploadFile(image, imagePath, REFERENCE);

        Assert.assertEquals(optimizedImage, imageService1.getOptimizedImage(THUMBNAIL, "", REFERENCE));
    }

    @Test
    public void getOptimizedImageDoesNotExistOriginalImageExist() {

        byte[] optimizedImage = new byte[]{1, 2, 3};
        byte[] image = new byte[]{1, 2, 3, 4, 5};

        String imagePath = FileUtils.generateFilePath(REFERENCE, ORIGINAL);
        String optimizedImagePath = FileUtils.generateFilePath(REFERENCE, THUMBNAIL);

        when(s3Helper.downloadFile(imagePath, REFERENCE)).thenReturn(image);
        when(s3Helper.downloadFile(optimizedImagePath, REFERENCE)).thenReturn(optimizedImage);
        when(s3Helper.getObjectList(optimizedImagePath)).thenReturn(new ArrayList<>());
        when(s3Helper.getObjectList(imagePath)).thenReturn(List.of(new S3ObjectSummary()));
        doReturn(optimizedImage).when(imageService1).optimizeImage(image, optimizedImagePath, REFERENCE);
        doNothing().when(s3Helper).uploadFile(optimizedImage, optimizedImagePath, REFERENCE);

        Assert.assertEquals(optimizedImage, imageService1.getOptimizedImage(THUMBNAIL, "", REFERENCE));
    }

    @Test
    public void getOptimizedImageExist() {

        byte[] optimizedImage = new byte[]{1, 2, 3};

        String optimizedImagePath = FileUtils.generateFilePath(REFERENCE, THUMBNAIL);

        when(s3Helper.downloadFile(optimizedImagePath, REFERENCE)).thenReturn(optimizedImage);
        when(s3Helper.getObjectList(optimizedImagePath)).thenReturn(List.of(new S3ObjectSummary()));

        Assert.assertEquals(optimizedImage, imageService.getOptimizedImage(THUMBNAIL, "", REFERENCE));
    }

    @Test
    public void flushImages() {
    }
}