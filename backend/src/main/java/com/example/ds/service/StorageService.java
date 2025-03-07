package com.example.ds.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    /**
     * 存储文件
     */
    String store(MultipartFile file, String directory);
    
    /**
     * 加载文件
     */
    Resource loadAsResource(String filename);
    
    /**
     * 删除文件
     */
    void delete(String filename);
    
    /**
     * 清理目录
     */
    void deleteDirectory(String directory);
    
    /**
     * 列出目录下的文件
     */
    List<Path> listFiles(String directory);
    
    /**
     * 获取文件的绝对路径
     */
    Path getAbsolutePath(String filename);
    
    /**
     * 检查文件是否存在
     */
    boolean exists(String filename);
} 