package Filter;


import ScoregDataSctructure.ScoregMemberComplete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SectionFilter {

    public enum Stufe {
        WiWoe, GuSp, CaEx, RaRo, ANY, NONE
    }

    private final List<String> wiwoeFilter = List.of("WI","WOE","WiWö");
    private final List<String> guspFilter = List.of("GU","SP","GuSp");
    private final List<String> caexFilter = List.of("CA","EX","CaEx");
    private final List<String> raroFilter = List.of("RA","RO","RaRo");
    private final Map<Stufe, List<String>> sectionFilterMap;

    SectionFilter() {
        sectionFilterMap = new HashMap<>();
        sectionFilterMap.put(Stufe.WiWoe, wiwoeFilter);
        sectionFilterMap.put(Stufe.GuSp, guspFilter);
        sectionFilterMap.put(Stufe.CaEx, caexFilter);
        sectionFilterMap.put(Stufe.RaRo, raroFilter);
    }

    Map<Stufe, List<String>> getSectionFilterMap() {
        return sectionFilterMap;
    }

    private Boolean isWiWoe(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getSection())) return false;
        return wiwoeFilter.contains(member.getSection());
    }

    private Boolean isCaEx(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getSection())) return false;
        return caexFilter.contains(member.getSection());
    }

    private Boolean isGuSp(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getSection())) return false;
        return guspFilter.contains(member.getSection());
    }

    private Boolean isRaRo(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getSection())) return false;
        return raroFilter.contains(member.getSection());
    }

    private Boolean isAny(ScoregMemberComplete member) {
        if (Objects.isNull(member) || Objects.isNull(member.getSection())) return false;
        return sectionFilterMap.values().stream().anyMatch(s -> s.contains(member.getSection()));
    }

    Boolean isStufe(ScoregMemberComplete member, Stufe stufe) {
       return switch (stufe) {
           case WiWoe -> isWiWoe(member);
           case GuSp -> isGuSp(member);
           case CaEx -> isCaEx(member);
           case RaRo -> isRaRo(member);
           case ANY -> isAny(member);
           case NONE -> false;
       };
    }

}
