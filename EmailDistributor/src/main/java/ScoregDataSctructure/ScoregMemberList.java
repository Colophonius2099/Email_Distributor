package ScoregDataSctructure;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DI(FH) Hubert Novak
 * @date 14.02.2014
 * 
 */

@XmlRootElement(name="ScoregMemberList")
public class ScoregMemberList {

	private List<ScoregMember> list;

	public List<ScoregMember> getList() {
		if(list == null)
		{
			list = new ArrayList<>();
		}
		return list;
	}

	public void setList(List<ScoregMember> list) {
		this.list = list;
	}
	
}
