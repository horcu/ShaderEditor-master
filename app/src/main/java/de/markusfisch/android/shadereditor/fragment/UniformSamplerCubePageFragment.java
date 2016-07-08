package de.markusfisch.android.shadereditor.fragment;

import de.markusfisch.android.shadereditor.activity.CubeMapActivity;
import de.markusfisch.android.shadereditor.app.ShaderEditorApplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class UniformSamplerCubePageFragment
	extends UniformSampler2dPageFragment
{
	@Override
	public void onCreate(Bundle state)
	{
		super.onCreate(state);
		setSamplerType( SamplerPropertiesFragment.SAMPLER_CUBE );
	}

	@Override
	protected void addTexture()
	{
		Activity activity = getActivity();

		if( activity == null )
			return;

		startActivity( new Intent(
			activity,
			CubeMapActivity.class ) );
	}

	@Override
	protected Cursor loadTextures()
	{
		return ShaderEditorApplication
			.dataSource
			.getSamplerCubeTextures();
	}
}
