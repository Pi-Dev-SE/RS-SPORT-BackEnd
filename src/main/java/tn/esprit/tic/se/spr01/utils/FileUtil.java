package tn.esprit.tic.se.spr01.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtil {

    @Value("${backend.url}")
    public void setBackendUrl(String backendUrl) {
        FileUtil.BACKEND_URL = backendUrl;
    }
    private static String BACKEND_URL;
    public static final String UPLOADS_FOLDER = "uploads";

    public String storeFile(final MultipartFile file, final String folderName) {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if(folderName == null || folderName.isEmpty()) {
            throw new IllegalArgumentException("Folder name is empty");
        }
        return storeFileLocal(file, folderName);
    }


    private String storeFileLocal(final MultipartFile file, final String folderName) {
        final String fileName = UUID.randomUUID() + getFileExtension(file.getOriginalFilename());
        Path path = Paths.get(UPLOADS_FOLDER, folderName);
        if(!path.toFile().exists()) {
            path.toFile().mkdirs();
        }
        try {
            file.transferTo(path.resolve(fileName));
            return fileName;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to store file");
        }
    }


    private String storeFileFirebase(final MultipartFile file, final String folderName) {
        return null;
    }


    public boolean deleteFile(final String fileName) throws IOException {
        return deleteFileLocal(fileName);
    }


    private boolean deleteFileLocal(final String fileName) throws IOException {
        Path path = Paths.get(UPLOADS_FOLDER, fileName);
        return Files.deleteIfExists(path);
    }


    private boolean deleteFileFirebase(final String fileName) {
        return false;
    }


    public String getFileExtension(final String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public static String getFileUrl(final String folderName, final String fileName) {
        return BACKEND_URL + "/images/" + folderName + "/" + fileName;
    }

}

