package com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eScentedCandle_project_BackEnd.eScentedCandle.models.enums.Permission.*;


@RequiredArgsConstructor
public enum Role {
    USER(
            Set.of(
                    USER_VIEW,
                    USER_CREATE,
                    USER_UPDATE,
                    USER_DELETE

            )
    ),

    ADMIN(
            Set.of(
                    ADMIN_VIEW,
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE

            )
    ),
    GUEST(
            Set.of(
                    GUEST_VIEW

    )
    );



    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }
}