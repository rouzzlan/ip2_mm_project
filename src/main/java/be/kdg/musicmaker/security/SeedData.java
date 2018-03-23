package be.kdg.musicmaker.security;

import be.kdg.musicmaker.band.BandNotFoundException;
import be.kdg.musicmaker.band.BandService;
import be.kdg.musicmaker.event.EventNotFoundException;
import be.kdg.musicmaker.event.EventService;
import be.kdg.musicmaker.instrument.InstrumentService;
import be.kdg.musicmaker.lesson.dto.LessonDTO;
import be.kdg.musicmaker.lesson.LessonService;
import be.kdg.musicmaker.lesson.dto.LessonTypeDTO;
import be.kdg.musicmaker.musiclib.MusicLibraryService;
import be.kdg.musicmaker.model.MusicPiece;
import be.kdg.musicmaker.model.*;
import be.kdg.musicmaker.musiclib.util.LanguagesImporter;
import be.kdg.musicmaker.user.UserService;
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
import java.util.List;

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
    private Instrument instrument = new Instrument("basgitaar", "elektrisch", InstrumentSort.SNAAR, "5 snaren");
    private Instrument instrument1 = new Instrument("viool", "klassiek", InstrumentSort.SNAAR, "4 snaren");
    private Instrument instrument2 = new Instrument("trombone", "brass", InstrumentSort.BLAAS, "ook gekend als een schuiftrompet");
    private Instrument instrument3 = new Instrument("gitaar", "klassiek", InstrumentSort.SNAAR, "6 snaren");
    private Instrument instrument4 = new Instrument("ukelele", "klassiek", InstrumentSort.SNAAR, "5 snaren");

    //MUSICPIECES
