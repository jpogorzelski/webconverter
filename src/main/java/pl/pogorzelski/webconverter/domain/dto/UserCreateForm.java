package pl.pogorzelski.webconverter.domain.dto;

import pl.pogorzelski.webconverter.domain.Role;

import javax.validation.constraints.NotNull;

public class UserCreateForm extends RegisterForm {


    @NotNull
    private Role role = Role.USER;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserCreateForm{" +
                "email='" + getEmail().replaceFirst("@.+", "@***") + '\'' +
                ", password=***" + '\'' +
                ", passwordRepeated=***" + '\'' +
                ", role=" + role +
                '}';
    }

}
