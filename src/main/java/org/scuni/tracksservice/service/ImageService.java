package org.scuni.tracksservice.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.scuni.tracksservice.exception.ImageDownloadException;
import org.scuni.tracksservice.exception.ImageUploadException;
import org.scuni.tracksservice.model.ImageDownload;
import org.scuni.tracksservice.properties.MinioProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String upload(MultipartFile image) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException("failed to upload image: " + e.getMessage());
        }

        if (image.isEmpty() || image.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must have name");
        }

        String fileName = generateFileName(image);
        InputStream inputStream;
        try {
            inputStream = image.getInputStream();
        } catch (IOException e) {
            throw new ImageUploadException("failed to upload image: " + e.getMessage());
        }
        saveImage(inputStream, fileName);

        return fileName;
    }

    public ImageDownload download(String imageName) {
        byte[] bytes;
        try(GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                .object(imageName)
                .build())) {
            bytes = object.readAllBytes();
        } catch (ErrorResponseException e){
            if (e.errorResponse().code().equals("NoSuchKey")){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find image: " + imageName);
            }
            throw new ImageDownloadException("failed to download image: " + e.getMessage());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e){
            throw new ImageDownloadException("failed to download image: " + e.getMessage());
        }

        String extension = imageName.substring(imageName.lastIndexOf(".") + 1);
        return new ImageDownload(extension, bytes);
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(MultipartFile image) {
        String extension = getExtension(image);
        if (!(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png"))) {
            throw new ImageUploadException("unsupported image extension: " + extension);
        }
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile image) {
        return image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
