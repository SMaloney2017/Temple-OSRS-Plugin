package com.templeosrs.util.sync;

import com.google.gson.annotations.SerializedName;

public class TempleSyncResponseData
{
	@SerializedName("added_names")
	public Integer addedNames;

	@SerializedName("removed_names")
	public Integer removedNames;

	@SerializedName("old_member_count")
	public Integer oldMemberCount;

	@SerializedName("new_member_count")
	public Integer newMemberCount;
}
