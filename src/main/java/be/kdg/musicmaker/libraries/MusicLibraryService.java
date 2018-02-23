package be.kdg.musicmaker.libraries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicLibraryService {
    @Autowired
    MusicLibraryRepository musicLibraryRepository;
    public void addMusicPiece(MusicPiece musicPiece){
        musicLibraryRepository.save(musicPiece);
    }
    public List<MusicPiece> getMusicPiece(String title){
        return musicLibraryRepository.findByTitle(title);
    }
}
