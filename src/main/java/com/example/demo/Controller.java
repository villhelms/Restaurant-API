package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;





@RestController
public class Controller {
    
    String[] meals = {"burger", "fried chicken", "pasta", "salmon"};
    String[] drinks = {"water", "sprite", "coke", "sweet tea", "lemonade", "beer"};
    
    @Autowired
    Repo repository;
    
    @GetMapping("/")
    public String getRoot() {
        return "The root is able to communicate";
    }
    @GetMapping("/orders")
    public List<DemoOrder> getOrders() {
        return repository.findAll();
    }
    
    @GetMapping("/menu")
    public ArrayList<String> getMenu() {
        ArrayList<String> menu = new ArrayList<String>();
        //String fullMenu = "";

        //fullMenu += "Entrees: \n";
        for(String e : meals){
            menu.add(e);
            //fullMenu += "\t" + e + "\n";
        }

        //fullMenu += "\n\nDrinks: \n";
        for(String x : drinks){
            menu.add(x);
            //fullMenu +="\t" + x + "\n";
        }

        return menu;
    }

    @GetMapping("/menu/entrees")
    public String[] getEntrees() {
        return meals;
    }
    
    @GetMapping("/menu/drinks")
    public String[] getDrinks() {
        return drinks;
    }

    @GetMapping("/order/{id}")
    public DemoOrder getOrderById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping("/order")
    public String createOrder(@RequestBody DemoOrder order ) {
        Boolean entree = false;
        Boolean drink = false;

        for (String element: meals) {
            if (order.getEntree().toLowerCase().equals(element)){
                entree = true;
            }
        }

        for (String element: drinks) {
            if (order.getDrink().toLowerCase().equals(element)){
                drink = true;
            }
        }

        if (entree && drink) {
            repository.save(order);
            return "Order has been placed";
        }
        else return "That item is not on our menu";
        
    }

    @PutMapping("/order/{id}")
    public DemoOrder updateOrder(@PathVariable Long id, @RequestBody DemoOrder updatedOrder) {
        DemoOrder currentOrder = repository.findById(id).orElse(null);
        if(currentOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error: 404 order data not found");
        }

        currentOrder.setName(updatedOrder.getName());
        currentOrder.setDrink(updatedOrder.getDrink());
        currentOrder.setEntree(updatedOrder.getEntree());

        return repository.save(currentOrder);
    }

    @DeleteMapping("/order/{id}")
    public void deleteOrder(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}



