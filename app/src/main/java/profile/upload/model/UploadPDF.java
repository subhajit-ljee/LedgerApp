package profile.upload.model;

import java.util.Objects;

public class UploadPDF {

    private String id;
    private String name;
    private String url;
    private String timestamp;

    public UploadPDF(String id, String name, String url, String timestamp) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UploadPDF uploadPDF = (UploadPDF) o;
        return Objects.equals(id, uploadPDF.id) &&
                Objects.equals(name, uploadPDF.name) &&
                Objects.equals(url, uploadPDF.url) &&
                Objects.equals(timestamp, uploadPDF.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, timestamp);
    }

    @Override
    public String toString() {
        return "UploadPDF{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
