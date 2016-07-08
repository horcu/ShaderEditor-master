package de.markusfisch.android.shadereditor.fragment;

import de.markusfisch.android.shadereditor.activity.AbstractSubsequentActivity;
import de.markusfisch.android.shadereditor.graphics.BitmapEditor;
import de.markusfisch.android.shadereditor.widget.CropImageView;
import de.markusfisch.android.shadereditor.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CropImageFragment extends Fragment
{
	public static final String IMAGE_URI = "image_uri";

	public interface CropImageViewProvider
	{
		CropImageView getCropImageView();
	}

	private static boolean inProgress = false;

	private CropImageView cropImageView;
	private View progressView;
	private Uri imageUri;
	private Bitmap bitmap;

	@Override
	public void onCreate( Bundle state )
	{
		super.onCreate( state );

		setHasOptionsMenu( true );
	}

	@Override
	public View onCreateView(
		LayoutInflater inflater,
		ViewGroup container,
		Bundle state )
	{
		Activity activity;

		if( (activity = getActivity()) == null )
			return null;

		activity.setTitle( R.string.crop_image );

		try
		{
			cropImageView = ((CropImageViewProvider)activity)
				.getCropImageView();
		}
		catch( ClassCastException e )
		{
			throw new ClassCastException(
				activity.toString()+
				" must implement "+
				"CropImageFragment.CropImageViewProvider" );
		}

		Bundle args;
		View view;

		if( cropImageView == null ||
			(args = getArguments()) == null ||
			(imageUri = args.getParcelable(
				IMAGE_URI )) == null ||
			(view = inflater.inflate(
				R.layout.fragment_crop_image,
				container,
				false )) == null ||
			(progressView = view.findViewById(
				R.id.progress_view )) == null )
		{
			activity.finish();
			return null;
		}

		// make cropImageView in activity visible (again)
		cropImageView.setVisibility( View.VISIBLE );

		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		loadBitmapAsync();
	}

	@Override
	public void onCreateOptionsMenu( Menu menu, MenuInflater inflater )
	{
		inflater.inflate(
			R.menu.fragment_crop_image,
			menu );
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		switch( item.getItemId() )
		{
			case R.id.crop:
				cropImage();
				return true;
			case R.id.rotate_clockwise:
				rotateClockwise();
				return true;
			default:
				return super.onOptionsItemSelected( item );
		}
	}

	private void loadBitmapAsync()
	{
		final Activity activity = getActivity();

		if( activity == null ||
			inProgress )
			return;

		inProgress = true;
		progressView.setVisibility( View.VISIBLE );

		new AsyncTask<Void, Void, Bitmap>()
		{
			@Override
			protected Bitmap doInBackground( Void... nothings )
			{
				return BitmapEditor.getBitmapFromUri(
					activity,
					imageUri,
					1024 );
			}

			@Override
			protected void onPostExecute( Bitmap bmp )
			{
				inProgress = false;
				progressView.setVisibility( View.GONE );

				if( bmp == null )
				{
					Toast.makeText(
						activity,
						R.string.cannot_pick_image,
						Toast.LENGTH_SHORT ).show();

					activity.finish();
					return;
				}

				bitmap = bmp;
				cropImageView.setImageBitmap( bitmap );
			}
		}.execute();
	}

	private void cropImage()
	{
		AbstractSubsequentActivity.addFragment(
			getFragmentManager(),
			Sampler2dPropertiesFragment.newInstance(
				imageUri,
				cropImageView.getNormalizedRectInBounds(),
				cropImageView.getImageRotation() ) );

		bitmap.recycle();
		bitmap = null;

		cropImageView.setImageBitmap( null );
		cropImageView.setVisibility( View.GONE );
	}

	private void rotateClockwise()
	{
		cropImageView.setImageRotation(
			(cropImageView.getImageRotation()+90) % 360 );
	}
}
