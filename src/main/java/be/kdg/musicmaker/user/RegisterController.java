package be.kdg.musicmaker.user;

import be.kdg.musicmaker.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody String jsonString, HttpServletRequest request) throws UserNotFoundException, JSONException {
        JSONObject jsonObj;
        UserDTO user = new UserDTO();
        try {
            jsonObj = new JSONObject(jsonString);
            user.setEmail(jsonObj.getString("email"));
            user.setPassword(jsonObj.getString("password"));
            userService.doesUserExist(user.getEmail());
        } catch (UserNotFoundException e) {

            // Disable user until they click on confirmation link in email
            user.setEnabled(false);

            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());

            userService.createUser(user);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getEmail());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                    + appUrl + "/account/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("noreply@domain.com");

            emailService.sendEmail(registrationEmail);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        //The request could not be completed due to a conflict with the current state of the resource.
        return ResponseEntity.status(HttpStatus.valueOf(409)).build();
    }

    // Process confirmation link
    @GetMapping(value="/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) throws UserNotFoundException  {
        try {
            User user = userService.findByConfirmationToken(token);
            if (user.isEnabled() == true){
                return ResponseEntity.status(HttpStatus.valueOf(409)).build();
            } else {
                user.setEnabled(true);
            }
            //update user
            userService.createUser(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
