package com.templeosrs.util.favorite;

import java.io.Serializable;

public class TempleFavorite implements Serializable
{
	public TempleFavoriteType type;

	public String name;

	public String id;

	TempleFavorite(TempleFavoriteType type, String name, String id)
	{
		this.type = type;
		this.name = name;
		this.id = id;
	}

	TempleFavorite(TempleFavoriteType type, String name)
	{
		this.type = type;
		this.name = name;
	}
}
