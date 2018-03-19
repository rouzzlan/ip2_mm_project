package be.kdg.musicmaker.CRUD.event;

import be.kdg.musicmaker.MMAplication;
import be.kdg.musicmaker.event.dto.EventDTO;
import be.kdg.musicmaker.event.EventNotFoundException;
import be.kdg.musicmaker.event.EventService;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.security.CorsFilter;
import be.kdg.musicmaker.util.TokenGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MMAplication.class)
public class EventTest {
    private LocalDateTime localDateTime;
    private EventDTO eventDTO;
    private static String ACCESS_TOKEN_Admin = "";
    private static String ACCESS_TOKEN_Student = "";
    private static String ACCESS_TOKEN_Teacher = "";
    private TokenGetter tokenGetter;
    private ObjectMapper objectMapper;

    @Autowired
    EventService eventService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CorsFilter corsFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity())
                .addFilter(corsFilter)
                .build();

        try {
            tokenGetter = TokenGetter.getInstance(this.mockMvc);
            objectMapper = new ObjectMapper();
            //REQ om Time Obj. te parsen naar JSON
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            localDateTime = LocalDateTime.now();

            eventDTO = new EventDTO("testEvent", LocalDateTime.now().toString(), "KdG", "The X-Nuts");
            ACCESS_TOKEN_Admin = tokenGetter.obtainAccessToken("user3@user.com", "user3");
            ACCESS_TOKEN_Student = tokenGetter.obtainAccessToken("user@user.com", "user");
            ACCESS_TOKEN_Teacher = tokenGetter.obtainAccessToken("user2@user.com", "user2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //CREATE
    @Test
    public void CreateEventAsAdmin() throws EventNotFoundException {
        String jsonString = "";
        try {
            //Volledige LocalDateTime objecten serializen naar blob
            //kunnen ook ISO string opslaan in plaats van volledige objecten
            //dan moet er gewoon geparsed worden voor gebruik
            jsonString = objectMapper.writeValueAsString(eventDTO);
            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            this.mockMvc.perform(post("/event/add")
                    .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                    .contentType(MediaType.APPLICATION_JSON).content(jsonString)).andDo(print())
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Event event = eventService.getEvent("testEvent");
        assertNotNull(event);
    }

    //READ
    //MVC result parsing
    //http://tutorials.jenkov.com/java-json/jackson-objectmapper.html#read-object-from-json-string
    @Test
    public void GetEventAsAdmin() throws Exception {
        MvcResult res =  mockMvc.perform(get("/event/get")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)).andReturn();

        List<EventDTO> result = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<List<EventDTO>>(){});
        assertEquals(result.get(0).getName(), "SportPladijsje");
        assertEquals(result.get(2).getName(), "event2");

        res = mockMvc.perform(get("/event/id/1")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andReturn();

        EventDTO result2 = objectMapper.readValue(res.getResponse().getContentAsString(), EventDTO.class);
        assertEquals(result2.getName(), "SportPladijsje");

        //deze gebruiker is door andere testen verwijderd dus zou null moeten geven
        res = mockMvc.perform(get("/event/email/user3@user.com")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin))
                .andReturn();

        List<EventDTO> result3 = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<List<EventDTO>>(){});
        assertEquals(result3.get(0).getName(), "SportPladijsje");
        assertEquals(result3.get(2).getName(), "event2");
    }

    //UPDATE
    @Test
    public void UpdateEventAsAdmin() throws Exception {
        EventDTO original = new EventDTO("event3", LocalDateTime.now().toString(), "Sportpaleis", "The X-Nuts");
        EventDTO updateDTO = new EventDTO("event3", LocalDateTime.now().toString(), "BijSeppeThuis", "The X-Nuts");

        Event event = eventService.getEvent(original.getName());
        assertEquals("Sportpaleis", event.getPlace());

        mockMvc.perform(put("/event/update")
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andDo(print())
                .andExpect(status().isContinue());

        event = eventService.getEvent(original.getName());
        assertEquals("BijSeppeThuis", event.getPlace());
        eventService.updateEvent(original);
        event = eventService.getEvent(original.getName());
        assertEquals("Sportpaleis", event.getPlace());
    }

    //DELETE
    @Test
    public void DeleteEventAsAdmin() throws Exception {
        Event original = eventService.getEvent("event3");
        EventDTO originalDTO = new EventDTO(original.getName(), original.getDateTime().toString(), original.getPlace(), original.getBand().getName());

        mockMvc.perform(delete("/event/delete/" + original.getId())
                .header("Authorization", "Bearer " + ACCESS_TOKEN_Admin)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Event event = null;
        try {
            event = eventService.getEvent("event3");
        } catch (EventNotFoundException e) {
            event = null;
        }
        assertNull(event);
        eventService.createEvent(originalDTO);
        assertNotNull(eventService.getEvent("event3"));
    }
}