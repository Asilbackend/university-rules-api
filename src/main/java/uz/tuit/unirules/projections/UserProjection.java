package uz.tuit.unirules.projections;

public interface UserProjection {
    String getFirstname();
    String getLastname();
    String getEmail();
    String getPhone();
    String getLanguage();
    boolean getPassedTest();
    Long getGroupId();
    String getRole();
}
