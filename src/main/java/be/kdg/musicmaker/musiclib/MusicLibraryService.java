package be.kdg.musicmaker.musiclib;

import be.kdg.musicmaker.band.BandNotFoundException;
import be.kdg.musicmaker.instrument.dto.InstrumentDTO;
import be.kdg.musicmaker.model.Instrument;
import be.kdg.musicmaker.model.Language;
import be.kdg.musicmaker.model.MusicPiece;
import be.kdg.musicmaker.model.MusicPieceRating;
import be.kdg.musicmaker.musiclib.dto.MusicPieceDTO;
import be.kdg.musicmaker.musiclib.dto.RatingDTO;
import be.kdg.musicmaker.musiclib.repo.LanguagesRepository;
import be.kdg.musicmaker.musiclib.repo.MusicLibraryRatingRepository;
import be.kdg.musicmaker.musiclib.repo.MusicLibraryRepository;
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
    @Autowired
    private MusicLibraryRepository musicLibraryRepository;
    @Autowired
    private MusicLibraryRatingRepository musicRatingRepository;
    @Autowired
    private LanguagesRepository languagesRepository;

    //ADD
    public void addMusicPiece(MusicPiece musicPiece) {
        musicLibraryRepository.save(musicPiece);
    }

    public MusicPiece addMusicPiece(MusicPieceDTO musicPiece) {
        MusicPiece mp= map(musicPiece, MusicPiece.class);
        musicLibraryRepository.save(mp);
        return mp;
    }
    public void addMusicPieceEnPartituur(MusicPieceDTO musicPieceDTO, MultipartFile partituur) throws IOException {
        MusicPiece mp = map(musicPieceDTO, MusicPiece.class);
        mp.setPartituurFile(partituur.getOriginalFilename(), partituur.getBytes());
        musicLibraryRepository.save(mp);
    }

    public void addMusicPieceEnMusicFile(MusicPieceDTO musicPieceDTO, MultipartFile musicFile) throws IOException {
        MusicPiece mp = map(musicPieceDTO, MusicPiece.class);
        mp.setMusicFile(musicFile.getOriginalFilename(), musicFile.getBytes());
        musicLibraryRepository.save(mp);
    }

    public void addMusicPieceFull(MusicPieceDTO musicPieceDTO, MultipartFile musicFile, MultipartFile partituur) throws IOException {
        MusicPiece mp = map(musicPieceDTO, MusicPiece.class);
        mp.setMusicFile(musicFile.getOriginalFilename(), musicFile.getBytes());
        mp.setPartituurFile(partituur.getOriginalFilename(), partituur.getBytes());
        musicLibraryRepository.save(mp);
    }

    public void addMusicPiece(MusicPieceDTO musicPiece, MultipartFile file) throws IOException {
        MusicPiece mp = map(musicPiece, MusicPiece.class);
        mp.setMusicFile(file.getOriginalFilename(), file.getBytes());
        musicLibraryRepository.save(mp);
    }

    public void addMusicPiece(MusicPieceDTO musicPiece, MultipartFile musicFile, MultipartFile partituur) throws IOException {
        MusicPiece mp = map(musicPiece, MusicPiece.class);
        mp.setMusicFile(musicFile.getOriginalFilename(), musicFile.getBytes());
        mp.setPartituurFile(partituur.getOriginalFilename(), partituur.getBytes());
//        mp.setLanguage(getLanguage(musicPiece.getLanguage()));
        musicLibraryRepository.save(mp);
    }

    public long createMusicPiece(MusicPieceDTO musicPieceDTO) {
        MusicPiece mp = map(musicPieceDTO, MusicPiece.class);
        return musicLibraryRepository.save(mp).getId();
    }

    //VIEW
    public List<MusicPiece> getMusicPiecesByTitle(String title) {
        return musicLibraryRepository.findByTitle(title);
    }

    public MusicPieceDTO getMusicPieceDTOById(Long mpId){
        MusicPiece musicPieces = musicLibraryRepository.findOne(mpId);
        MusicPieceDTO musicPieceDTO = new MusicPieceDTO();
        musicPieceDTO = map(musicPieces, MusicPieceDTO.class);
        return musicPieceDTO;
        /*List<MusicPiece> musicPieces = musicLibraryRepository.findAll();
        MusicPieceDTO musicPieceDTO = new MusicPieceDTO();
        for (MusicPiece musicPiece : musicPieces) {
            if(musicPiece.getId() == mpId){
                musicPieceDTO = map(musicPiece, MusicPieceDTO.class);
              //  musicPieceDTO.setMyRating(getYourRating(mpId, userId).getRating());
                musicPieceDTO.setRating(getAllMprs(mpId));
                musicPieceDTO.setLanguage(musicPiece.getLanguage().getLanguageName());
            }
        }
       return musicPieceDTO;*/
    }

    public MusicPiece getMusicPiecesById(Long id) {
        return musicLibraryRepository.findOne(id);
    }

    /**
     * public List<MusicPiece> getMusicPieces() {
     return  musicLibraryRepository.findAll();
     }     */
    public List<MusicPieceDTO> getMusicPieces() {
        List<MusicPiece> musicPieces = musicLibraryRepository.findAll();
        List<MusicPieceDTO> dtoMusicPieces = new ArrayList<>(musicPieces.size());
        for (MusicPiece musicPiece : musicPieces) {
            MusicPieceDTO mp = map(musicPiece, MusicPieceDTO.class);
            dtoMusicPieces.add(mp);
        }
        return dtoMusicPieces;
    }


    //EDIT
    public void update(MusicPieceDTO musicPieceDTO, Long id) {
        MusicPiece musicPiece = musicLibraryRepository.getOne(id);
        mapDTO(musicPieceDTO, musicPiece);
        musicLibraryRepository.save(musicPiece);
    }

    public void updateRating(RatingDTO ratingDTO) {
        MusicPieceRating rating = musicRatingRepository.getOne(ratingDTO.getId());
        musicRatingRepository.save(rating);
    }

    //RATING
    public List<RatingDTO> getRatings(Long id) {
        List<MusicPieceRating> ratings = musicRatingRepository.findAllratings(id);
        List<RatingDTO> list = new ArrayList<>();
        for(MusicPieceRating mpr: ratings){
            RatingDTO dto =map(mpr, RatingDTO.class);
            list.add(dto);

        }
        return list;
    }
    private Double getAverage(Collection<MusicPieceRating> mprs) {
        Double count = 0.0;
        for(MusicPieceRating mpr: mprs){
            count += mpr.getRating();
        }
        return count/ mprs.size();
    }

    //OTHER
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

  /*  public void deleteMusicPiece(Long id) throws ResouceNotFoundException {
        if (musicLibraryRepository.exists(id)) {
            musicLibraryRepository.delete(id);
        } else {
            throw new ResouceNotFoundException("Music piece does not exist");
        }
    }*/


    public boolean isLanguagesEmpty() {
        return musicLibraryRepository.count() == 0;
    }

    public Language getLanguage(String language) {
        return languagesRepository.findLanguageByLanguageName(language);
    }

    public void updateLanguageList(List<Language> languages) {
        languagesRepository.save(languages);
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
                .byDefault()
                .register();
        return mapperFactory.getMapperFacade().map(s, type);
    }


    public void addRating(MusicPieceRating mpr) {
        musicRatingRepository.save(mpr);
    }
    public MusicPieceRating addRating(RatingDTO dto) {
        MusicPieceRating mpr = map(dto, MusicPieceRating.class);
        musicRatingRepository.save(mpr);
        return mpr;
    }

    public MusicPieceDTO getMusicPieceByTitle(String musicPieceName) {
        MusicPiece musicPiece = musicLibraryRepository.getMusicPieceByTitle(musicPieceName);
        return map(musicPiece, MusicPieceDTO.class);
    }
}
