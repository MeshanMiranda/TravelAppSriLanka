package com.example.travel.Domain;

import java.io.Serializable;

public class ItemDomain implements Serializable {
    private String title;
    private String address;
    private String description;
    private String pic;
    private String direction;
    private String timeTour;
    private String dateTour;
    private String tourGuideName;
    private String tourGuidePhone;
    private String tourGuidePic;
    private int price ;
    private float priceUSD ;
    private int bed;
    private String distance;
    private double score;
    private String id;

    public ItemDomain() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String duration) {
        this.direction = direction;
    }

    public String getTimeTour() {
        return timeTour;
    }

    public void setTimeTour(String timeTour) {
        this.timeTour = timeTour;
    }

    public String getDateTour() {
        return dateTour;
    }

    public void setDateTour(String dateTour) {
        this.dateTour = dateTour;
    }

    public String getTourGuideName() {
        return tourGuideName;
    }

    public void setTourGuideName(String tourGuideName) {
        this.tourGuideName = tourGuideName;
    }

    public String getTourGuidePhone() {
        return tourGuidePhone;
    }

    public void setTourGuidePhone(String tourGuidePhone) {
        this.tourGuidePhone = tourGuidePhone;
    }

    public String getTourGuidePic() {
        return tourGuidePic;
    }

    public void setTourGuidePic(String tourGuidePic) {
        this.tourGuidePic = tourGuidePic;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public float getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(float priceUSD) {
        this.priceUSD = priceUSD;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getId() {
        return id;

    }
}
