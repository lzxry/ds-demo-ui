package com.example.ds.service.impl;

import com.example.ds.config.StorageConfig;
import com.example.ds.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageConfig storageConfig;

    @Override
    public String store(MultipartFile file, String directory) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("文件为空");
            }
            
            // 检查文件大小
            if (file.getSize() > storageConfig.getMaxFileSize()) {
                throw new RuntimeException("文件大小超过限制");
            }
            
            // 检查文件扩展名
            String extension = "." + FilenameUtils.getExtension(file.getOriginalFilename());
            if (!Arrays.asList(storageConfig.getAllowedExtensions()).contains(extension.toLowerCase())) {
                throw new RuntimeException("不支持的文件类型");
            }
            
            // 生成唯一文件名
            String filename = UUID.randomUUID().toString() + extension;
            
            // 创建目录
            Path dirPath = Paths.get(storageConfig.getBaseDir(), directory);
            Files.createDirectories(dirPath);
            
            // 保存文件
            Path targetPath = dirPath.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // 返回相对路径
            return directory + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("文件存储失败", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = getAbsolutePath(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("文件不存在或无法读取");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("文件读取失败", e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = getAbsolutePath(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }

    @Override
    public void deleteDirectory(String directory) {
        try {
            Path dirPath = Paths.get(storageConfig.getBaseDir(), directory);
            FileSystemUtils.deleteRecursively(dirPath);
        } catch (IOException e) {
            throw new RuntimeException("目录删除失败", e);
        }
    }

    @Override
    public List<Path> listFiles(String directory) {
        try {
            Path dirPath = Paths.get(storageConfig.getBaseDir(), directory);
            return Files.list(dirPath).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("目录读取失败", e);
        }
    }

    @Override
    public Path getAbsolutePath(String filename) {
        return Paths.get(storageConfig.getBaseDir()).resolve(filename);
    }

    @Override
    public boolean exists(String filename) {
        Path file = getAbsolutePath(filename);
        return Files.exists(file);
    }
} 