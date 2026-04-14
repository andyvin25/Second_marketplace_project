package com.marketplace.Util;


import java.util.Random;

public class ImageName {

    public static String customLogoImage() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

//    public static String customLogoImage(String storeId) {
//        @SuppressWarnings("deprecation")
//        String generatedString = RandomStringUtils.randomAlphanumeric(10);
//        return storeId + "_profile_" + generatedString;
//    }

    public static String getImageFileExtensions(String fileName) {
        if (fileName == null) {
            return null;
        }
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

}
