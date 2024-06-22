package edu.hit.lvyoubackend.utils.token;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MailLoginToken {
    @NotEmpty
    @NotNull
    @NotBlank(message = "邮箱不能为空")
    private String mailAddress;
    @NotBlank(message = "验证码不能为空")
    private String code;
}
