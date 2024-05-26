package com.ada.economizaapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LocalizacaoServiceTest {

    @Autowired
    private LocalizacaoService localizacaoService;

    @Test
    void testRetornarDistanciaKm() {
        Double distancia = localizacaoService.retornarDistanciaKm("-34.90860637402315, -8.052514882835915", "-34.89939394133639, -8.05166126289959");

        assertEquals(2.1462, distancia, 0.001);
    }
}
