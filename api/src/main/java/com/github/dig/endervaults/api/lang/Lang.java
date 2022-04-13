package com.github.dig.endervaults.api.lang;

import lombok.Getter;

public enum Lang {

    VAULT_SELECTOR_TITLE("vault-selector-title"),
    ADMIN_VAULT_SELECTOR_TITLE("admin-vault-selector-title"),
    VAULT_TITLE("vault-title"),
    VAULT_SELECT_ICON_TITLE("vault-select-icon-title"),
    INVALID_VAULT_ORDER("invalid-vault-order"),
    PLAYER_NOT_LOADED("player-not-loaded"),
    NO_PERMISSION("no-permission"),
    CONFIG_RELOAD("config-reload"),
    BLACKLISTED_ITEM("blacklisted-item"),
    PLAYER_NOT_FOUND("player-not-found"),
    ONLY_FROM_CONSOLE("only-from-console");
    //UNLOCK_VAULT("unlock-vault");

    @Getter
    private String key;

    Lang(String key) {
        this.key = key;
    }
}
