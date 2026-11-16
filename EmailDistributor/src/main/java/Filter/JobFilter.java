package Filter;

import ScoregDataSctructure.ScoregMemberComplete;
import jakarta.annotation.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class JobFilter {

    public enum Job {
        ZBV, Leiter, Kassier, ERO, Elternratsmitglied, Gruppenleitung, GAB, Mitarbeiter, Stufenleitung
    }

    private final List<String> zbvFilter = List.of("ZBV");
    private final List<String> mitarbeiterFilter = List.of("MA");
    private final List<String> kassierFilter = List.of("KASS");
    private final List<String> eroFilter = List.of("ERO");
    private final List<String> elternratsmitgliedFilter = List.of("ER");
    private final List<String> gruppenleitungFilter = List.of("GL");
    private final List<String> gabFilter = List.of("GAB");

    private final List<String> leiterPrefixes = List.of("SF", "AS");
    private List<String> wiwoeLeiterFilter = new ArrayList<>();
    private List<String> guspLeiterFilter = new ArrayList<>();
    private List<String> caexLeiterFilter = new ArrayList<>();
    private List<String> raroLeiterFilter = new ArrayList<>();
    private List<String> leiterFilter = new ArrayList<>();

    JobFilter(Map<SectionFilter.Stufe, List<String>> stufenFilterMap) {
        BiConsumer<List<String>, List<String>> leiterFilterInitializer = (filter, list) ->
                filter.forEach(suffix -> leiterPrefixes.forEach(prefix -> list.add(prefix.concat(suffix))));

        stufenFilterMap.forEach((stufe, filter) -> {
            switch (stufe) {
                case WiWoe -> leiterFilterInitializer.accept(filter, wiwoeLeiterFilter);
                case GuSp -> leiterFilterInitializer.accept(filter, guspLeiterFilter);
                case CaEx -> leiterFilterInitializer.accept(filter, caexLeiterFilter);
                case RaRo -> leiterFilterInitializer.accept(filter, raroLeiterFilter);
            }
        });

        List<List<String>> combined = List.of(wiwoeLeiterFilter, guspLeiterFilter, caexLeiterFilter, raroLeiterFilter);
        combined.forEach(list -> leiterFilter.addAll(list));
    }

    public Boolean hasJob(ScoregMemberComplete memberComplete, Job job, SectionFilter.Stufe stufe) {
        return switch (job) {
            case ZBV -> isZBV(memberComplete);
            case Leiter -> isLeiter(memberComplete, stufe);
            case Kassier -> isKassier(memberComplete);
            case ERO -> isERO(memberComplete);
            case Elternratsmitglied -> isElternratsmitglied(memberComplete);
            case Gruppenleitung -> isGruppenleitung(memberComplete);
            case GAB -> isGAB(memberComplete);
            case Mitarbeiter -> isMitarbeiter(memberComplete);
            case Stufenleitung -> isStufenleitung(memberComplete) || isLeiter(memberComplete, stufe);
        };
    }

    public Boolean hasJob(ScoregMemberComplete memberComplete, Job job) {
        return switch (job) {
            case ZBV -> isZBV(memberComplete);
            case Leiter -> false;
            case Kassier -> isKassier(memberComplete);
            case ERO -> isERO(memberComplete);
            case Elternratsmitglied -> isElternratsmitglied(memberComplete);
            case Gruppenleitung -> isGruppenleitung(memberComplete);
            case GAB -> isGAB(memberComplete);
            case Mitarbeiter -> isMitarbeiter(memberComplete);
            case Stufenleitung -> isStufenleitung(memberComplete);
        };
    }


    private Boolean isZBV(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(zbvFilter::contains);
    }

    private Boolean isMitarbeiter(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(mitarbeiterFilter::contains);
    }

    private Boolean isGAB(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(gabFilter::contains);
    }

    private Boolean isGruppenleitung(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(gruppenleitungFilter::contains);
    }

    private Boolean isERO(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(eroFilter::contains);
    }

    private Boolean isElternratsmitglied(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(elternratsmitgliedFilter::contains);
    }

    private Boolean isKassier(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(kassierFilter::contains);
    }

    private Boolean isLeiter(ScoregMemberComplete member, SectionFilter.Stufe stufe) {
        List<String> jobList = Arrays.stream(member.getJobList().split(",")).toList();
        if (Objects.isNull(member.getJobList()) || Objects.isNull(stufe)) return false;
        return switch (stufe) {
            case WiWoe -> jobList.stream().anyMatch(wiwoeLeiterFilter::contains);
            case GuSp -> jobList.stream().anyMatch(guspLeiterFilter::contains);
            case CaEx -> jobList.stream().anyMatch(caexLeiterFilter::contains);
            case RaRo -> jobList.stream().anyMatch(raroLeiterFilter::contains);
            case ANY -> jobList.stream().anyMatch(leiterFilter::contains);
            case NONE -> false;
        };
    }

    private Boolean isStufenleitung(ScoregMemberComplete member) {
        List<String> jobList = Arrays.stream(member.getJobList().split(",")).toList();
        if (Objects.isNull(member.getJobList())) return false;
        return jobList.stream().anyMatch(s -> s.contains("SF"));
    }
}
