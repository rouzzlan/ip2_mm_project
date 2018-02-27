package be.kdg.musicmaker.libraries.musiclib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicLibraryService {
    @Autowired
    MusicLibraryRepository musicLibraryRepository;

    public void addMusicPiece(MusicPiece musicPiece) {
        musicLibraryRepository.save(musicPiece);
    }

    public List<MusicPiece> getMusicPiecesByTitle(String title) {
        return musicLibraryRepository.findByTitle(title);
    }

    public MusicPiece getMusicPieceById(Long id) {
        return musicLibraryRepository.findOne(id);
    }
}
