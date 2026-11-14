package MailList;

import Filter.FilterHelper;
import ScoregDataSctructure.ScoregMemberComplete;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailList {

    @Getter
    private final String name;
    private final FilterHelper.Distributor distributor;
    private final List<String> mailList;
    private final List<String> authorisations;

    public MailList(List<ScoregMemberComplete> memberCompletes, FilterHelper.Distributor distributor) {
        this.distributor = distributor;
        this.name = distributor.name();
        this.mailList = new ArrayList<>();
        this.authorisations = new ArrayList<>();

        FilterHelper filterHelper = new FilterHelper();
        List<ScoregMemberComplete> filtered = filterHelper.filterList(memberCompletes, distributor);
        filtered.forEach(s -> mailList.addAll(s.getRelevantEmails()));

        List<ScoregMemberComplete> authorisationFiltered = filterHelper.filterListForAuthorization(memberCompletes, distributor);
        authorisationFiltered.forEach(s -> authorisations.addAll(s.getRelevantEmails()));
    }

    private String generateDiscription(FilterHelper.Distributor distributor) {
        return switch (distributor) {
            case WiWö, GuSp, CaEx, RaRo -> "Schickt aus an alle " + distributor.name();
            case Info -> "Schickt aus an alle Gruppenmitglieder";
            case ScoutFix -> "Schickt aus an alle für Scout Fix relevante";
            case Gruppenrat -> "Schickt aus an alle GruppenratteilnehmerInnen";
            case Leiter -> "Schickt an aktive Leiter";
        };
    }

    public Map<String, Object> toJsonObject(){
        Map<String, Object> listDetails = new HashMap<>();

        listDetails.put("description", generateDiscription(distributor));
        listDetails.put("name", name);
        listDetails.put("allowed_senders", authorisations);
        listDetails.put("recipients", mailList);

        return listDetails;
    }
}
