package com.newnius.code4storm.marmot.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by newnius on 11/17/16.
 *
 */
public class Author implements Serializable{
    private static final long serialVersionUID = -3059114652157813120L;

    private Integer authorId;

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    private String book_name;
    private List<String> places_of_residence;
    private List<String> date_of_birth;

    private String reviews;

    private String members;

    private List<String> education;

    private List<String> date_of_death;

    private List<String> nationality;

    private String favorited;

    private List<String> relationships;

    private List<String> country;

    private List<String> place_of_death;

    private List<String> tags;

    private List<String> disambiguation_notice;

    private List<String> burial_location;

    private List<String> short_biography;

    private String rating;

    private String book_author;

    private List<String> birthplace;

    private List<String> awards_an_honors;

    private List<String> legal_name;

    private List<String> gender;

    private List<String> organizations;

    private String popularity;

    private List<String> book_id;

    private String events;

    private List<String> canonical_name;

    private List<String> occupations;

    private List<String> other_names;

    private List<String> agents;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public List<String> getPlaces_of_residence() {
        return places_of_residence;
    }

    public void setPlaces_of_residence(List<String> places_of_residence) {
        this.places_of_residence = places_of_residence;
    }

    public List<String> getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(List<String> date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getDate_of_death() {
        return date_of_death;
    }

    public void setDate_of_death(List<String> date_of_death) {
        this.date_of_death = date_of_death;
    }

    public List<String> getNationality() {
        return nationality;
    }

    public void setNationality(List<String> nationality) {
        this.nationality = nationality;
    }

    public String getFavorited() {
        return favorited;
    }

    public void setFavorited(String favorited) {
        this.favorited = favorited;
    }

    public List<String> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<String> relationships) {
        this.relationships = relationships;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public List<String> getPlace_of_death() {
        return place_of_death;
    }

    public void setPlace_of_death(List<String> place_of_death) {
        this.place_of_death = place_of_death;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getDisambiguation_notice() {
        return disambiguation_notice;
    }

    public void setDisambiguation_notice(List<String> disambiguation_notice) {
        this.disambiguation_notice = disambiguation_notice;
    }

    public List<String> getBurial_location() {
        return burial_location;
    }

    public void setBurial_location(List<String> burial_location) {
        this.burial_location = burial_location;
    }

    public List<String> getShort_biography() {
        return short_biography;
    }

    public void setShort_biography(List<String> short_biography) {
        this.short_biography = short_biography;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public List<String> getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(List<String> birthplace) {
        this.birthplace = birthplace;
    }

    public List<String> getAwards_an_honors() {
        return awards_an_honors;
    }

    public void setAwards_an_honors(List<String> awards_an_honors) {
        this.awards_an_honors = awards_an_honors;
    }

    public List<String> getLegal_name() {
        return legal_name;
    }

    public void setLegal_name(List<String> legal_name) {
        this.legal_name = legal_name;
    }

    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public List<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public List<String> getBook_id() {
        return book_id;
    }

    public void setBook_id(List<String> book_id) {
        this.book_id = book_id;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public List<String> getCanonical_name() {
        return canonical_name;
    }

    public void setCanonical_name(List<String> canonical_name) {
        this.canonical_name = canonical_name;
    }

    public List<String> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<String> occupations) {
        this.occupations = occupations;
    }

    public List<String> getOther_names() {
        return other_names;
    }

    public void setOther_names(List<String> other_names) {
        this.other_names = other_names;
    }

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }
}
