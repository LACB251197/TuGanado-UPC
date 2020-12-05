package com.upc.yourlivestock.models;

import java.util.List;

public class LivestockResponse {
    private Integer code;
    private String message;
    private List<Livestock> body;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Livestock> getBody() {
        return body;
    }

    public void setBody(List<Livestock> body) {
        this.body = body;
    }

    public class Livestock {

        private Long id;
        private Long codeNumber;
        private String photoPath;
        private String race;
        private String gender;
        private String color;
        private String years;
        private String owner;
        private String phoneNumber;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getCodeNumber() {
            return codeNumber;
        }

        public void setCodeNumber(Long codeNumber) {
            this.codeNumber = codeNumber;
        }

        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
        }

        public String getRace() {
            return race;
        }

        public void setRace(String race) {
            this.race = race;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getYears() {
            return years;
        }

        public void setYears(String years) {
            this.years = years;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
