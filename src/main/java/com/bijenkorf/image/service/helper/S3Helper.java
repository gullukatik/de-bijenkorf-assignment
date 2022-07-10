package com.bijenkorf.image.service.helper;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bijenkorf.image.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class S3Helper {

    private final Logger logger = LogManager.getLogger();

    @Value("${aws.s3-endpoint}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    public byte[] downloadFile(String filePath, String reference) {
        try {
            S3ObjectInputStream s3is = amazonS3.getObject(bucketName, filePath).getObjectContent();
            FileOutputStream fos = new FileOutputStream(reference);

            byte[] readBuf = new byte[1024];
            int readLen = 0;
            while ((readLen = s3is.read(readBuf)) > 0) {
                fos.write(readBuf, 0, readLen);
            }
            s3is.close();
            fos.close();
            return readBuf;
        } catch (AmazonServiceException | IOException e) {
            logger.info(String.format("Image: %s can not be downloaded from S3", filePath));
            throw new NotFoundException("Image not found");
        } catch (AmazonClientException e) {
            logger.error("S3 connection error");
            throw new NotFoundException("S3 connection error");
        }
    }

    public void uploadFile(byte[] image, String filePath, String fileName) {
        try {
            File file = new File(fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(image);
            outputStream.close();
            amazonS3.putObject(bucketName, filePath, file);
            logger.info(String.format("Image: %s is uploaded to S3", filePath));
        } catch (AmazonServiceException | IOException e) {
            logger.info(String.format("Image: %s can not be uploaded to S3", filePath));
            throw new NotFoundException("Image can not be uploaded to S3 bucket");
        } catch (AmazonClientException e) {
            logger.error("S3 connection error");
            throw new NotFoundException("S3 connection error");
        }
    }

    public void deleteFile(String filePath) {
        try {
            amazonS3.deleteObject(bucketName, filePath);
            logger.info(String.format("Image: %s is deleted from S3", filePath));
        } catch (AmazonServiceException e) {
            logger.info(String.format("Image: %s can not be deleted from S3", filePath));
            throw new NotFoundException("Image not found");
        } catch (AmazonClientException e) {
            logger.error("S3 connection error");
            throw new NotFoundException("S3 connection error");
        }
    }

    public List<S3ObjectSummary> getObjectList(String filePath) {
        try {
            return amazonS3.listObjects(bucketName, filePath).getObjectSummaries();
        } catch (AmazonClientException e) {
            logger.error("S3 connection error");
            throw new NotFoundException("S3 connection error");
        }
    }
}
