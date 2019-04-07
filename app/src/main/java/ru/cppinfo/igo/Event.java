package ru.cppinfo.igo;

public class Event {
    private String title;
    private String organizatorName;
    private String description;
    private String date;
    private String minimalAge;
    private String mapPosition;
    private String images;

    public Event(String title, String organizatorName, String description, String date, String minimalAge, String mapPosition, String images) {
        this.title = title;
        this.organizatorName = organizatorName;
        this.description = description;
        this.date = date;
        this.minimalAge = minimalAge;
        this.mapPosition = mapPosition;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganizatorName() {
        return organizatorName;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getMinimalAge() {
        return minimalAge;
    }

    public String getMapPosition() {
        return mapPosition;
    }

    public String getImages() {
        return images;
    }
}
