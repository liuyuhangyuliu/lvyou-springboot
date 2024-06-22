package edu.hit.lvyoubackend.utils.token;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class MailRegisterToken {
    @NotBlank
    private String mailAddress;
    @NotBlank
    private String username;
    @NotBlank
    private String code;
}
