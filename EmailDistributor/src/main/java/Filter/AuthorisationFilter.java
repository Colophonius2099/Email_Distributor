package Filter;

import ScoregDataSctructure.ScoregMemberComplete;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AuthorisationFilter {

    private final JobFilter jobFilter;

    public AuthorisationFilter(JobFilter jobFilter) {
        this.jobFilter = jobFilter;
    }

    public Boolean hasSendRights(ScoregMemberComplete member, FilterHelper.Distributor distributor) {
        List<String> jobList = Arrays.stream(member.getJobList().split(",")).toList();
        if (Objects.isNull(member.getJobList())) return false;
        Boolean hasSendRight = switch (distributor) {
            case WiWoe -> jobFilter.hasJob(member, JobFilter.Job.Leiter, SectionFilter.Stufe.WiWoe);
            case GuSp -> jobFilter.hasJob(member, JobFilter.Job.Leiter, SectionFilter.Stufe.GuSp);
            case CaEx -> jobFilter.hasJob(member, JobFilter.Job.Leiter, SectionFilter.Stufe.CaEx);
            case RaRo -> jobFilter.hasJob(member, JobFilter.Job.Leiter, SectionFilter.Stufe.RaRo);
            case Gruppe -> jobFilter.hasJob(member, JobFilter.Job.ERO);
            case Gruppenrat -> jobFilter.hasJob(member, JobFilter.Job.ERO);
            case Leiter -> jobFilter.hasJob(member, JobFilter.Job.Leiter, SectionFilter.Stufe.ANY);
            case Elternrat -> jobFilter.hasJob(member, JobFilter.Job.ERO)
                    || jobFilter.hasJob(member, JobFilter.Job.Elternratsmitglied);
            case ScoutFix -> jobFilter.hasJob(member, JobFilter.Job.Kassier)
                    || jobFilter.hasJob(member, JobFilter.Job.Leiter, SectionFilter.Stufe.ANY)
                    || jobFilter.hasJob(member, JobFilter.Job.ERO)
                    || jobFilter.hasJob(member, JobFilter.Job.GAB)
                    || jobFilter.hasJob(member, JobFilter.Job.Gruppenleitung)
                    || jobFilter.hasJob(member, JobFilter.Job.ZBV)
                    || jobFilter.hasJob(member, JobFilter.Job.Mitarbeiter);
        };
        if (hasSendRight) {
            return true;
        } else {
            return jobFilter.hasJob(member, JobFilter.Job.Gruppenleitung);
        }
    }
}
