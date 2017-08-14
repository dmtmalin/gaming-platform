package ru.iteco;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class WebDavStorageService implements StorageService {

    private final String rootUrl;
    private final Sardine sardine;

    public WebDavStorageService(StorageProperties properties) {
        this.rootUrl = properties.getRootUrl();
        this.sardine = SardineFactory.begin();
    }

    @Override
    public String store(MultipartFile file) throws StorageException {
        return store(file, "");
    }

    @Override
    public String store(MultipartFile file, String prefix) throws StorageException {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        }
        catch (IOException e) {
            throw new StorageException("Cannot read for store file: " + file.getName(), e);
        }
        String filename = file.getOriginalFilename();
        return store(filename, bytes, prefix);
    }

    @Override
    public String store(String filename, byte[] bytes, String prefix) throws StorageException {
        String url = rootUrl + prefix + filename;
        try {
            int copy = 0;
            while (sardine.exists(url)) {
                copy += 1;
                String copyFilename = getCopiesFilename(filename, copy);
                url = rootUrl + prefix + copyFilename;
            }
            sardine.put(url, bytes);
        }
        catch (IOException e) {
            throw new StorageException("Cannot store file: " + url, e);
        }
        return prefix + filename;
    }

    @Override
    public String thumbnail(MultipartFile file, int width, int height) throws StorageException {
        return thumbnail(file, width, height, "");
    }

    @Override
    public String thumbnail(MultipartFile file, int width, int height, String prefix)
            throws StorageException {
        byte[] thumbnailBytes = resize(file, width, height);
        String originalFilename = file.getOriginalFilename();
        String filename = getThumbnailFilename(originalFilename, width, height);
        return store(filename, thumbnailBytes, prefix);
    }

    @Override
    public byte[] resize(MultipartFile file, int width, int height)
            throws StorageException {
        byte[] thumbnailBytes;
        try {
            InputStream stream = file.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(stream)
                    .size(width, height)
                    .toOutputStream(outputStream);
            thumbnailBytes = outputStream.toByteArray();
            outputStream.close();
        }
        catch (IOException e) {
            throw new StorageException("Cannot thumbnail file: " + file.getName());
        }
        return thumbnailBytes;
    }

    @Override
    public Resource load(String url) throws StorageException {
        url = rootUrl + url;
        try {
            InputStream stream = sardine.get(url);
            return new InputStreamResource(stream);
        }
        catch (IOException e) {
            throw new StorageException("Cannot get file: " + url, e);
        }
    }

    private String getThumbnailFilename(String filename, int width, int height) {
        String extension = FilenameUtils.getExtension(filename);
        String baseName = FilenameUtils.getBaseName(filename);
        return String.format("%s_%s_%s.%s", baseName, width, height, extension);
    }

    private String getCopiesFilename(String filename, int copy) {
        String extension = FilenameUtils.getExtension(filename);
        String baseName = FilenameUtils.getBaseName(filename);
        return String.format("%s%s.%s", baseName, copy, extension);
    }
}
