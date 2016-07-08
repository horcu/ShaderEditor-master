package de.markusfisch.android.shadereditor.fragment;

import de.markusfisch.android.shadereditor.adapter.UniformPageAdapter;
import de.markusfisch.android.shadereditor.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UniformPagesFragment extends Fragment
{
	@Override
	public View onCreateView(
		LayoutInflater inflater,
		ViewGroup container,
		Bundle state )
	{
		Activity activity;

		if( (activity = getActivity()) == null )
			return null;

		View view;
		ViewPager viewPager;

		if( (view = inflater.inflate(
				R.layout.fragment_uniform_pages,
				container,
				false )) == null ||
			(viewPager = (ViewPager)view.findViewById(
				R.id.pager )) == null )
		{
			activity.finish();
			return null;
		}

		viewPager.setAdapter( new UniformPageAdapter(
			activity,
			getChildFragmentManager() ) );

		return view;
	}
}
