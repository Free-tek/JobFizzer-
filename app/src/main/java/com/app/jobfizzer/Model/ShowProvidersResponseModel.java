package com.app.jobfizzer.Model;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.app.jobfizzer.R;

import java.io.Serializable;
import java.util.List;

public class ShowProvidersResponseModel implements Serializable {

    private final static long serialVersionUID = 6242212727340814878L;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("all_providers")
    @Expose
    private List<ProviderService.AllProvider> allProviders = null;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<ProviderService.AllProvider> getAllProviders() {
        return allProviders;
    }

    public void setAllProviders(List<ProviderService.AllProvider> allProviders) {
        this.allProviders = allProviders;
    }

    public class Review implements Serializable {

        private final static long serialVersionUID = 7108814298294639375L;
        @SerializedName("providername")
        @Expose
        private String providername;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("feedback")
        @Expose
        private String feedback;
        @SerializedName("rating")
        @Expose
        private Integer rating;

        public String getProvidername() {
            return providername;
        }

        public void setProvidername(String providername) {
            this.providername = providername;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

    }

    public class ProviderService implements Serializable {

        private final static long serialVersionUID = -7084740791699318966L;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sub_category_name")
        @Expose
        private String subCategoryName;
        @SerializedName("quickpitch")
        @Expose
        private String quickpitch;
        @SerializedName("priceperhour")
        @Expose
        private String priceperhour;
        @SerializedName("experience")
        @Expose
        private String experience;


        String displayPricePerhour;

        public String getDisplayPricePerhour(Context context) {
            displayPricePerhour=context.getString(R.string.currency_symbol)+ getPriceperhour() + " " + context.getResources().getString(R.string.price_per_hour);
            return displayPricePerhour;
        }

        public void setDisplayPricePerhour(String displayPricePerhour) {
            this.displayPricePerhour = displayPricePerhour;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSubCategoryName() {
            return subCategoryName;
        }

        public void setSubCategoryName(String subCategoryName) {
            this.subCategoryName = subCategoryName;
        }

        public String getQuickpitch() {
            return quickpitch;
        }

        public void setQuickpitch(String quickpitch) {
            this.quickpitch = quickpitch;
        }

        public String getPriceperhour() {
            return priceperhour;
        }

        public void setPriceperhour(String priceperhour) {
            this.priceperhour = priceperhour;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public class AllProvider implements Serializable {

            private final static long serialVersionUID = -7533015361397238312L;
            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("mobile")
            @Expose
            private long mobile;
            @SerializedName("latitude")
            @Expose
            private Double latitude;
            @SerializedName("longitude")
            @Expose
            private Double longitude;
            @SerializedName("about")
            @Expose
            private String about;
            @SerializedName("distance")
            @Expose
            private Double distance;
            @SerializedName("addressline1")
            @Expose
            private String addressline1;
            @SerializedName("addressline2")
            @Expose
            private String addressline2;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("state")
            @Expose
            private String state;
            @SerializedName("zipcode")
            @Expose
            private String zipcode;
            @SerializedName("quickpitch")
            @Expose
            private String quickpitch;
            @SerializedName("priceperhour")
            @Expose
            private String priceperhour;
            @SerializedName("experience")
            @Expose
            private String experience;
            @SerializedName("avg_rating")
            @Expose
            private float avgRating;
            @SerializedName("reviews")
            @Expose
            private List<Review> reviews = null;
            @SerializedName("provider_services")
            @Expose
            private List<ProviderService> providerServices = null;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public long getMobile() {
                return mobile;
            }

            public void setMobile(long mobile) {
                this.mobile = mobile;
            }

            public Double getLatitude() {
                return latitude;
            }

            public void setLatitude(Double latitude) {
                this.latitude = latitude;
            }

            public Double getLongitude() {
                return longitude;
            }

            public void setLongitude(Double longitude) {
                this.longitude = longitude;
            }

            public String getAbout() {
                return about;
            }

            public void setAbout(String about) {
                this.about = about;
            }

            public Double getDistance() {
                return distance;
            }

            public void setDistance(Double distance) {
                this.distance = distance;
            }

            public String getAddressline1() {
                return addressline1;
            }

            public void setAddressline1(String addressline1) {
                this.addressline1 = addressline1;
            }

            public String getAddressline2() {
                return addressline2;
            }

            public void setAddressline2(String addressline2) {
                this.addressline2 = addressline2;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getZipcode() {
                return zipcode;
            }

            public void setZipcode(String zipcode) {
                this.zipcode = zipcode;
            }

            public String getQuickpitch() {
                return quickpitch;
            }

            public void setQuickpitch(String quickpitch) {
                this.quickpitch = quickpitch;
            }

            public String getPriceperhour() {
                return priceperhour;
            }

            public void setPriceperhour(String priceperhour) {
                this.priceperhour = priceperhour;
            }

            public String getExperience() {
                return experience;
            }

            public void setExperience(String experience) {
                this.experience = experience;
            }

            public float getAvgRating() {
                return avgRating;
            }

            public void setAvgRating(float avgRating) {
                this.avgRating = avgRating;
            }

            public List<Review> getReviews() {
                return reviews;
            }

            public void setReviews(List<Review> reviews) {
                this.reviews = reviews;
            }

            public List<ProviderService> getProviderServices() {
                return providerServices;
            }

            public void setProviderServices(List<ProviderService> providerServices) {
                this.providerServices = providerServices;
            }

        }
    }


}
