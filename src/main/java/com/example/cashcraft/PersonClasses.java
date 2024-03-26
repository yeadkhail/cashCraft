package com.example.cashcraft;

import java.security.cert.PolicyNode;
import java.util.Date;

public class PersonClasses {
    public class Wallet{
        public String uuid;
        public String name;
        public String desc;
        public Wallet(String name, String desc){
            this.uuid = "";
            this.name = name;
            this.desc = desc;
        }
        public Wallet(){
            name = "";
            desc = "";
        }
        public Wallet(String uuid,String name, String desc){
            this.uuid = uuid;
            this.name = name;
            this.desc = desc;
        }
    }
    public class Place{
        public String uuid;
        public String name;
        public String desc;
        public Place(String name, String desc){
            this.uuid = "";
            this.name = name;
            this.desc = desc;
        }
        public Place(){
            name = "";
            desc = "";
        }
        public Place(String uuid,String name, String desc){
            this.uuid = uuid;
            this.name = name;
            this.desc = desc;
        }
    }
    public class People{
        public String uuid;
        public String name;
        public String desc;
        public People(String name, String desc){
            this.uuid = "";
            this.name = name;
            this.desc = desc;
        }
        public People(){
            name = "";
            desc = "";
        }
        public People(String uuid,String name, String desc){
            this.uuid = uuid;
            this.name = name;
            this.desc = desc;
        }
    }
    public class Category{
        public String uuid;
        public String name;
        public String desc;
        public Category(String name, String desc){
            this.uuid = "";
            this.name = name;
            this.desc = desc;
        }
        public Category(){
            name = "";
            desc = "";
        }
        public Category(String uuid,String name, String desc){
            this.uuid = uuid;
            this.name = name;
            this.desc = desc;
        }
    }

    public class Income{
        public String uuid;
        public double amount;
        public String desc;
        public Category category;
        public People people;
        public Date date;
        public Wallet wallet;
        public String notes;
        public Income(){
            this.uuid = "";
            this.amount = 0.0d;
            this.desc = "";
            this.category = new Category();
            this.people = new People();
            this.date = new Date();
            this.wallet = new Wallet();
            this.notes = "";
        }
        public Income(String uuid, double amount, String desc, Category category, People people, Date date, Wallet wallet, String notes) {
            this.uuid = uuid;
            this.amount = amount;
            this.desc = desc;
            this.category = category;
            this.people = people;
            this.date = date;
            this.wallet = wallet;
            this.notes = notes;
        }

    }


    public class Expense{
        public String uuid;
        public double amount;
        public String desc;
        public Category category;
        public People people;
        public Date date;
        public Wallet wallet;
        public String notes;
        public Expense(){
            this.uuid = "";
            this.amount = 0.0d;
            this.desc = "";
            this.category = new Category();
            this.people = new People();
            this.date = new Date();
            this.wallet = new Wallet();
            this.notes = "";
        }
        public Expense(String uuid, double amount, String desc, Category category, People people, Date date, Wallet wallet, String notes) {
            this.uuid = uuid;
            this.amount = amount;
            this.desc = desc;
            this.category = category;
            this.people = people;
            this.date = date;
            this.wallet = wallet;
            this.notes = notes;
        }
    }
    public class Transfer{
        public String uuid;
        public double amount;
        public String desc;
        public People people;
        public Date date;
        public Wallet fromWallet;
        public Wallet toWallet;
        public String notes;
        public Transfer(){
            this.uuid = "";
            this.amount = 0.0d;
            this.desc = "";
            this.people = new People();
            this.date = new Date();
            this.fromWallet = new Wallet();
            this.toWallet = new Wallet();
            this.notes = "";
        }
        public Transfer(String uuid, double amount, String desc, People people, Date date, Wallet fromWallet, Wallet toWallet, String notes) {
            this.uuid = uuid;
            this.amount = amount;
            this.desc = desc;
            this.people = people;
            this.date = date;
            this.fromWallet = fromWallet;
            this.toWallet = toWallet;
            this.notes = notes;
        }
    }

}
