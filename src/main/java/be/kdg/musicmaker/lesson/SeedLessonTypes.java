package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.Instrument.InstrumentService;
import be.kdg.musicmaker.model.InstrumentSort;
import be.kdg.musicmaker.model.MusicInstrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class SeedLessonTypes {
    @Autowired
    LessonService lessonService;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seed();
    }

    private void seed() {
        lessonService.addLessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor beginners", "gitaar 1"));
        lessonService.addLessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor gevorderden", "gitaar 2"));
        lessonService.addLessonType(new LessonTypeDTO(15.50, "gitaar", "samenspel voor gitaar", "gitaar 3"));
    }
}
