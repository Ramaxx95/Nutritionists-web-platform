package com.inutri.cucumber.support;

import com.inutri.modelo.Alimento;
import com.inutri.repositorio.AlimentoRepository;
import io.cucumber.java.Before;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ContextConfiguration
public class InitialSetUp {

    @Autowired
    private AlimentoRepository alimentoRepository;

    private static boolean yaEjecutado = false;

    @Before(order = 0)
    public void beforeAll() {
        if (!yaEjecutado) {
            yaEjecutado = true;

            alimentoRepository.save(new Alimento("Frutas","Manzana con piel",48,new BigDecimal("0.3"),new BigDecimal("13.8"),new BigDecimal("0.2"),new BigDecimal("0"),1));
            alimentoRepository.save(new Alimento("Legumbres, cereales, papa, pan y pastas","Galleta marinera",350,new BigDecimal("12.80"),new BigDecimal("75.90"),new BigDecimal("0.70"),new BigDecimal("0"),30));
            alimentoRepository.save(new Alimento("Legumbres, cereales, papa, pan y pastas","Grisines",380,new BigDecimal("10.90"),new BigDecimal("76.60"),new BigDecimal("5.00"),new BigDecimal("1.00"),552));
            alimentoRepository.save(new Alimento("Carnes","Chinchulines, tripagorda (horno/parrilla)",270,new BigDecimal("13.80"),new BigDecimal("0.00"),new BigDecimal("23.90"),new BigDecimal("188.00"),58));
            alimentoRepository.save(new Alimento("Carnes","Chorizo seco",425,new BigDecimal("21.70"),new BigDecimal("1.20"),new BigDecimal("37.00"),new BigDecimal("80.00"),1890));
            alimentoRepository.save(new Alimento("Pescados y mariscos","Merluza",104,new BigDecimal("22.20"),new BigDecimal("0.00"),new BigDecimal("1.70"),new BigDecimal("28.00"),130));
            alimentoRepository.save(new Alimento("Pescados y mariscos","Pejerrey",110,new BigDecimal("23.80"),new BigDecimal("0.30"),new BigDecimal("1.50"),new BigDecimal("36.00"),87));
            alimentoRepository.save(new Alimento("Leche y postres de leche","Leche descremada fluida, con 50% más de proteínas",43,new BigDecimal("4.5"),new BigDecimal("5.4"),new BigDecimal("0.4"),new BigDecimal("0"),53));
            alimentoRepository.save(new Alimento("Legumbres, cereales, papa, pan y pastas","Tostadas light",361,new BigDecimal("12"),new BigDecimal("80"),new BigDecimal("1"),new BigDecimal("0"),440));
            alimentoRepository.save(new Alimento("Carnes","Pollo con piel (horno/parrilla)",216,new BigDecimal("24"),new BigDecimal("0"),new BigDecimal("13.4"),new BigDecimal("76"),73));
            alimentoRepository.save(new Alimento("Verduras","Zapallito, hervido",15,new BigDecimal("1.1"),new BigDecimal("2.7"),new BigDecimal("0.4"),new BigDecimal("0"),2));
            alimentoRepository.save(new Alimento("Legumbres, cereales, papa, pan y pastas","Arroz blanco, hervido",126,new BigDecimal("2.4"),new BigDecimal("28.6"),new BigDecimal("0.2"),new BigDecimal("0"),0));
            alimentoRepository.save(new Alimento("Carnes","Hamburguesa de carne vacuna (carnicería)",209,new BigDecimal("18.6"),new BigDecimal("0"),new BigDecimal("15"),new BigDecimal("68"),66));

        }
    }
}