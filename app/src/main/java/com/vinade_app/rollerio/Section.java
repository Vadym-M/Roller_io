package com.vinade_app.rollerio;

public class Section {
    private String img, nameSection;

    public Section() {
    }

    public Section(String img, String name) {
        this.img = img;
        this.nameSection = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNameSection() {
        return nameSection;
    }

    public void setNameSection(String name) {
        this.nameSection = name;
    }
}
