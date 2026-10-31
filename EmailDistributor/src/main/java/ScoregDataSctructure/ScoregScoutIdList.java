package ScoregDataSctructure;

import java.util.ArrayList;
import java.util.List;

public class ScoregScoutIdList {

	private List<String> list;

	public List<String> getList() {
		if (list == null) {
			list = new ArrayList<>();
		}
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

}