/*    private MusicPiece musicpiece = new MusicPiece("Sweet Home Alabama", "Lynyrd Skynyrd", l, "Love",    " [D]Big[Cadd9] wheels [G]keep on turning\n[D]Carry [Cadd9] me home [G]to see my kin\n[D]Singing[Cadd9]  songs [G]about the south land\n[D]I miss [Cadd9] 'ole' [G]'bamy once again and I think it's a sin\n\n[D] [Cadd9] [G]\n[D] [Cadd9] [G]\n\n[D]Well I heard [Cadd9] Mr. Young [G]sing about her\n[D]Well I heard [Cadd9] old Neil [G]put her down\n[D]Well I hope Neil[Cadd9]  [G]Young will remember\n[D]A southern[Cadd9]  man don’t [G]need him around, anyhow\n\n[D]Sweet [Cadd9] home Alabama[D], [Cadd9] where [G]the skies are so blue\n[D]Sweet [Cadd9] home Alabama, lord I’m [Cadd9] coming [G]home to you.\n\n[F] [C]\n[D] [Cadd9] [G]\n[D] [Cadd9] [G]\n\n[D]In Birmin[Cadd9] gham they [G]love the [F]Gov'nor, [C]boo-hoo[D]-hoo\n[D]Now we all [Cadd9] did what [G]we could do\n[D]Now water[Cadd9] gate [G]doesn't bother me\n[D]Does you [Cadd9] conscience [G]bother you, (now tell the truth!)\n\n[D]Sweet [Cadd9] home [G]Alabama[D], where the skies [G]are so blue\n[D]Sweet [Cadd9] home [G]Alabama[D], lord I’m coming [G]home to you. Here I come\n\n[D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G]\n   [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G]\n\n[D]Now [Cadd9]Muscle Shoals has got the[G] Swappers\n[D] And they've been known to pick a song or [G]two (yes we do)\n[D]Lord they get me off so [G]much\n[D]They pick me up when I'm feeling [G]blue, Now how about you?\n\n[D]Sweet [Cadd9]home [D]Alab[G]ama, where the[Cadd9] skies are so [G]blue\n[D]Sweet [Cadd9]home [D]Alab[G]ama, lord I’m[Cadd9] coming home to [G]you\n\n[D]Sweet [Cadd9]home Alab[G]ama (Oh sweet home baby)\n[D]Where the [Cadd9]skies are so [G]blue (And the governor's true)\n[D]Sweet [Cadd9]Home Alab[G]ama, (Lord, yeah)\n[D]Lord, I'm [Cadd9]coming home to [G]you\n\n[D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G]", "rock", "gitaarakkoorden", "https://www.youtube.com/watch?v=2fntQxhkht4","akkoord","easy",null,null);
    private MusicPiece musicpiece1 = new MusicPiece("Havana","Camilla Cabello", l,"Longing", "test[a]", "Pop","gitaarakkoorden", "https://www.youtube.com/watch?v=6tEZoZOh8MM", "akkoord","easy",null,null);
    private MusicPiece musicpiece2 = new MusicPiece("You'll be in my heart", "Phil Collins", l, "Disney",null, "Pop","pianoakkoorden", null, "muziekstuk","average","../../../../../../resources/musicFiles/Youll_Be_In_My_Heart.mp3","../../../../../../resources/musicFiles/Youll_Be_In_My_Heart.mxl");
   private MusicPiece musicpiece3 = new MusicPiece();
    private MusicPiece musicpiece4 = new MusicPiece();
    private MusicPiece musicpiece5 = new MusicPiece();
    private MusicPiece musicpiece6 = new MusicPiece();*/





    //BANDS
    private Band band = new Band("The X-Nuts");
    private Band band1 = new Band("Band1");
    private Band band2 = new Band("Band2");

    //EVENTS
    private Event event = new Event("SportPladijsje", null, "Sportpaleis", band);
    private Event event1 = new Event("event1", null, "Lotto Arena", band);
    private Event event2 = new Event("event2", null, "Prins Bouwdesijnstadion", band);
    private Event event3 = new Event("event3", null, "Sportpaleis", band);


    //LESSONTYPES
    private LessonType lessonType = new LessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor nerds", "gitaar 0"));
    private LessonType lessonType1 = new LessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor beginners", "gitaar 1"));
    private LessonType lessonType2 = new LessonType(new LessonTypeDTO(15.50, "gitaar", "gitaar voor gevorderden", "gitaar 2"));
    private LessonType lessonType3 = new LessonType(new LessonTypeDTO(15.50, "gitaar", "samenspel voor gitaar", "gitaar 3"));

    //LESSONS
    private Lesson lesson1 = new Lesson(new LessonDTO(60, 90.0, "open", "", LocalDateTime.now().toString()));
    private Lesson lesson2 = new Lesson(new LessonDTO(60, 90.0, "open", "", LocalDateTime.now().toString()));
    private Lesson lesson3 = new Lesson(new LessonDTO(60, 90.0, "open", "", LocalDateTime.now().toString()));
    private Lesson lesson4 = new Lesson(new LessonDTO(60, 50, "open", "", LocalDateTime.now().toString()));
    private Lesson lesson5 = new Lesson(new LessonDTO(60, 90.0, "open", "", LocalDateTime.now().toString()));
    private Lesson lesson6 = new Lesson(new LessonDTO(60, 90.0, "open", "", LocalDateTime.now().toString()));
    private Lesson lesson7 = new Lesson(new LessonDTO(60, 45, "open", "", LocalDateTime.now().toString()));
    private Lesson lesson8 = new Lesson(new LessonDTO(60, 90.0, "open", "", LocalDateTime.now().toString()));

    private static final Logger LOG = LoggerFactory.getLogger(SeedData.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
        //seedMuziekstukken();
        seedUsers();
        seedBands();
        seedEvents();
        seedLessonTypes();
        seedLessons();
        addStudentsoLessons();
       seedratings();
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
            bandService.createBand(band2);
            bandService.createBand(band1);
            //SET TEACHER AND USERS
            band.setTeacher(user2);
            band.setStudents(Arrays.asList(user, user3));
            band1.setTeacher(user2);
            band1.setStudents(Arrays.asList(user6, user5, user8));
            band2.setTeacher(user4);
            band2.setStudents(Arrays.asList(user9, user3, user6, user8));
            bandService.createBand(band);
            bandService.createBand(band1);
            bandService.createBand(band2);

            LOG.info(String.format("%-6s ADDED || teacher: %-15s || students: %s", band.getName().toUpperCase(), band.getTeacher(), band.getStudents().toString()));
            LOG.info(String.format("%-6s ADDED || teacher: %-15s || students: %s", band1.getName().toUpperCase(), band1.getTeacher(), band1.getStudents().toString()));
            LOG.info(String.format("%-6s ADDED || teacher: %-15s || students: %s", band2.getName().toUpperCase(), band2.getTeacher(), band2.getStudents().toString()));
        }
    }

    private void seedInstruments() {
        if (instrumentService.isInstrumentsEmpty()) {
            instrumentService.createInstrument(instrument);
            instrumentService.createInstrument(instrument1);
            instrumentService.createInstrument(instrument2);
            instrumentService.createInstrument(instrument3);
            instrumentService.createInstrument(instrument4);


            LOG.info(String.format("%-6s ADDED ", instrument.getName()));
            LOG.info(String.format("%-6s ADDED ", instrument1.getName()));
            LOG.info(String.format("%-6s ADDED ", instrument2.getName()));
            LOG.info(String.format("%-6s ADDED ", instrument3.getName()));
            LOG.info(String.format("%-6s ADDED ", instrument4.getName()));
        }
    }




    private void seedMuziekstukken() throws URISyntaxException, IOException {
        if (musicLibService.isLanguagesEmpty()) {
            LanguagesImporter languagesImporter = new LanguagesImporter();
            List<Language> languageList = languagesImporter.getLanguagesList();
            musicLibService.updateLanguageList(languageList);

            LOG.info("Languages added");
        }
        if (musicLibService.isMusicLibEmpty()) {
            Language musipieceLanguage = musicLibService.getLanguage("English");

            ClassLoader classLoader = getClass().getClassLoader();
            Language language = musicLibService.getLanguage("English");
            File musicFile = new File(classLoader.getResource("musicFiles/Requiem-piano-mozart-lacrymosa.mp3").toURI());
            byte[] fileArray = Files.readAllBytes(musicFile.toPath());
            MusicPiece musicPiece1 = new MusicPiece();
            musicPiece1.setArtist("Mozart");
            musicPiece1.setTitle("Requiem piano Mozart. Lacrymosa, requiem in D minor, K 626 III sequence");
            musicPiece1.setMusicFile("Requiem-piano-mozart-lacrymosa.mp3", fileArray);
            musicPiece1.setTypeofpiece("muziekpartituren");
            musicPiece1.setTopic("klassiek");
            musicPiece1.setGenre("Klassieke muziek");
            musicPiece1.setLanguage(musipieceLanguage);
            musicLibService.addMusicPiece(musicPiece1);

            LOG.info(String.format("%-6s ADDED ", musicPiece1.getTitle()));

            // 2e musicfile
            musicFile = new File(classLoader.getResource("musicFiles/how_to_save_a_life_-_the_fray.mp3").toURI());
            fileArray = Files.readAllBytes(musicFile.toPath());
            musicPiece1 = new MusicPiece();
            musicPiece1.setArtist("The fray");
            musicPiece1.setTitle("How to save a life");
            musicPiece1.setLanguage(musipieceLanguage);
            musicPiece1.setTypeofpiece("muziekpartituren");
            musicPiece1.setTopic("klassiek");
            musicPiece1.setGenre("Klassieke muziek");
            musicPiece1.setMusicFile(musicFile.getName(), fileArray);
            File partituur = new File(classLoader.getResource("partituren/How_To_Save_A_Life_-_The_Fray.mxl").toURI());
            fileArray = Files.readAllBytes(partituur.toPath());
            musicPiece1.setPartituurFile(partituur.getName(), fileArray);
            musicLibService.addMusicPiece(musicPiece1);
            LOG.info(String.format("%-6s ADDED ", musicPiece1.getTitle()));

            musicPiece1 = new MusicPiece();
            musicPiece1.setTitle("Sweet Home Alabama");
            musicPiece1.setArtist("Lynyrd Skynyrd");
            musicPiece1.setTopic("love");
            musicPiece1.setChordText("[D]Big[Cadd9] wheels [G]keep on turning\n[D]Carry [Cadd9] me home [G]to see my kin\n[D]Singing[Cadd9]  songs [G]about the south land\n[D]I miss [Cadd9] 'ole' [G]'bamy once again and I think it's a sin\n\n[D] [Cadd9] [G]\n[D] [Cadd9] [G]"/*\n\n[D]Well I heard [Cadd9] Mr. Young [G]sing about her\n[D]Well I heard [Cadd9] old Neil [G]put her down\n[D]Well I hope Neil[Cadd9]  [G]Young will remember\n[D]A southern[Cadd9]  man don’t [G]need him around, anyhow\n\n[D]Sweet [Cadd9] home Alabama[D], [Cadd9] where [G]the skies are so blue\n[D]Sweet [Cadd9] home Alabama, lord I’m [Cadd9] coming [G]home to you.\n\n[F] [C]\n[D] [Cadd9] [G]\n[D] [Cadd9] [G]\n\n[D]In Birmin[Cadd9] gham they [G]love the [F]Gov'nor, [C]boo-hoo[D]-hoo\n[D]Now we all [Cadd9] did what [G]we could do\n[D]Now water[Cadd9] gate [G]doesn't bother me\n[D]Does you [Cadd9] conscience [G]bother you, (now tell the truth!)\n\n[D]Sweet [Cadd9] home [G]Alabama[D], where the skies [G]are so blue\n[D]Sweet [Cadd9] home [G]Alabama[D], lord I’m coming [G]home to you. Here I come\n\n[D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G]\n   [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G]\n\n[D]Now [Cadd9]Muscle Shoals has got the[G] Swappers\n[D] And they've been known to pick a song or [G]two (yes we do)\n[D]Lord they get me off so [G]much\n[D]They pick me up when I'm feeling [G]blue, Now how about you?\n\n[D]Sweet [Cadd9]home [D]Alab[G]ama, where the[Cadd9] skies are so [G]blue\n[D]Sweet [Cadd9]home [D]Alab[G]ama, lord I’m[Cadd9] coming home to [G]you\n\n[D]Sweet [Cadd9]home Alab[G]ama (Oh sweet home baby)\n[D]Where the [Cadd9]skies are so [G]blue (And the governor's true)\n[D]Sweet [Cadd9]Home Alab[G]ama, (Lord, yeah)\n[D]Lord, I'm [Cadd9]coming home to [G]you\n\n[D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G] [D] [Cadd9] [G]"*/);
            musicPiece1.setGenre( "rock");
            musicPiece1.setInstrumentType("gitaarakkoorden");
            musicPiece1.setYoutubeUrl("https://www.youtube.com/watch?v=2fntQxhkht4");
            musicPiece1.setTypeofpiece("akkoord");
            musicPiece1.setDifficulty("easy");
            musicLibService.addMusicPiece(musicPiece1);
            LOG.info(String.format("%-6s ADDED ", musicPiece1.getTitle()));

            musicPiece1 = new MusicPiece();
            musicPiece1.setTitle("Havana");
            musicPiece1.setArtist("Camilla Cabello");
            musicPiece1.setTopic("Ode");
            musicPiece1.setChordText("[A]test");
            musicPiece1.setGenre( "pop");
            musicPiece1.setInstrumentType("gitaarakkoorden");
            musicPiece1.setYoutubeUrl("https://www.youtube.com/watch?v=6tEZoZOh8MM");
            musicPiece1.setTypeofpiece("akkoord");
            musicPiece1.setLanguage(musipieceLanguage);

            musicPiece1.setDifficulty("easy");
            musicLibService.addMusicPiece(musicPiece1);
            LOG.info(String.format("%-6s ADDED ", musicPiece1.getTitle()));


            musicFile = new File(classLoader.getResource("musicFiles/Youll_Be_In_My_Heart.mp3").toURI());
            fileArray = Files.readAllBytes(musicFile.toPath());
            musicPiece1 = new MusicPiece();
            musicPiece1.setArtist("Phil Collins");
            musicPiece1.setTitle("You'll be in my heart");
            musicPiece1.setLanguage(musipieceLanguage);
            musicPiece1.setTypeofpiece("muziekpartituren");
            musicPiece1.setTopic("Disney");
            musicPiece1.setGenre("Pop");
            musicPiece1.setDifficulty("average");
            musicPiece1.setInstrumentType("pianoakkoorden");
            musicPiece1.setMusicFile(musicFile.getName(), fileArray);
            partituur = new File(classLoader.getResource("musicFiles/Youll_Be_In_My_Heart.mxl").toURI());
            fileArray = Files.readAllBytes(partituur.toPath());
            musicPiece1.setPartituurFile(partituur.getName(), fileArray);
            musicLibService.addMusicPiece(musicPiece1);
            LOG.info(String.format("%-6s ADDED ", musicPiece1.getTitle()));


//
        }

    }

    private void seedEvents() throws BandNotFoundException, EventNotFoundException {
        if (eventService.isEventEmpty()) {
            event.setStart(LocalDateTime.parse("03/06/2018 20:00", formatter));
            event1.setStart(LocalDateTime.parse("29/06/2018 20:00", formatter));
            event2.setStart(LocalDateTime.parse("03/08/2018 20:00", formatter));
            event3.setStart(LocalDateTime.parse("25/03/2018 20:00", formatter));

            eventService.createEvent(event);
            eventService.createEvent(event1);
            eventService.createEvent(event2);
            eventService.createEvent(event3);

            LOG.info(String.format("%-6s ADDED || date: %-15s || place: %-15s || band: %s", event.getTitle().toUpperCase(), event.getStart().toString(), event.getPlace(), event.getBand().getName()));
            LOG.info(String.format("%-6s ADDED || date: %-15s || place: %-15s || band: %s", event1.getTitle().toUpperCase(), event1.getStart().toString(), event1.getPlace(), event1.getBand().getName()));
            LOG.info(String.format("%-6s ADDED || date: %-15s || place: %-15s || band: %s", event2.getTitle().toUpperCase(), event2.getStart().toString(), event2.getPlace(), event2.getBand().getName()));
            LOG.info(String.format("%-6s ADDED || date: %-15s || place: %-15s || band: %s", event3.getTitle().toUpperCase(), event3.getStart().toString(), event3.getPlace(), event3.getBand().getName()));
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
        lesson1.setDate(LocalDateTime.parse("22/03/2018 13:30", formatter));
        lesson2.setDate(LocalDateTime.parse("23/03/2018 13:30", formatter));
        lesson3.setDate(LocalDateTime.parse("22/03/2018 15:30", formatter));
        lesson4.setDate(LocalDateTime.parse("24/03/2018 13:30", formatter));
        lesson5.setDate(LocalDateTime.parse("22/03/2018 17:00", formatter));
        lesson6.setDate(LocalDateTime.parse("23/03/2018 15:30", formatter));
        lesson7.setDate(LocalDateTime.parse("24/03/2018 13:30", formatter));
        lesson8.setDate(LocalDateTime.parse("23/03/2018 20:30", formatter));

        lesson1.setLessonType(lessonType1);
        lesson2.setLessonType(lessonType2);
        lesson3.setLessonType(lessonType3);
        lesson4.setLessonType(lessonType3);
        lesson5.setLessonType(lessonType2);
        lesson6.setLessonType(lessonType1);
        lesson7.setLessonType(lessonType2);
        lesson8.setLessonType(lessonType1);

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
        lessonService.addStudentToLesson(new Attender("leerling", user7, lesson2));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson3));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson4));
        lessonService.addStudentToLesson(new Attender("leerling", user5, lesson5));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson6));
        lessonService.addStudentToLesson(new Attender("leerling", user6, lesson7));
        lessonService.addStudentToLesson(new Attender("leerling", user5, lesson8));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson1));
        lessonService.addStudentToLesson(new Attender("leerling", user8, lesson2));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson3));
        lessonService.addStudentToLesson(new Attender("leerling", user9, lesson4));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson5));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson6));
        lessonService.addStudentToLesson(new Attender("leerling", user9, lesson7));
        lessonService.addStudentToLesson(new Attender("leerling", user, lesson8));
        lessonService.addStudentToLesson(new Attender("leerling", user3, lesson1));
        lessonService.addStudentToLesson(new Attender("leerling", user7, lesson7));
        lessonService.addStudentToLesson(new Attender("leerling", user7, lesson8));
        lessonService.addStudentToLesson(new Attender("leerling", user5, lesson3));

        LOG.info(String.format("%-6s ADDED TO LESSONS", "STUDENTS"));


        lessonService.addStudentToLesson(new Attender("lesgever", user2, lesson1));
        lessonService.addStudentToLesson(new Attender("lesgever", user2, lesson2));
        lessonService.addStudentToLesson(new Attender("lesgever", user2, lesson3));
        lessonService.addStudentToLesson(new Attender("lesgever", user4, lesson4));
        lessonService.addStudentToLesson(new Attender("lesgever", user4, lesson5));
        lessonService.addStudentToLesson(new Attender("lesgever", user4, lesson6));
        lessonService.addStudentToLesson(new Attender("lesgever", user4, lesson7));
        lessonService.addStudentToLesson(new Attender("lesgever", user2, lesson8));

        LOG.info(String.format("%-6s ADDED TO LESSONS", "TEACHERS"));
    }

    private void seedratings() {
        MusicPieceRating mpr;
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(1));
        mpr.setUser(new Long(1));
        mpr.setRating(4);
        musicLibService.addRating(mpr);

        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(2));
        mpr.setUser(new Long(1));
        mpr.setRating(3);
        musicLibService.addRating(mpr);

        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(3));
        mpr.setUser(new Long(1));
        mpr.setRating(5);
        musicLibService.addRating(mpr);

        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(4));
        mpr.setUser(new Long(1));
        mpr.setRating(4);
        musicLibService.addRating(mpr);

        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(1));
        mpr.setUser(new Long(2));
        mpr.setRating(2);
        musicLibService.addRating(mpr);

        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(6));
        mpr.setUser(new Long(6));
        mpr.setRating(4);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(3));
        mpr.setUser(new Long(2));
        mpr.setRating(3);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(4));
        mpr.setUser(new Long(8));
        mpr.setRating(2);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(2));
        mpr.setUser(new Long(5));
        mpr.setRating(1);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(6));
        mpr.setUser(new Long(3));
        mpr.setRating(0);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(5));
        mpr.setUser(new Long(8));
        mpr.setRating(5);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(4));
        mpr.setUser(new Long(3));
        mpr.setRating(4);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(3));
        mpr.setUser(new Long(5));
        mpr.setRating(3);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(2));
        mpr.setUser(new Long(4));
        mpr.setRating(2);
        musicLibService.addRating(mpr);
        mpr = new MusicPieceRating();
        mpr.setMusicpiece(new Long(1));
        mpr.setUser(new Long(7));
        mpr.setRating(3);
        musicLibService.addRating(mpr);
    }

}
