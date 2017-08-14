package ru.iteco.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.WebDavStorageService;
import ru.iteco.ZipUtility;
import ru.iteco.game.web.GameVersionCreateForm;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
public class GameVersionServiceImpl implements GameVersionService {
    private final GameVersionRepository gameVersionRepository;
    private final WebDavStorageService webDavStorageService;
    private final ZipUtility zipUtility;

    @Autowired
    public GameVersionServiceImpl(GameVersionRepository gameVersionRepository,
                                  WebDavStorageService webDavStorageService,
                                  ZipUtility zipUtility) {
        this.gameVersionRepository = gameVersionRepository;
        this.webDavStorageService = webDavStorageService;
        this.zipUtility = zipUtility;
    }

    @Override
    public GameVersion create(GameVersionCreateForm form) {
        Game game = form.getGame();
        GameVersion gameVersion = gameVersionRepository.findFirstByGameOrderByBuildDesc(game);
        Integer build = (gameVersion == null) ? 1 : gameVersion.getBuild() + 1;
        gameVersion = new GameVersion();
        gameVersion.setBuild(build);
        gameVersion.setGameFk(game.getId());
        gameVersion.setWhatNew(form.getWhatNew());
        Boolean isRemote = !form.getUri().isEmpty();
        gameVersion.setRemote(isRemote);
        if (isRemote) {
            gameVersion.setUri(form.getUri());
        }
        else {
            MultipartFile archive = form.getArchive();
            String uploadPrefix = makeUploadPrefix(gameVersion);
            try {
                InputStream archiveStream = archive.getInputStream();
                List<String> storageFiles = unpackAndSaveFilesToStorage(archiveStream, uploadPrefix);
                String entryFile = findEntryHtmlFile(storageFiles);
                gameVersion.setUri(entryFile);

            } catch (IOException e) {
            }
        }
        return gameVersionRepository.save(gameVersion);
    }

    private List<String> unpackAndSaveFilesToStorage(InputStream inputStream, String prefix) throws IOException {
        List<String> storageFilenames = new ArrayList<>();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry entry = zipInputStream.getNextEntry();
        while(entry != null) {
            if(!entry.isDirectory()) {
                byte[] content = zipUtility.unpackFile(zipInputStream);
                String filename = entry.getName();
                String storageFilename = webDavStorageService.store(filename, content, prefix);
                storageFilenames.add(storageFilename);
            }
            zipInputStream.closeEntry();
            entry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        return storageFilenames;
    }

    private String makeUploadPrefix(@NotNull GameVersion gameVersion) {
        return GameVersionCreateForm.UPLOAD_ARCHIVE_PATH + gameVersion.getGameFk() +
                "/build/" + gameVersion.getBuild() + "/";
    }

    private String findEntryHtmlFile(List<String> files) {
        String entryFile = "";
        for(String file: files) {
            if(file.contains("index.html")) {
                entryFile = file;
                break;
            }
        }
        return entryFile;
    }
}
