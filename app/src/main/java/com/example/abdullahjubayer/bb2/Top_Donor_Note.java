package com.example.abdullahjubayer.bb2;

class Top_Donor_Note {

    String name;
    String image;
    String department;
    String batch;
    String gender;
    String blood_Group;
    String give_Blood;

    public Top_Donor_Note() {
    }

    public Top_Donor_Note(String name, String image, String department, String batch, String gender, String blood_Group, String give_Blood) {
        this.name = name;
        this.image = image;
        this.department = department;
        this.batch = batch;
        this.gender = gender;
        this.blood_Group = blood_Group;
        this.give_Blood = give_Blood;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDepartment() {
        return department;
    }

    public String getBatch() {
        return batch;
    }

    public String getGender() {
        return gender;
    }

    public String getBlood_Group() {
        return blood_Group;
    }

    public String getGive_Blood() {
        return give_Blood;
    }
}
