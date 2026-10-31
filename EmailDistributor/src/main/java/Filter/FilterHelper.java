package Filter;

import Logger.MyLogger;
import ScoregDataSctructure.ScoregMemberComplete;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterHelper {

    public enum Distributor {
        WiWoe, GuSp, CaEx, RaRo, Info, ScoutFix, Gruppenrat
    }

    private final JobFilter jobFilter;
    private final SectionFilter sectionFilter;
    private final MyLogger logger;

    public FilterHelper() {
        this.sectionFilter = new SectionFilter();
        this.jobFilter = new JobFilter(sectionFilter.getSectionFilterMap());
        logger = new MyLogger("FilterHelper");
    }

    public JobFilter getJobFilter() {
        return jobFilter;
    }

    public SectionFilter getSectionFilter() {
        return sectionFilter;
    }

    public Boolean isStufe(ScoregMemberComplete member, SectionFilter.Stufe stufe) {
        return sectionFilter.isStufe(member,stufe) || jobFilter.isLeiter(member, stufe);
    }

    public List<ScoregMemberComplete> filterList (
            List<ScoregMemberComplete> list,
            Distributor distributor
    ) {
        logger.log("Creating Filter for: " + distributor.name());
        Predicate<ScoregMemberComplete> filter = generateFilter(distributor);
        return list.parallelStream().filter(filter).collect(Collectors.toList());
    }

    private Predicate<ScoregMemberComplete> generateFilter(Distributor distributor) {
        return switch (distributor) {
            case WiWoe -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.WiWoe);
            case GuSp -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.GuSp);
            case CaEx -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.CaEx);
            case RaRo -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.RaRo);
            case Gruppenrat -> memberComplete -> jobFilter.isGruppenratParticipant(memberComplete);
            case ScoutFix -> memberComplete -> jobFilter.isGruppenratParticipant(memberComplete)
                    || jobFilter.isZBV(memberComplete)
                    || jobFilter.isMitarbeiter(memberComplete);
            case Info -> memberComplete -> isStufe(memberComplete, SectionFilter.Stufe.ANY)
                    || jobFilter.isGruppenratParticipant(memberComplete)
                    || jobFilter.isZBV(memberComplete);
        };
    }
}
