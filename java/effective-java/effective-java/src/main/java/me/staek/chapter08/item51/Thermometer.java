package me.staek.chapter08.item51;

public class Thermometer {

    TemperatureScale temperatureScale;
    TemperatureScaleSEP temperatureScaleSEP;

    private Thermometer() {}
    public Thermometer(TemperatureScale temperatureScale) {
        this.temperatureScale = temperatureScale;
    }
    public Thermometer(TemperatureScaleSEP temperatureScaleSEP) {
        this.temperatureScaleSEP = temperatureScaleSEP;
    }
    public static Thermometer newInstance(TemperatureScale temperatureScale) {
        return new Thermometer(temperatureScale);
    }
    public static Thermometer newInstance(TemperatureScaleSEP temperatureScaleSEP) {
        return new Thermometer(temperatureScaleSEP);
    }

    public double toCELSIUS(double data) {
        return this.temperatureScaleSEP.action(data);
    }

    public static void main(String[] args) {
//        Thermometer.newInstance(true);
        Thermometer.newInstance(TemperatureScale.CELSIUS);
        System.out.println(Thermometer.newInstance(TemperatureScaleSEP.FAHRENHEIT).toCELSIUS(32));;
    }
}
