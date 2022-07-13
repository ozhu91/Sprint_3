package api.model;

import java.util.ArrayList;

public class OrderList {
    private ArrayList<Order> orders;
    private OrderPageInfo pageInfo;
    private ArrayList<MetroStation> availableStations;


    public OrderList(ArrayList<Order> orders, OrderPageInfo pageInfo, ArrayList<MetroStation> availableStations) {
        this.orders = orders;
        this.pageInfo = pageInfo;
        this.availableStations = availableStations;
    }

    public OrderList() {}
}

