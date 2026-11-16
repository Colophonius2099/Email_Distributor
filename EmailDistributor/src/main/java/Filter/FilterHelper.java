package Filter;

import Logger.MyLogger;
import ScoregDataSctructure.ScoregMemberComplete;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterHelper {

    public enum Distributor {
        WiWoe, GuSp, CaEx, RaRo, Gruppe, ScoutFix, Gruppenrat, Leiter, Elternrat
    }

    private final JobFilter jobFilter;
    private final SectionFilter sectionFilter;
    private final AuthorisationFilter authorisationFilter;

    private final MyLogger logger;

    public FilterHelper() {
        this.sectionFilter = new SectionFilter();
        this.jobFilter = new JobFilter(sectionFilter.getSectionFilterMap());
        this.authorisationFilter = new AuthorisationFilter(jobFilter);
        logger = new MyLogger("FilterHelper");
    }

    public Boolean isStufe(ScoregMemberComplete member, SectionFilter.Stufe stufe) {
        return sectionFilter.isStufe(member,stufe) || jobFilter.hasJob(member, JobFilter.Job.Leiter, stufe);
    }

    public Boolean isTeilnehmer(ScoregMemberComplete member) {
        return sectionFilter.isStufe(member, SectionFilter.Stufe.ANY);
    }

    public List<ScoregMemberComplete> filterList (
            List<ScoregMemberComplete> list,
            Distributor distributor
    ) {
        logger.log("Creating Filter for: " + distributor.name());
        Predicate<ScoregMemberComplete> filter = generateFilter(distributor);
        return list.parallelStream().filter(filter).collect(Collectors.toList());
    }

    public List<ScoregMemberComplete> filterListForAuthorization(
            List<ScoregMemberComplete> list,
            Distributor distributor
    ) {
        Predicate<ScoregMemberComplete> filter = generateAuthorizationFilter(distributor);
        return list.parallelStream().filter(filter).collect(Collectors.toList());
    }

    private Predicate<ScoregMemberComplete> generateFilter(Distributor distributor) {
        return switch (distributor) {
            case Gruppe -> memberComplete -> true; // schickt an alle
            case WiWoe -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.WiWoe);
            case GuSp -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.GuSp);
            case CaEx -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.CaEx);
            case RaRo -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.RaRo);
            case Gruppenrat -> memberComplete -> isGruppenratParticipant(memberComplete);
            case ScoutFix -> memberComplete -> isGruppenratParticipant(memberComplete)
                    || jobFilter.hasJob(memberComplete, JobFilter.Job.ZBV)
                    || jobFilter.hasJob(memberComplete, JobFilter.Job.Mitarbeiter);
            case Leiter -> memberComplete ->  jobFilter.hasJob(memberComplete, JobFilter.Job.Leiter, SectionFilter.Stufe.ANY);
            case Elternrat -> memberComplete -> jobFilter.hasJob(memberComplete, JobFilter.Job.ERO)
                    || jobFilter.hasJob(memberComplete, JobFilter.Job.Elternratsmitglied)
                    || jobFilter.hasJob(memberComplete, JobFilter.Job.Gruppenleitung);
        };
    }

    private Predicate<ScoregMemberComplete> generateAuthorizationFilter(Distributor distributor) {
        return memberComplete -> authorisationFilter.hasSendRights(memberComplete, distributor);
    }

    private Boolean isGruppenratParticipant(ScoregMemberComplete member) {
        return jobFilter.hasJob(member, JobFilter.Job.Kassier)
                || jobFilter.hasJob(member, JobFilter.Job.Leiter, SectionFilter.Stufe.ANY)
                || jobFilter.hasJob(member, JobFilter.Job.ERO)
                || jobFilter.hasJob(member, JobFilter.Job.GAB)
                || jobFilter.hasJob(member, JobFilter.Job.Gruppenleitung);
    }
}
