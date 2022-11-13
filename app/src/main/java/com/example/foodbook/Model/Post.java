package com.example.foodbook.Model;

public class Post {
    private String postid;
    private String recipeimage;
    private String recipename;
    private String description;
    private String ingredients;
    private String publisher;

    public Post(String postid, String recipeimage, String recipename, String description, String ingredients, String publisher) {
        this.postid = postid;
        this.recipeimage = recipeimage;
        this.recipename = recipename;
        this.description = description;
        this.ingredients = ingredients;
        this.publisher = publisher;
    }
    public Post(){

    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getRecipeimage() {
        return recipeimage;
    }

    public void setRecipeimage(String recipeimage) {
        this.recipeimage = recipeimage;
    }

    public String getRecipename() {
        return recipename;
    }

    public void setRecipename(String recipename) {
        this.recipename = recipename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
