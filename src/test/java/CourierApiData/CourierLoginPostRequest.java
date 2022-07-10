package CourierApiData;

public class CourierLoginPostRequest {
    private Integer id;
    public CourierLoginPostRequest(Integer id) {
        this.id = id;
    }

    public CourierLoginPostRequest() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
