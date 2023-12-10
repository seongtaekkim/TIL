package me.staek.chapter08.item51;


/**
 * 열거타입상수간 코드공유 : The strategy enum pattern
 */
public enum TemperatureScaleSEP {
    FAHRENHEIT(Transfer.CELSIUS), CELSIUS(Transfer.FAHRENHEIT);

    private final Transfer transfer;

    TemperatureScaleSEP(Transfer transfer) {
        this.transfer = transfer;
    }

    enum Transfer {

        FAHRENHEIT {
            double transfer(double data) {
                return (data * 1.8) + 32;
            }
        }
        ,CELSIUS {
            double transfer(double data) {
                return (data - 32) / 1.8;
            }
        };
        abstract double transfer(double data);
        double action(double data) {
            return transfer(data);
        }
    }

    double action(double data) {
        return transfer.action(data);
    }

    public static void main(String[] args) {
        for (TemperatureScaleSEP day : values())
            System.out.printf("%-10s%f%n", day, day.action(32));
    }
}
