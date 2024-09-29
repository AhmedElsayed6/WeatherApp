package com.example.weatherapp.data.source.sharedPrefrence


class FakeShared : IWeatherSharedPreferenceDataSource {

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private var isFirstTime :Boolean = true

    override fun getLanguage(): String {
     return "English"
    }

    override fun getUnitTemp(): String {
        TODO("Not yet implemented")
    }

    override fun getUnitWind(): String {
        TODO("Not yet implemented")
    }

    override fun getNotificationSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getLocationSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getLatitude(): Double {
        return latitude
    }

    override fun getLongitude(): Double {
        return longitude
    }

    override fun setLanguage(language: String) {
        TODO("Not yet implemented")
    }

    override fun setUnitTemp(unit: String) {
        TODO("Not yet implemented")
    }

    override fun setUnitWind(unit: String) {
        TODO("Not yet implemented")
    }

    override fun setNotificationSettings(key: String) {
        TODO("Not yet implemented")
    }

    override fun setLocationSettings(key: String) {
        TODO("Not yet implemented")
    }

    override fun setLatitude(lat: Double) {
        latitude = lat
    }

    override fun setLongitude(long: Double) {
        longitude = long
    }

    override fun getFirstTime(): Boolean {
        return isFirstTime
    }

    override fun getFirstTimeData(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setFirstTime() {
        isFirstTime=false
    }

    override fun setFirstTimeData() {

    }
}