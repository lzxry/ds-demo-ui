package com.example.ds.controller;

import com.example.ds.common.Result;
import com.example.ds.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "文件存储", description = "提供文件上传、下载、删除等功能")
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @Operation(summary = "上传文件", description = "上传文件到指定目录")
    @PostMapping("/{directory}")
    @PreAuthorize("hasAuthority('storage:upload')")
    public Result<String> uploadFile(
            @Parameter(description = "目标目录", required = true)
            @PathVariable String directory,
            @Parameter(description = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        String path = storageService.store(file, directory);
        return Result.success(path);
    }

    @Operation(summary = "下载文件", description = "下载指定文件")
    @GetMapping("/{filename}")
    @PreAuthorize("hasAuthority('storage:download')")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "文件名", required = true)
            @PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @Operation(summary = "删除文件", description = "删除指定文件")
    @DeleteMapping("/{filename}")
    @PreAuthorize("hasAuthority('storage:delete')")
    public Result<Void> deleteFile(
            @Parameter(description = "文件名", required = true)
            @PathVariable String filename) {
        storageService.delete(filename);
        return Result.success();
    }

    @Operation(summary = "删除目录", description = "删除指定目录及其下所有文件")
    @DeleteMapping("/directory/{directory}")
    @PreAuthorize("hasAuthority('storage:delete')")
    public Result<Void> deleteDirectory(
            @Parameter(description = "目录名", required = true)
            @PathVariable String directory) {
        storageService.deleteDirectory(directory);
        return Result.success();
    }

    @Operation(summary = "列出文件", description = "列出指定目录下的所有文件")
    @GetMapping("/list/{directory}")
    @PreAuthorize("hasAuthority('storage:list')")
    public Result<List<String>> listFiles(
            @Parameter(description = "目录名", required = true)
            @PathVariable String directory) {
        List<String> files = storageService.listFiles(directory)
                .stream()
                .map(Path::toString)
                .collect(Collectors.toList());
        return Result.success(files);
    }
} 