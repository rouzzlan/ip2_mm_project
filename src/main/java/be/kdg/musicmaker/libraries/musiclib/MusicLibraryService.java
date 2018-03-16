package be.kdg.musicmaker.libraries.musiclib;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MusicLibraryService {

    private MusicLibraryRepository musicLibraryRepository;

    private LanguagesRepository languagesRepository;
    @Autowired
    public MusicLibraryService(MusicLibraryRepository musicLibraryRepository, LanguagesRepository languagesRepository) {
        this.musicLibraryRepository = musicLibraryRepository;
        this.languagesRepository = languagesRepository;
    }

    public void addMusicPiece(MusicPiece musicPiece) {
        musicLibraryRepository.save(musicPiece);
    }
    public long addMusicPiece(MusicPieceDTO musicPieceDTO) {
        MusicPiece mp = map(musicPieceDTO, MusicPiece.class);
        return musicLibraryRepository.save(mp).getId();
    }

    public void addMusicPiece(MusicPieceDTO musicPiece, MultipartFile file) throws IOException {
        MusicPiece mp = map(musicPiece, MusicPiece.class);
        mp.setMusicFile(file.getOriginalFilename(), file.getBytes());
//        mp.setLanguage(getLanguage(musicPiece.getLanguage()));
        musicLibraryRepository.save(mp);
    }

    public void addMusicPiece(MusicPieceDTO musicPiece, MultipartFile musicFile, MultipartFile partituur) throws IOException {
        MusicPiece mp = map(musicPiece, MusicPiece.class);
        mp.setMusicFile(musicFile.getOriginalFilename(), musicFile.getBytes());
        mp.setPartituurFile(partituur.getOriginalFilename(), partituur.getBytes());
//        mp.setLanguage(getLanguage(musicPiece.getLanguage()));
        musicLibraryRepository.save(mp);
    }

    public List<MusicPiece> getMusicPiecesByTitle(String title) {
        return musicLibraryRepository.findByTitle(title);
    }

    public MusicPieceDTO getMusicPieceDTOById(Long id) {
        MusicPiece musicPiece = musicLibraryRepository.findOne(id);
        return map(musicPiece, MusicPieceDTO.class);
    }

    public MusicPiece getMusicPiecesById(Long id) {
        return musicLibraryRepository.findOne(id);
    }


    public Collection<MusicPieceDTO> getMusicPieces() {
        List<MusicPiece> musicPieces = musicLibraryRepository.findAll();
        List<MusicPieceDTO> dtoMusicPieces = new ArrayList<>(musicPieces.size());
        for (MusicPiece musicPiece : musicPieces) {
            MusicPieceDTO mp = map(musicPiece, MusicPieceDTO.class);
            dtoMusicPieces.add(mp);
        }
        return dtoMusicPieces;
    }

    public boolean isMusicLibEmpty() {
        return musicLibraryRepository.count() == 0;
    }


    public File getPartituur(Long id) throws IOException {
        MusicPiece mp = musicLibraryRepository.findOne(id);
        File file = new File(mp.getPartituurFileName());
        FileUtils.writeByteArrayToFile(file, mp.getPartiturBinary());
        file.deleteOnExit();
        return file;
    }

    public void addMusicPieceFile(Long id, MultipartFile file) throws IOException {
        MusicPiece mp = musicLibraryRepository.findOne(id);
        mp.setMusicFile(file.getOriginalFilename(), file.getBytes());
        musicLibraryRepository.save(mp);
    }

    public void deleteMusicPiece(Long id) throws ResouceNotFoundException {
        if (musicLibraryRepository.exists(id)) {
            musicLibraryRepository.delete(id);
        } else {
            throw new ResouceNotFoundException("Music piece does not exist");
        }
    }

    public void update(MusicPieceDTO musicPieceDTO, Long id) {
        MusicPiece musicPiece = musicLibraryRepository.getOne(id);
        mapDTO(musicPieceDTO, musicPiece);
        musicLibraryRepository.save(musicPiece);
    }

    public void updateLanguageList(List<Language> languages) {
        languagesRepository.save(languages);
    }

    public Language getLanguage(String language) {
        return languagesRepository.getLanguageByLanguageName(language);
    }

    public Language getLanguage(Long id) {
        return languagesRepository.findOne(id);
    }

    private void mapDTO(MusicPieceDTO dtoObject, MusicPiece object) {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(MusicPieceDTO.class, MusicPiece.class).
                mapNulls(false)
                .exclude("id")
                .exclude("language")
                .byDefault()
                .register();
        mapperFactory.getMapperFacade().map(dtoObject, object);
    }

    private <S, D> D map(S s, Class<D> type) {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(MusicPiece.class, MusicPieceDTO.class).
                mapNulls(false).
                mapNullsInReverse(false)
                .exclude("language")
                .byDefault()
                .register();
        return mapperFactory.getMapperFacade().map(s, type);
    }
}
