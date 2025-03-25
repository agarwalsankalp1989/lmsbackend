package com.scholarbridge.lms.service;

import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.StringPrivateKeySupplier;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ObjectStorageService {
    private final ObjectStorageClient client;
    private final String bucketName;
    private final String namespace;
    private final String region; // Store the region as a field

    public ObjectStorageService(
            @Value("${oci.bucket.name}") String bucketName,
            @Value("${oci.namespace}") String namespace,
            @Value("${oci.region}") String region,
            @Value("${OCI_AUTH_USER:default-user}") String user,
            @Value("${OCI_AUTH_FINGERPRINT:default-fingerprint}") String fingerprint,
            @Value("${OCI_AUTH_KEY:default-key}") String privateKey,
            @Value("${OCI_AUTH_TENANCY:default-tenancy}") String tenancy) throws Exception {
        SimpleAuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
                .userId(user)
                .fingerprint(fingerprint)
                .privateKeySupplier(new StringPrivateKeySupplier(privateKey))
                .tenantId(tenancy)
                .build();
        this.client = ObjectStorageClient.builder()
                .region(region)
                .build(provider);
        this.bucketName = bucketName;
        this.namespace = namespace;
        this.region = region; // Store the region
    }

    public String uploadFile(MultipartFile file) throws Exception {
        String objectName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucketName(bucketName)
                    .namespaceName(namespace)
                    .objectName(objectName)
                    .contentLength(file.getSize())
                    .build();

            // Use the updated putObject method with the InputStream as the body
            PutObjectResponse response = client.putObject(
                    PutObjectRequest.builder()
                            .bucketName(bucketName)
                            .namespaceName(namespace)
                            .objectName(objectName)
                            .contentLength(file.getSize())
                            .build(),
                    inputStream
            );
        }

        // Use the stored region instead of calling getRegion()
        return String.format("https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                region, namespace, bucketName, objectName);
    }
}