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
            case WiWö -> jobFilter.isLeiter(member, SectionFilter.Stufe.WiWoe) && jobFilter.isStufenleitung(member);
            case GuSp -> jobFilter.isLeiter(member, SectionFilter.Stufe.GuSp) && jobFilter.isStufenleitung(member);
            case CaEx -> jobFilter.isLeiter(member, SectionFilter.Stufe.CaEx) && jobFilter.isStufenleitung(member);
            case RaRo -> jobFilter.isLeiter(member, SectionFilter.Stufe.RaRo) && jobFilter.isStufenleitung(member);
            case Info -> false;
            case ScoutFix -> jobFilter.isLeiter(member, SectionFilter.Stufe.ANY);
            case Gruppenrat -> false;
            case Leiter -> jobFilter.isLeiter(member, SectionFilter.Stufe.ANY);
        };
        if (hasSendRight) {
            return true;
        } else {
            return jobFilter.isGruppenleitung(member);
        }
    }
}
