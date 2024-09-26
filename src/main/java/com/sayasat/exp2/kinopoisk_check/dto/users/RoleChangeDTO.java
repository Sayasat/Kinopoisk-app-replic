package com.sayasat.exp2.kinopoisk_check.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeDTO {
    private String username;
    private String newRole;
}
