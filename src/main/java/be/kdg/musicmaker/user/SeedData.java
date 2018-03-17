package be.kdg.musicmaker.user;

import be.kdg.musicmaker.band.BandNotFoundException;
import be.kdg.musicmaker.band.BandRepository;
import be.kdg.musicmaker.band.BandService;
import be.kdg.musicmaker.event.EventNotFoundException;
import be.kdg.musicmaker.event.EventRepository;
import be.kdg.musicmaker.event.EventService;
import be.kdg.musicmaker.instrument.InstrumentRepository;
import be.kdg.musicmaker.instrument.InstrumentService;
import be.kdg.musicmaker.lesson.dto.LessonDTO;
import be.kdg.musicmaker.lesson.LessonService;
import be.kdg.musicmaker.lesson.dto.LessonTypeDTO;
import be.kdg.musicmaker.libraries.musiclib.MusicLibraryService;
import be.kdg.musicmaker.libraries.musiclib.MusicPiece;
import be.kdg.musicmaker.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class SeedData {
    //ROLES
    private Role leerling = new Role("ROLE_LEERLING");
    private Role lesgever = new Role("ROLE_LESGEVER");
    private Role beheerder = new Role("ROLE_BEHEERDER");

    //USERS
    private User user = new User("user", "user", "user", "user", "user@user.com");
    private User user2 = new User("user2", "user2", "user2", "user2", "user2@user.com");
    private User user3 = new User("user3", "user3", "user3", "user3", "user3@user.com");
    private User user4 = new User("user4", "user4", "user4", "user4", "user4@user.com");
    private User user5 = new User("user5", "user5", "user5", "user5", "user5@user.com");
    private User user6 = new User("user6", "user6", "user6", "user6", "user6@user.com");
    private User user7 = new User("user7", "user7", "user7", "user7", "user7@user.com");
    private User user8 = new User("user8", "user8", "user8", "user8", "user8@user.com");
    private User user9 = new User("user9", "user9", "user9", "user9", "user9@user.com");

    //INSTRUMENTEN
    private MusicInstrument instrument = new MusicInstrument("basgitaar", "elektrisch", InstrumentSort.SNAAR, "5 snaren");

    //BANDS
    private Band band = new Band("The X-Nuts");

    //LESSONTYPES
    private LessonType lessonType = new LessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor nerds", "gitaar 0"));
    private LessonType lessonType1 = new LessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor beginners", "gitaar 1"));
    private LessonType lessonType2 = new LessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor gevorderden", "gitaar 2"));
    private LessonType lessonType3 = new LessonType(new LessonTypeDTO(15.50, "gitaar", "samenspel voor gitaar", "gitaar 3"));

    //LESSONS
    private Lesson lesson1 = new Lesson(new LessonDTO(60, 90.0, "", new Playlist(), lessonType1, new SeriesOfLessons(), LocalDateTime.now().toString()));
    private Lesson lesson2 = new Lesson(new LessonDTO(60, 90.0, "", new Playlist(), lessonType2, new SeriesOfLessons(), LocalDateTime.now().toString()));
    private Lesson lesson3 = new Lesson(new LessonDTO(60, 90.0, "", new Playlist(), lessonType3, new SeriesOfLessons(), LocalDateTime.now().toString()));
    private Lesson lesson4 = new Lesson(new LessonDTO(60, 50, "", new Playlist(), lessonType1, new SeriesOfLessons(), LocalDateTime.now().toString()));
    private Lesson lesson5 = new Lesson(new LessonDTO(60, 90.0, "", new Playlist(), lessonType2, new SeriesOfLessons(), LocalDateTime.now().toString()));
    private Lesson lesson6 = new Lesson(new LessonDTO(60, 90.0, "", new Playlist(), lessonType3, new SeriesOfLessons(), LocalDateTime.now().toString()));
    private Lesson lesson7 = new Lesson(new LessonDTO(60, 45, "", new Playlist(), lessonType1, new SeriesOfLessons(), LocalDateTime.now().toString()));
    private Lesson lesson8 = new Lesson(new LessonDTO(60, 90.0, "", new Playlist(), lessonType2, new SeriesOfLessons(), LocalDateTime.now().toString()));

    private static final Logger LOG = LoggerFactory.getLogger(SeedData.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private LessonService lessonService;
    private UserService userService;
    private BandService bandService;
    private EventService eventService;
    private InstrumentService instrumentService;
    private MusicLibraryService musicLibService;

    @Autowired
    public SeedData(LessonService lessonService,
                    UserService userService,
                    BandService bandService,
                    EventService eventService,
                    InstrumentService instrumentService,
                    MusicLibraryService musicLibService) {
        this.lessonService = lessonService;
        this.userService = userService;
        this.bandService = bandService;
        this.eventService = eventService;
        this.instrumentService = instrumentService;
        this.musicLibService = musicLibService;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) throws IOException, URISyntaxException, BandNotFoundException, EventNotFoundException {
        seedRoles();
        seedInstruments();
        seedMuziekstukken();
        seedUsers();
        seedBands();
        seedEvents();
        seedLessonTypes();
        seedLessons();
        addStudentsoLessons();
    }

    private void seedRoles() {
        if (userService.isRolesEmpty()) {
            userService.createRole(leerling);
            userService.createRole(lesgever);
            userService.createRole(beheerder);

            LOG.info(String.format("%-6s ADDED ", leerling.getName()));
            LOG.info(String.format("%-6s ADDED ", lesgever.getName()));
            LOG.info(String.format("%-6s ADDED ", beheerder.getName()));
        }
    }

    private void seedUsers() {
        if (userService.isUsersEmpty()) {
            //CREATE USERS
            userService.createUser(user);
            userService.createUser(user2);
            userService.createUser(user3);
            userService.createUser(user4);
            userService.createUser(user5);
            userService.createUser(user6);
            userService.createUser(user7);
            userService.createUser(user8);
            userService.createUser(user9);

            //SET ROLES AND ENABLE
            user.setRoles(Arrays.asList(leerling));
            user5.setRoles(Arrays.asList(leerling));
            user6.setRoles(Arrays.asList(leerling));
            user7.setRoles(Arrays.asList(leerling));
            user8.setRoles(Arrays.asList(leerling));
            user9.setRoles(Arrays.asList(leerling));
            user2.setRoles(Arrays.asList(leerling, lesgever));
            user4.setRoles(Arrays.asList(leerling, lesgever));
            user3.setRoles(Arrays.asList(leerling, lesgever, beheerder));
            user.setEnabled(true);
            user2.setEnabled(true);
            user3.setEnabled(true);
            userService.createUser(user);
            userService.createUser(user2);
            userService.createUser(user3);

            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user.getUsername().toUpperCase(), user.getEmail(), user.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user2.getUsername().toUpperCase(), user2.getEmail(), user2.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user3.getUsername().toUpperCase(), user3.getEmail(), user3.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user4.getUsername().toUpperCase(), user4.getEmail(), user4.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user5.getUsername().toUpperCase(), user5.getEmail(), user5.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user6.getUsername().toUpperCase(), user6.getEmail(), user6.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user7.getUsername().toUpperCase(), user7.getEmail(), user7.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user8.getUsername().toUpperCase(), user8.getEmail(), user8.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user9.getUsername().toUpperCase(), user9.getEmail(), user9.getPassword()));
        }
    }

    private void seedBands() {
        if (bandService.isBandEmpty()) {
            //CREATE BAND
            bandService.createBand(band);
            //SET TEACHER AND USERS
            band.setTeacher(user2);
            band.setStudents(Arrays.asList(user, user3));
            bandService.createBand(band);

            LOG.info(String.format("%-6s ADDED || teacher: %-15s || students: %s", band.getName().toUpperCase(), band.getTeacher(), band.getStudents().toString()));
        }
    }

    private void seedInstruments() {
        if (instrumentService.isInstrumentsEmpty()) {
            instrumentService.createInstrument(instrument);
            LOG.info(String.format("%-6s ADDED ", instrument.getName()));
        }
    }

    private void seedMuziekstukken() throws URISyntaxException, IOException {
        if (musicLibService.isMusicLibEmpty()) {
            ClassLoader classLoader = getClass().getClassLoader();
            File musicFile = new File(classLoader.getResource("musicFiles/Requiem-piano-mozart-lacrymosa.mp3").toURI());
            byte[] fileArray = Files.readAllBytes(musicFile.toPath());
            MusicPiece musicPiece1 = new MusicPiece();
            musicPiece1.setArtist("Mozart");
            musicPiece1.setTitle("Requiem piano Mozart. Lacrymosa, requiem in D minor, K 626 III sequence");
            musicPiece1.setMusicFile("Requiem-piano-mozart-lacrymosa.mp3", fileArray);
            musicLibService.addMusicPiece(musicPiece1);

            LOG.info(String.format("%-6s ADDED ", musicPiece1.getTitle()));
//
            // 2e musicfile
            musicFile = new File(classLoader.getResource("musicFiles/how_to_save_a_life_-_the_fray.mp3").toURI());
            fileArray = Files.readAllBytes(musicFile.toPath());
            musicPiece1 = new MusicPiece();
            musicPiece1.setArtist("The fray");
            musicPiece1.setTitle("How to save a life");
            musicPiece1.setMusicFile(musicFile.getName(), fileArray);
            File partituur = new File(classLoader.getResource("partituren/How_To_Save_A_Life_-_The_Fray.mxl").toURI());
            fileArray = Files.readAllBytes(partituur.toPath());
            musicPiece1.setPartituurFile(partituur.getName(), fileArray);

            musicLibService.addMusicPiece(musicPiece1);
//
//            LOG.info(String.format("%-6s ADDED ", musicPiece1.getTitle()));
        }

    }

    private void seedEvents() throws BandNotFoundException, EventNotFoundException {
        if (eventService.isEventEmpty()) {
            band = bandService.doesBandExist("The X-Nuts");

            String dateTimeString = "2018-06-03T12:30:00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

            Event event = new Event("SportPladijsje", dateTime, "Sportpaleis", band);
            eventService.createEvent(event);

            LOG.info(String.format("%-6s ADDED || date: %-15s || place: %-15s || band: %s", event.getName().toUpperCase(), event.getDateTime().toString(), event.getPlace(), event.getBand().getName()));

            Event testEvent = eventService.getEvent(new Long(1));
            System.out.println(testEvent.toString());
        }
    }

    private void seedLessonTypes() {
        lessonService.addLessonType(lessonType);
        lessonService.addLessonType(lessonType1);
        lessonService.addLessonType(lessonType2);
        lessonService.addLessonType(lessonType3);
        LOG.info(String.format("%-6s ADDED ", "LESTYPE: GITAAR 1"));
        LOG.info(String.format("%-6s ADDED ", "LESTYPE: GITAAR 2"));
        LOG.info(String.format("%-6s ADDED ", "LESTYPE: GITAAR 3"));
    }

    private void seedLessons() {
        lessonService.addLesson(lesson1);
        lessonService.addLesson(lesson2);
        lessonService.addLesson(lesson3);
        lessonService.addLesson(lesson4);
        lessonService.addLesson(lesson5);
        lessonService.addLesson(lesson6);
        lessonService.addLesson(lesson7);
        lessonService.addLesson(lesson8);
        LOG.info(String.format("%-6s ADDED ", "LES 1"));
        LOG.info(String.format("%-6s ADDED ", "LES 2"));
        LOG.info(String.format("%-6s ADDED ", "LES 3"));
        LOG.info(String.format("%-6s ADDED ", "LES 4"));
        LOG.info(String.format("%-6s ADDED ", "LES 5"));
        LOG.info(String.format("%-6s ADDED ", "LES 6"));
        LOG.info(String.format("%-6s ADDED ", "LES 7"));
        LOG.info(String.format("%-6s ADDED ", "LES 8"));
    }

    private void addStudentsoLessons() {
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson1));
        lessonService.addStudentToLesson(new Attender("leerling", user2, lesson2));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson3));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson4));
        lessonService.addStudentToLesson(new Attender("leerling", user2, lesson5));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson6));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson7));
        lessonService.addStudentToLesson(new Attender("leerling", user2, lesson8));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson1));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson2));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson3));
        lessonService.addStudentToLesson(new Attender("leerling", user2, lesson4));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson5));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson6));
        lessonService.addStudentToLesson(new Attender("leerling", user2, lesson7));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson8));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson1));
        lessonService.addStudentToLesson(new Attender("leerling", user2, lesson7));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson8));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson3));

        LOG.info(String.format("%-6s ADDED TO LESSONS", "STUDENTS"));


        lessonService.addStudentToLesson(new Attender("lesgever", user2, lesson1));
        lessonService.addStudentToLesson(new Attender("lesgever", user2, lesson2));
        lessonService.addStudentToLesson(new Attender("lesgever", user2, lesson3));
        lessonService.addStudentToLesson(new Attender("lesgever", user3, lesson4));
        lessonService.addStudentToLesson(new Attender("lesgever", user3, lesson5));
        lessonService.addStudentToLesson(new Attender("lesgever", user3, lesson6));
        lessonService.addStudentToLesson(new Attender("lesgever", user3, lesson7));
        lessonService.addStudentToLesson(new Attender("lesgever", user3, lesson8));

        LOG.info(String.format("%-6s ADDED TO LESSONS", "TEACHERS"));
    }
}
