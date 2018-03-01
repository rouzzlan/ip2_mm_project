package be.kdg.musicmaker.libraries.musiclib;

import be.kdg.musicmaker.libraries.musiclib.dto.MusicPieceGetDTO;
import be.kdg.musicmaker.libraries.musiclib.dto.MusicPiecePostDTO;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.model.DTO.UserDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MusicLibraryService {
    @Autowired
    MusicLibraryRepository musicLibraryRepository;
    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public void addMusicPiece(MusicPiece musicPiece) {
        musicLibraryRepository.save(musicPiece);
    }
    public void addMusicPiece(MusicPiecePostDTO musicPiece) throws IOException {
        mapperFactory.classMap(MusicPiecePostDTO.class, MusicPiece.class).exclude("musicClip");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        MusicPiece mp = mapperFacade.map(musicPiece, MusicPiece.class);
        mp.setMusicClip(musicPiece.getMusicFile().getBytes());
        musicLibraryRepository.save(mp);
    }

    public List<MusicPiece> getMusicPiecesByTitle(String title) {
        return musicLibraryRepository.findByTitle(title);
    }

    public MusicPiece getMusicPieceById(Long id) {
        return musicLibraryRepository.findOne(id);
    }


    public Collection<MusicPieceGetDTO> getMusicPieces() {
        mapperFactory.classMap(MusicPiece.class, MusicPieceGetDTO.class).exclude("musicClip");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        List<MusicPiece> musicPieces = musicLibraryRepository.findAll();
        List<MusicPieceGetDTO> dtoMusicPieces = new ArrayList<>(musicPieces.size());
        for (MusicPiece musicPiece : musicPieces){
            MusicPieceGetDTO mp = mapperFacade.map(musicPiece, MusicPieceGetDTO.class);
            dtoMusicPieces.add(mp);
        }
        return dtoMusicPieces;
    }
}
