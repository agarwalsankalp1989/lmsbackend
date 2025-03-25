package com.scholarbridge.lms.service;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ObjectStorageService {
    private final ObjectStorageClient client;
    private final String bucketName;
    private final String namespace;

    public ObjectStorageService(
            @Value("${oci.bucket.name}") String bucketName,
            @Value("${oci.namespace}") String namespace,
            @Value("${oci.region}") String region) throws Exception {
        ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();
        ConfigFileAuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configFile);
        this.client = ObjectStorageClient.builder()
                .region(region)
                .build(provider);
        this.bucketName = bucketName;
        this.namespace = namespace;
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
            client.putObject(request, inputStream);
        }
        return String.format("https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                client.getRegion().getRegionId(), namespace, bucketName, objectName);
    }
}