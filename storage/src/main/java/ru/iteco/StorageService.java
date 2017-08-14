package ru.iteco;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file, String prefix) throws StorageException;

    String store(MultipartFile file) throws StorageException;

    String store(String filename, byte[] bytes, String prefix) throws StorageException;

    Resource load(String url) throws StorageException;

    String thumbnail(MultipartFile file, int width, int height, String prefix);

    String thumbnail(MultipartFile file, int width, int height);

    byte[] resize(MultipartFile file, int width, int height);
}
