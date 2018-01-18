package com.coviam.util;


import org.springframework.stereotype.Component;

@Component
public class EscapeCharacter {

    public String escapeCharacter(String escapeString){
        String unescapeString = escapeString.replaceAll("\\\\", "");
        return unescapeString;
    }
}
