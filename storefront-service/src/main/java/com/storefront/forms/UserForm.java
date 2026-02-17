package com.storefront.forms;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {



    @NotBlank(message = "first name is required ")
    @Size(message = "Minimum 3 characters", min = 3)
    private String firstName;
    @NotBlank(message = "last name is required ")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email Address")
    private String email;
    @Size(message = "Minimum 6 characters", min = 6)
    private String password;
    @Size(message = "Invalid Phone Number", min = 10, max =14)
    private String phoneNumber;
   

    // private String email;
    // private String password; 


    // private String phoneNumber;
    // private String firstName;
    // private String lastName;


   @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate DOB;

}
