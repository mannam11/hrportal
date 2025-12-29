package com.app.hrportal.dto.request;

import com.app.hrportal.enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {
    @Email
    @NotBlank
    private String email;

    @NotNull
    private OtpPurpose purpose;
}

