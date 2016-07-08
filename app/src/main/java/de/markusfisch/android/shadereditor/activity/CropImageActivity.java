package de.markusfisch.android.shadereditor.activity;

import de.markusfisch.android.shadereditor.fragment.CropImageFragment;
import de.markusfisch.android.shadereditor.widget.CropImageView;
import de.markusfisch.android.shadereditor.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class CropImageActivity
	extends AbstractSubsequentActivity
	implements CropImageFragment.CropImageViewProvider
{
	private CropImageView cropImageView;

	public static Intent getIntentForImage(
		Context context,
		Uri imageUri )
	{
		Intent intent = new Intent(
			context,
			CropImageActivity.class );

		intent.putExtra(
			CropImageFragment.IMAGE_URI,
			imageUri );

		return intent;
	}

	@Override
	public CropImageView getCropImageView()
	{
		return cropImageView;
	}

	@Override
	protected void onCreate( Bundle state )
	{
		super.onCreate( state );
		setContentView( R.layout.activity_crop_image );

		if( (cropImageView = (CropImageView)findViewById(
			R.id.crop_image_view )) == null )
		{
			finish();
			return;
		}

		MainActivity.initSystemBars( this );
		AbstractSubsequentActivity.initToolbar( this );

		if( state == null )
			setFragmentForIntent(
				new CropImageFragment(),
				getIntent() );
	}
}
