package DataSource;

public class UmidadeDataSource {
    private static UmidadeDataSource sharedInstance  = new UmidadeDataSource();
    private String min;
    private String max;

    private UmidadeDataSource() {}

    public static UmidadeDataSource getInstance() {
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
