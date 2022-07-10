package com.bijenkorf.image.util;

public class FileUtils {

    public FileUtils(){
    }

    private static final int FOLDER_NAME_LENGTH = 4;

    public static String generateFilePath(String reference, String imageType) {
        String referenceWithoutExtension = reference.substring(0, reference.lastIndexOf("."));
        String path = imageType + "/";
        if (referenceWithoutExtension.length() > FOLDER_NAME_LENGTH*2) {
            path = path + referenceWithoutExtension.substring(0, FOLDER_NAME_LENGTH) + "/" +
                    referenceWithoutExtension.substring(FOLDER_NAME_LENGTH, FOLDER_NAME_LENGTH*2) + "/" + reference;
        } else if (reference.length() > FOLDER_NAME_LENGTH) {
            path = path + referenceWithoutExtension.substring(0, FOLDER_NAME_LENGTH) + "/" + reference;
        } else {
            path = path + reference;
        }
        return path;
    }

}
