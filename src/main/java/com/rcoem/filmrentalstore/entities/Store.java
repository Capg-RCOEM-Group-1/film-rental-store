package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Store {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
private String store_name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
    Store(){}
    Store(Long id, String store_name){
        this.id = id;
        this.store_name = store_name;
    }
    public Store(String store_name){
        this.store_name = store_name;
    }
}
