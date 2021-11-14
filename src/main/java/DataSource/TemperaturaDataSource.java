package DataSource;

public class TemperaturaDataSource {
    private static TemperaturaDataSource sharedInstance = new TemperaturaDataSource();
    private String min;
    private String max;

    private TemperaturaDataSource() {}

    public static TemperaturaDataSource getInstance() {
        return sharedInstance;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMax() {
        return max;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMin() {
        return min;
    }
}
