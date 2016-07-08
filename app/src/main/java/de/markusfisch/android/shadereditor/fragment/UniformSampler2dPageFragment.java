package de.markusfisch.android.shadereditor.fragment;

import de.markusfisch.android.shadereditor.activity.AddUniformActivity;
import de.markusfisch.android.shadereditor.activity.TextureViewActivity;
import de.markusfisch.android.shadereditor.adapter.TextureAdapter;
import de.markusfisch.android.shadereditor.app.ShaderEditorApplication;
import de.markusfisch.android.shadereditor.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class UniformSampler2dPageFragment extends Fragment
{
	private ListView listView;
	private TextureAdapter texturesAdapter;
	private View progressBar;
	private View noTexturesMessage;
	private String samplerType = SamplerPropertiesFragment.SAMPLER_2D;

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
		View fab;

		if( (view = inflater.inflate(
				R.layout.fragment_uniform_sampler_2d_page,
				container,
				false )) == null ||
			(fab = view.findViewById(
				R.id.add_texture )) == null ||
			(listView = (ListView)view.findViewById(
				R.id.textures )) == null ||
			(progressBar = view.findViewById(
				R.id.progress_bar )) == null ||
			(noTexturesMessage = view.findViewById(
				R.id.no_textures_message )) == null )
		{
			activity.finish();
			return null;
		}

		fab.setOnClickListener(
			new View.OnClickListener()
			{
				@Override
				public void onClick( View v )
				{
					addTexture();
				}
			} );

		initListView( view );

		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		loadTexturesAsync( getActivity() );
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

		if( texturesAdapter != null )
		{
			texturesAdapter.changeCursor( null );
			texturesAdapter = null;
		}
	}

	public void setSamplerType( String type )
	{
		samplerType = type;
	}

	protected void addTexture()
	{
		Activity activity = getActivity();

		if( activity == null )
			return;

		Intent intent = new Intent( Intent.ACTION_GET_CONTENT );

		intent.setType( "image/*" );

		// use Activity.startActivityForResult() to keep
		// requestCode; Fragment.startActivityForResult()
		// will modify the requestCode
		activity.startActivityForResult(
			Intent.createChooser(
				intent,
				getString( R.string.choose_image ) ),
			AddUniformActivity.PICK_IMAGE );
	}

	protected Cursor loadTextures()
	{
		return ShaderEditorApplication
			.dataSource
			.getTextures();
	}

	protected void showTexture( long id )
	{
		Activity activity = getActivity();

		if( activity == null )
			return;

		Intent intent = new Intent(
			activity,
			TextureViewActivity.class );

		intent.putExtra( TextureViewFragment.TEXTURE_ID, id );
		intent.putExtra( TextureViewFragment.SAMPLER_TYPE, samplerType );

		// use Activity.startActivityForResult() to keep
		// requestCode; Fragment.startActivityForResult()
		// will modify the requestCode
		activity.startActivityForResult(
			intent,
			AddUniformActivity.PICK_TEXTURE );
	}

	private void loadTexturesAsync( final Context context )
	{
		if( context == null )
			return;

		new AsyncTask<Void, Void, Cursor>()
		{
			@Override
			protected Cursor doInBackground( Void... nothings )
			{
				return loadTextures();
			}

			@Override
			protected void onPostExecute( Cursor cursor )
			{
				if( cursor == null )
					return;

				updateAdapter( context, cursor );
			}
		}.execute();
	}

	private void updateAdapter( Context context, Cursor cursor )
	{
		if( texturesAdapter != null )
		{
			texturesAdapter.changeCursor( cursor );
			texturesAdapter.notifyDataSetChanged();
		}
		else
		{
			texturesAdapter = new TextureAdapter(
				context,
				cursor );

			listView.setAdapter( texturesAdapter );
		}

		if( cursor.getCount() < 1 )
		{
			progressBar.setVisibility( View.GONE );
			noTexturesMessage.setVisibility( View.VISIBLE );
		}
	}

	private void initListView( View view )
	{
		listView.setEmptyView( view.findViewById( R.id.no_textures ) );
		listView.setOnItemClickListener(
			new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(
					AdapterView<?> parent,
					View view,
					int position,
					long id )
				{
					showTexture( id );
				}
			} );
	}
}
