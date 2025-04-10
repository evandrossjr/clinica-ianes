package com.sistema.clinica.config;

import com.sistema.clinica.models.enums.DiaDaSemana;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToDiaDaSemanaConverter implements Converter<String, DiaDaSemana> {
    @Override
    public DiaDaSemana convert(String source) {
        return DiaDaSemana.valueOf(source.toUpperCase());
    }

}
