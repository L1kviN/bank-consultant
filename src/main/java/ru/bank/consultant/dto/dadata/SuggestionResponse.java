package ru.bank.consultant.dto.dadata;

import java.util.List;

public class SuggestionResponse {
    private List<Suggestion> suggestions;

    public List<Suggestion> getSuggestions() { return suggestions; }
    public void setSuggestions(List<Suggestion> suggestions) { this.suggestions = suggestions; }

    public static class Suggestion {
        private String value;
        private String unrestricted_value;
        private Data data;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getUnrestricted_value() { return unrestricted_value; }
        public void setUnrestricted_value(String unrestricted_value) { this.unrestricted_value = unrestricted_value; }
        public Data getData() { return data; }
        public void setData(Data data) { this.data = data; }
    }

    public static class Data {
        private String bic;
        private String inn;
        private String kpp;
        private String registration_number;
        private String correspondent_account;
        private Name name;
        private Address address;
        private State state;

        public String getBic() { return bic; }
        public void setBic(String bic) { this.bic = bic; }
        public String getInn() { return inn; }
        public void setInn(String inn) { this.inn = inn; }
        public String getKpp() { return kpp; }
        public void setKpp(String kpp) { this.kpp = kpp; }
        public String getRegistration_number() { return registration_number; }
        public void setRegistration_number(String registration_number) { this.registration_number = registration_number; }
        public String getCorrespondent_account() { return correspondent_account; }
        public void setCorrespondent_account(String correspondent_account) { this.correspondent_account = correspondent_account; }
        public Name getName() { return name; }
        public void setName(Name name) { this.name = name; }
        public Address getAddress() { return address; }
        public void setAddress(Address address) { this.address = address; }
        public State getState() { return state; }
        public void setState(State state) { this.state = state; }
    }

    public static class Name {
        private String payment;
        private String full;
        private String short_name;

        public String getPayment() { return payment; }
        public void setPayment(String payment) { this.payment = payment; }
        public String getFull() { return full; }
        public void setFull(String full) { this.full = full; }
        public String getShort_name() { return short_name; }
        public void setShort_name(String short_name) { this.short_name = short_name; }
    }

    public static class Address {
        private String value;
        private String postal_code;
        private String city;
        private String street;
        private String house;

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getPostal_code() { return postal_code; }
        public void setPostal_code(String postal_code) { this.postal_code = postal_code; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        public String getHouse() { return house; }
        public void setHouse(String house) { this.house = house; }
    }

    public static class State {
        private String status;
        private String actuality_date;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getActuality_date() { return actuality_date; }
        public void setActuality_date(String actuality_date) { this.actuality_date = actuality_date; }
    }
}