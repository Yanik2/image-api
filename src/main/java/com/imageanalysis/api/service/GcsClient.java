package com.imageanalysis.api.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GcsClient {
    private static final String PROCESSED_IMAGE_PATH = "%s-processed";

    private final Storage storage;

    @Value("${cloud.bucket.name}")
    private String bucketName;

    public URL getUploadLink(String imageId) {
        final var blobInfo = BlobInfo.newBuilder(bucketName, imageId).build();
        return storage.signUrl(blobInfo, 10L, TimeUnit.MINUTES, getSignUrlOptions(HttpMethod.PUT));
    }

    public URL getDownloadLink(String imageId) {
        final var blobInfo =
            BlobInfo.newBuilder(bucketName, PROCESSED_IMAGE_PATH.formatted(imageId)).build();

        return storage.signUrl(blobInfo, 10L, TimeUnit.MINUTES, getSignUrlOptions(HttpMethod.GET));
    }

    public void deleteImage(String imageId) {
        final var blobInfo = BlobInfo.newBuilder(bucketName, imageId).build();
        storage.delete(BlobId.of(bucketName, imageId));
        storage.delete(BlobId.of(bucketName, PROCESSED_IMAGE_PATH.formatted(imageId)));
    }

    private Storage.SignUrlOption[] getSignUrlOptions(HttpMethod method) {
        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", "application/octet-stream");

        return new Storage.SignUrlOption[] {
            Storage.SignUrlOption.httpMethod(method),
            Storage.SignUrlOption.withExtHeaders(extensionHeaders)
        };
    }
}
