package com.bijenkorf.image.service.impl;

import com.bijenkorf.image.config.PredefinedImageHelper;
import com.bijenkorf.image.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bijenkorf.image.config.PredefinedImageHelper.*;

@Service
public class ImageServiceImpl implements ImageService {

    List<PredefinedImageType> predefinedImageTypes;

    ImageServiceImpl(PredefinedImageHelper predefinedImageHelper) {
        predefinedImageTypes = predefinedImageHelper.getTypes();
    }

}
