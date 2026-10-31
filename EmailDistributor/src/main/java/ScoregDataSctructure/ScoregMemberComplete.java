package ScoregDataSctructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoregMemberComplete {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private String scoutId;
	private String prefixTitle;
	private String postfixTitle;
	private String firstname;
	private String secondname;
	private String lastname;
	private String emailPrimary;
	private String emailSecondary;
	private String section;
	private String jobList;
	private ScoregMemberParents parents;

	public String getScoutId() {
		return scoutId;
	}
	public String getPrefixTitle() {
		return prefixTitle;
	}
	public String getPostfixTitle() {
		return postfixTitle;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getSecondname() {
		return secondname;
	}
	public String getLastname() {
		return lastname;
	}
	public String getEmailPrimary() {
		return emailPrimary;
	}
	public String getEmailSecondary() {
		return emailSecondary;
	}
	public ScoregMemberParents getParents() {
		return parents;
	}
	public String getSection() {
		return section;
	}
	public String getJobList() {
		return jobList;
	}

	public List<String> getRelevantEmails() {
		return java.util.stream.Stream.of(
						emailPrimary,
						emailSecondary,
						(parents != null) && shouldUseParentEmail() ? parents.getFatherEmail() : null,
						(parents != null) && shouldUseParentEmail() ? parents.getMotherEmail() : null
				)
				.filter(Objects::nonNull)
				.filter(s -> !s.isBlank())
				.collect(Collectors.toList());
	}

	private Boolean shouldUseParentEmail() {
		if (Objects.nonNull(emailPrimary) && Objects.nonNull(emailSecondary)) {
			return emailPrimary.isBlank() && emailSecondary.isBlank();
		}
		return true;
	}

	@JsonSetter("parents")
	public void setParents(JsonNode node) {
		if (node == null || node.isNull()
				|| (node.isString() && node.asString().isEmpty())) {
			this.parents = null;
		} else {
			this.parents = MAPPER.treeToValue(node, ScoregMemberParents.class);
		}
	}
	
}
