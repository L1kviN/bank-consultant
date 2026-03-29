package ru.bank.consultant.dto.dadata;

public class IpLocationResponse {
    private Location location;
    private Data data;

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public static class Location {
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
        private String city;
        private String region;
        private String country;
        private String postal_code;

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getPostal_code() { return postal_code; }
        public void setPostal_code(String postal_code) { this.postal_code = postal_code; }
    }
}