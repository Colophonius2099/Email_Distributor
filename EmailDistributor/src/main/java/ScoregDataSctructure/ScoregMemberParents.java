package ScoregDataSctructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoregMemberParents {
    private String fatherEmail;
    private String fatherName;
    private String motherEmail;
    private String motherName;

    public String getFatherEmail() {
        return fatherEmail;
    }
    public String getFatherName() {
        return fatherName;
    }
    public String getMotherEmail() {
        return motherEmail;
    }
    public String getMotherName() {
        return motherName;
    }

}
