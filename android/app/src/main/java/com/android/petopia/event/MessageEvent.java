package com.android.petopia.event;


import com.android.petopia.model.Order;
import com.android.petopia.model.Pet;
import com.android.petopia.model.Product;
import com.android.petopia.model.Service;

public class MessageEvent {

    public static class ProductEvent {
        private Product product;

        public ProductEvent(Product product) {
            this.product = product;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    public static class PetEvent {
        private Pet pet;

        public PetEvent(Pet pet) {
            this.pet = pet;
        }

        public Pet getPet(){return pet;}

        public void setPet(Pet pet) {this.pet = pet;}
    }

    public static class ServiceEvent{
        private Service service;

        public ServiceEvent(Service service){
            this.service = service;
        }

        public Service getService(){return service;}

        public void setService(Service service) {this.service = service;}
    }
    public static class OrderEvent{
        private Order order;

        public OrderEvent(Order order) {
            this.order = order;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }
    }
}