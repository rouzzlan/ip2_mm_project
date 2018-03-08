package be.kdg.musicmaker.user;

import be.kdg.musicmaker.band.BandNotFoundException;
import be.kdg.musicmaker.band.BandRepository;
import be.kdg.musicmaker.band.BandService;
import be.kdg.musicmaker.event.EventNotFoundException;
import be.kdg.musicmaker.event.EventRepository;
import be.kdg.musicmaker.event.EventService;
import be.kdg.musicmaker.instrument.InstrumentRepository;
import be.kdg.musicmaker.instrument.InstrumentService;
import be.kdg.musicmaker.lesson.LessonService;
import be.kdg.musicmaker.lesson.LessonTypeDTO;
import be.kdg.musicmaker.libraries.musiclib.MusicLibraryService;
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
    Role leerling = new Role("ROLE_LEERLING");
    Role lesgever = new Role("ROLE_LESGEVER");
    Role beheerder = new Role("ROLE_BEHEERDER");

    //USERS
    User user = new User("user","user","user","user","user@user.com");
    User user2 = new User("user2", "user2", "user2", "user2", "user2@user.com");
    User user3 = new User("user3", "user3", "user3", "user3", "user3@user.com");

    //INSTRUMENTEN
    MusicInstrument instrument = new MusicInstrument("basgitaar", "elektrisch", InstrumentSort.SNAAR, "5 snaren");

    //BANDS
    Band band = new Band("The X-Nuts");

    private static final Logger LOG = LoggerFactory.getLogger(SeedData.class);
    @Autowired
    LessonService lessonService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    BandRepository bandRepository;
    @Autowired
    BandService bandService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventService eventService;
    @Autowired
    InstrumentRepository instrumentRepository;
    @Autowired
    InstrumentService instrumentService;
    @Autowired
    MusicLibraryService musicLibService;

    @EventListener
    public void seed(ContextRefreshedEvent event) throws IOException, URISyntaxException, BandNotFoundException, EventNotFoundException {
        seedRoles();
        seedUsers();
        seedInstruments();
        seedMuziekstukken();
        seedBands();
        seedEvents();
        seedLessonTypes();
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

            //SET ROLES AND ENABLE
            user.setRoles(Arrays.asList(leerling));
            user2.setRoles(Arrays.asList(leerling,lesgever));
            user3.setRoles(Arrays.asList(leerling,lesgever,beheerder));
            user.setEnabled(true);
            user2.setEnabled(true);
            user3.setEnabled(true);
            userService.createUser(user);
            userService.createUser(user2);
            userService.createUser(user3);

            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user.getUsername().toUpperCase(), user.getEmail(), user.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user2.getUsername().toUpperCase(), user2.getEmail(), user2.getPassword()));
            LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user3.getUsername().toUpperCase(), user3.getEmail(), user3.getPassword()));
        }
    }
    private void seedBands() {
        if (bandService.isBandEmpty()) {
            //CREATE BAND
            bandService.createBand(band);
            //SET TEACHER AND USERS
            band.setTeacher(user2);
            band.setStudents(Arrays.asList(user,user3));
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
            musicPiece1.setMusicClip(fileArray);
            musicPiece1.setFileName("Requiem-piano-mozart-lacrymosa.mp3");
            musicLibService.addMusicPiece(musicPiece1);

            LOG.info(String.format("%-6s ADDED ", musicPiece1.getTitle()));
        }
    }
    private void seedEvents() throws BandNotFoundException, EventNotFoundException {
        if (eventService.isEventEmpty()) {
            band = bandService.doesBandExist("The X-Nuts");

            String dateTimeString = "2018-06-03T12:30:00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

            Event event = new Event("SportPladijsje", dateTime , "Sportpaleis", band);
            eventService.createEvent(event);

            LOG.info(String.format("%-6s ADDED || date: %-15s || place: %-15s || band: %s", event.getName().toUpperCase(), event.getDateTime().toString(), event.getPlace(), event.getBand().getName()));

            Event testEvent = eventService.getEvent(new Long(1));
            System.out.println(testEvent.toString());
        }
    }
    private void seedLessonTypes() {
        lessonService.addLessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor beginners", "gitaar 1"));
        lessonService.addLessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor gevorderden", "gitaar 2"));
        lessonService.addLessonType(new LessonTypeDTO(15.50, "gitaar", "samenspel voor gitaar", "gitaar 3"));
        LOG.info(String.format("%-6s ADDED ", "LESTYPE: GITAAR 1"));
        LOG.info(String.format("%-6s ADDED ", "LESTYPE: GITAAR 2"));
        LOG.info(String.format("%-6s ADDED ", "LESTYPE: GITAAR 3"));
    }
}
