package com.example.clockcustomview


data class TimeZones(
    val name: String,
    val value: String
)

class AvailableTimeZoneValues(
    val values: List<TimeZones>,
    val currentIndex: Int
) {

    companion object {
        fun createTimeZoneValues(currentTimeZone: TimeZones): AvailableTimeZoneValues {
            val timeZones = listOf(
                TimeZones( "Мехико, Мексика", "America/Mexico_City"),
                TimeZones( "Нью Йорк, США", "America/New_York"),
                TimeZones( "Каракас, Венесуэла", "America/Caracas"),
                TimeZones( "Сантьяго, Чили", "America/Santiago"),
                TimeZones("Лондон, Великобритания", "Europe/London"),
                TimeZones("Осло, Норвегия", "Europe/Oslo"),
                TimeZones("Рига, Латвия", "Europe/Riga"),
                TimeZones( "Москва, Россия", "Europe/Moscow"),
                TimeZones("Ереван, Армения", "Asia/Yerevan"),
                TimeZones("Мале, Мальдивы", "Indian/Maldives"),
                TimeZones("Алматы, Казахстан", "Asia/Almaty"),
                TimeZones("Бангкок, Таиланд", "Asia/Bangkok"),
                TimeZones("Гонконг, Гонконг", "Asia/Hong_Kong"),
                TimeZones( "Токио, Япония", "Asia/Tokyo"),
                TimeZones( "Порт-Морсби, Папуа - Новая Гвинея", "Pacific/Port_Moresby"),
                TimeZones( "Канберра, Австралия", "Australia/Canberra"),
                TimeZones( "Веллингтон, Новая Зеландия", "Pacific/Auckland")
            )

            val currentIndex = timeZones.indexOf(currentTimeZone)
            return AvailableTimeZoneValues(timeZones.toList(), currentIndex)
        }

    }

}