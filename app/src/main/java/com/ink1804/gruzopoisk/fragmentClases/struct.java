package com.ink1804.gruzopoisk.fragmentClases;

/**
 * Created by Ink1804 on 01.04.15.
 */
public class struct {

    String city_from;//откуда
    String city_to;//куда
    String name;//что везти
    String common_price;//вроде цена
    String note;//примечание
    String _url;//сайт
   // int at_unload; // на выгрузке (0 или 1)
   // String distance_human; // реальное расстояние
   // String firm_location; // расположение фирмы
   // int count;//количество машин
   // int weight;//тонн
   // int pre_pay;//предоплата (0 или 1)
    // String nds_price;//оплата с ндс
    // String no_nds_price;//  оплата без ндс

    public struct(String city_from, String city_to, String name, String common_price, String note) {
        this.city_from = city_from;
        this.city_to = city_to;
        this.name = name;
        this.common_price = common_price;
        this.note = note;
    }
    //&#160; - пробел
}
