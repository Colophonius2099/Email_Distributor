package Filter;

import ScoregDataSctructure.ScoregMemberComplete;

import java.util.*;
import java.util.function.BiConsumer;

public class JobFilter {

    public enum Job {
        ZBV, Leiter, Kassier, ERO, Gruppenleitung, GAB, Mitarbeiter
    }

    private final List<String> zbvFilter = List.of("ZBV");
    private final List<String> mitarbeiterFilter = List.of("MA");
    private final List<String> kassierFilter = List.of("KASS");
    private final List<String> eroFilter = List.of("ERO");
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
        combined.forEach(list -> {
            leiterFilter.addAll(list);
        });
    }


    public Boolean isZBV(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(zbvFilter::contains);
    }

    public Boolean isMitarbeiter(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(mitarbeiterFilter::contains);
    }

    public Boolean isGAB(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(gabFilter::contains);
    }

    public Boolean isGruppenleitung(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(gruppenleitungFilter::contains);
    }

    public Boolean isERO(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(eroFilter::contains);
    }

    public Boolean isKassier(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getJobList())) return false;
        return Arrays.stream(member.getJobList().split(",")).anyMatch(kassierFilter::contains);
    }

    public Boolean isLeiter(ScoregMemberComplete member, SectionFilter.Stufe stufe) {
        List<String> jobList = Arrays.stream(member.getJobList().split(",")).toList();
        if (Objects.isNull(member.getJobList())) return false;
        return switch (stufe) {
            case WiWoe -> jobList.stream().anyMatch(wiwoeLeiterFilter::contains);
            case GuSp -> jobList.stream().anyMatch(guspLeiterFilter::contains);
            case CaEx -> jobList.stream().anyMatch(caexLeiterFilter::contains);
            case RaRo -> jobList.stream().anyMatch(raroLeiterFilter::contains);
            case ANY -> jobList.stream().anyMatch(leiterFilter::contains);
            case NONE -> false;
        };
    }

    public Boolean isGruppenratParticipant(ScoregMemberComplete member) {
        return isKassier(member)
                || isLeiter(member, SectionFilter.Stufe.ANY)
                || isERO(member)
                || isGAB(member)
                || isGruppenleitung(member);
    }
}
