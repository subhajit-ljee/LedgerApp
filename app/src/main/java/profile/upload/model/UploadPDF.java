package profile.upload.model;

import java.util.Objects;

public class UploadPDF {

    private String id;
    private String timestamp;

    public UploadPDF() {
    }

    public UploadPDF(String id, String timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                Objects.equals(timestamp, uploadPDF.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp);
    }

    @Override
    public String toString() {
        return "UploadPDF{" +
                "id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
