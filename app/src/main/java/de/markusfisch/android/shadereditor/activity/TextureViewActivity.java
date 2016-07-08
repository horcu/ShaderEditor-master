package de.markusfisch.android.shadereditor.activity;

import de.markusfisch.android.shadereditor.fragment.TextureViewFragment;
import de.markusfisch.android.shadereditor.widget.ScalingImageView;
import de.markusfisch.android.shadereditor.R;

import android.os.Bundle;

public class TextureViewActivity
	extends AbstractSubsequentActivity
	implements TextureViewFragment.ScalingImageViewProvider
{
	private ScalingImageView scalingImageView;

	@Override
	public ScalingImageView getScalingImageView()
	{
		return scalingImageView;
	}

	@Override
	protected void onCreate( Bundle state )
	{
		super.onCreate( state );
		setContentView( R.layout.activity_view_texture );

		if( (scalingImageView = (ScalingImageView)findViewById(
			R.id.scaling_image_view )) == null )
		{
			finish();
			return;
		}

		MainActivity.initSystemBars( this );
		AbstractSubsequentActivity.initToolbar( this );

		if( state == null )
			setFragmentForIntent(
				new TextureViewFragment(),
				getIntent() );
	}
}
