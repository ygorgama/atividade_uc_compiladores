package facs.edu.entities;

import facs.edu.entities.enums.TokensTypes;

public class Token {
    private final TokensTypes tokenType;
    private final String tokenValue;
    private final Integer tokenLine;
    private final Integer tokenPosition;

    public Token(TokensTypes tokenType, String tokenValue, Integer tokenLine, Integer tokenPosition) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
        this.tokenLine = tokenLine;
        this.tokenPosition = tokenPosition;
    }

    public TokensTypes getTokenType() {
        return tokenType;
    }

    public Integer getTokenLine() {
        return tokenLine;
    }

    public Integer getTokenPosition() {
        return tokenPosition;
    }

    public String getTokenValue() {
        return tokenValue;
    }


    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", tokenValue='" + tokenValue + '\'' +
                ", tokenLine=" + tokenLine +
                ", tokenPosition=" + tokenPosition +
                '}';
    }
}
