package uz.tuit.unirules.projections;

public interface ModuleUserProjection {
    Long getModuleId();

    String getModuleName();

    String getModuleDescription();

    Integer getModuleTestResult();

    String getUserModuleStatus();// PENDING, COMPLETED

}
