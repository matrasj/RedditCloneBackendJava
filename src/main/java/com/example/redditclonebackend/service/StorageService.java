package com.example.redditclonebackend.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final AmazonS3Client s3Client;


    @Value("${application.bucket.name}")
    private String bucketName;


    @Transactional
    public String saveImage(MultipartFile multipartFile, String folderName) throws IOException{
        File convertedFile = convertMultiPartFileToFile(multipartFile);

        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, folderName + "/" + fileName, convertedFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        convertedFile.delete();
        return s3Client.getResourceUrl(bucketName, folderName + "/" + fileName);
    }

    @Transactional
    public void deleteImage(String folderImage, String imageName) {
        s3Client.deleteObject(bucketName, folderImage + "/" + imageName);
    }

    private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)
                ) {
            fileOutputStream.write(multipartFile.getBytes());

            return convertedFile;

        } catch (IOException exception) {
            log.error("Error converting multi part file to file");
            throw new IOException("Error converting multi part file to file");
        }
    }
}
