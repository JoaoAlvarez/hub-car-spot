package com.hubcarspot.api.domain;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserAutenticationInstituicaoToken extends UsernamePasswordAuthenticationToken {

    private final Object principal;
    private Object credentials;
    private String usuarioInstituicao;

    public UserAutenticationInstituicaoToken(Object principal, Object credentials, String usuarioInstituicao) {
        super(principal, credentials);
        this.principal = principal;
        this.credentials = credentials;
        this.usuarioInstituicao = usuarioInstituicao;
        this.setAuthenticated(false);
    }

    public UserAutenticationInstituicaoToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    public String getUsuarioInstituicao() {
        return usuarioInstituicao;
    }

    public void setUsuarioInstituicao(String usuarioInstituicao) {
        this.usuarioInstituicao = usuarioInstituicao;
    }
}
