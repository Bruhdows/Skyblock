package me.bruhdows.skyblock.core.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public final class UserData implements Serializable {

    private Object object;
    private final boolean save;

}
