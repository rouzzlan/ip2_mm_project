package be.kdg.musicmaker.user;

import be.kdg.musicmaker.model.DTO.UserDTO;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user, HttpServletRequest request) throws UserNotFoundException {
        try {
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
                    + appUrl + "/confirm?token=" + user.getConfirmationToken());
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
            user.setEnabled(true);
            //update said user
            userService.createUser(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
