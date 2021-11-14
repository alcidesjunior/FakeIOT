package DataSource;

public class VelocidadeDataSource {
    private static VelocidadeDataSource sharedInstance = new VelocidadeDataSource();
    private String min;
    private String max;

    private VelocidadeDataSource() {}

    public static VelocidadeDataSource getInstance() {
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
