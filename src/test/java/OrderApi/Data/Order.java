package OrderApi.Data;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    private int id;
    private int courierId;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private Date deliveryDate;
    private int track;
    private ArrayList<String> color;
    private String comment;
    private Date createdAt;
    private Date updatedAt;
    private int status;
}
