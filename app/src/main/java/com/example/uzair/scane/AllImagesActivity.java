package com.example.uzair.scane;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import Controlers.RecyclerItemTouchHelper;
import Controlers.RecyclerTouchListener;

public class AllImagesActivity extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private List<ImageDetails> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageAdapter mAdapter;
    private boolean listing_Enabled;
    private ActionMode actionMode;

    private void openThisImage(int position) {
        ImageDetails movie = list.get(position);
        if(movie.getUri().toString().indexOf("pdf")!=-1)
            viewPdf(movie.getUri());

        else
        {
            Intent i=new Intent(AllImagesActivity.this,main_dashbord.class);
            i.putExtra("Uri",movie.getUri());
            i.setData(movie.getUri());
            startActivity(i);
        }
    }

    private void checkedItem(int position) {
        list.get(position).setChecked(!list.get(position).isChecked());
        //actionMode.setCustomView();
        if(getSelectedImagesCount()==0 && actionMode!=null)
        {
            actionMode.finish();
        }
        mAdapter.notifyItemChanged(position);
        Log.d("clicked image",list.get((position)).getName());
    }

    private void deSelectAll() {
        int c=0;
        for (Iterator<ImageDetails> it = list.iterator(); it.hasNext(); ) {
            ImageDetails i = it.next();
            if (i.isChecked()) {
                i.setChecked(false);
                mAdapter.notifyItemChanged(c);
            }
            c++;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        prepareImageData();

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        //if(true)return;
        if (viewHolder instanceof ImageAdapter.ImageViewHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final ImageDetails deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            //Log.d("details",""+viewHolder.getAdapterPosition()+" "+list.size());
            ImageDetails d=list.get(viewHolder.getAdapterPosition());
            Uri uri=d.getUri();
            final File f=new File(URI.create(uri.toString()));
            final Timer timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(f.exists()) {
                        Log.d("asyn","file deleted");
                        f.delete();
                    }else
                    Log.d("asyn","file not deleted");
                }
            },3500);
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.constrain_layout)
                            , name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timer.cancel();
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(AllImagesActivity.this).inflate(R.menu.image_list_gallery,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_all:
            {

                final AlertDialog.Builder builder = new AlertDialog.Builder(AllImagesActivity.this);
                builder.setTitle("Are you sure want to delete all images?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Iterator<ImageDetails> it = list.iterator(); it.hasNext(); ) {
                                ImageDetails i = it.next();
                                Uri uri = i.getUri();
                                File f = new File(URI.create(uri.toString()));
                                f.delete();

                        }
                        list.clear();
                        mAdapter.notifyDataSetChanged();
                        }
                }).show();

            }
            break;
            case R.id.shere_:break;
            case R.id.delete_button_item:


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images);
        listing_Enabled=false;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ImageAdapter(list,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position,float X) {

//                for (Iterator<ImageDetails> it = list.iterator(); it.hasNext(); ) {
//                    ImageDetails i = it.next();
//                    if(i.isChecked())
//                        Log.d(i.getName(),"Checked");
//                }
                if(listing_Enabled) {
                    checkedItem(position);
                    //mAdapter.notifyItemChanged(position);
                }
                else
                    openThisImage(position);
                //Toast.makeText(getApplicationContext(), movie() + " is selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, int position) {
                if(!listing_Enabled){
                    AllImagesActivity.this.startActionMode(callActionMode);
                    listing_Enabled=true;
                    //mAdapter.notifyItemChanged(position);
                }
                checkedItem(position);
            }
        }));

    }


    private void prepareImageData() {
        ImageDetails movie;
        List l = getFilesFromDir(main_dashbord.APP_FOLDER_ABSOLUTE_PATH);
        ListIterator it = l.listIterator();
        while (it.hasNext()) {
            File f = (File) it.next();
            movie = new ImageDetails(Uri.parse(f.toURI().toString()));
            movie.setName(f.getName());
            movie.setSize("" + (f.length() / 1000) + "KB");//"ssmmHHddMMyy"
            movie.setCreated(new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date(f.lastModified())));
            list.add(movie);

        }

        mAdapter.notifyDataSetChanged();

    }


    public static List getFilesFromDir(File aStartingDir) {
        List result = new ArrayList();
        File[] filesAndDirs = aStartingDir.listFiles();
        return Arrays.asList(filesAndDirs);
    }

    private void ShereImage(Uri uri)
    {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }
    public static void share(AppCompatActivity context,List<ImageDetails> paths) {

        if (paths == null || paths.size() == 0) {
            return;
        }
        ArrayList<Uri> uris = new ArrayList<>();
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        for (ImageDetails imageDetails : paths) {
            File file = new File(URI.create(imageDetails.getUri().toString()));
            uris.add(Uri.fromFile(file));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(intent);
    }

    private int getSelectedImagesCount()
    {
        int count=0;
        for (Iterator<ImageDetails> it = list.iterator(); it.hasNext(); ) {
            ImageDetails i = it.next();
            if(i.isChecked()) {
                Log.d(i.getName(), "Checked");
                count++;
            }
        }
        return count;
    }
    private void deleteSelectedImages()
    {
        int c=0;
        for (Iterator<ImageDetails> it = list.iterator(); it.hasNext(); ) {
            ImageDetails i = it.next();
            if (i.isChecked()) {
                Log.d(i.getName(), "deleting");
                Uri uri = i.getUri();
                File f = new File(URI.create(uri.toString()));
                f.delete();
                mAdapter.notifyItemRemoved(c);

                }
            c++;
        }
    }
    private  void shareSelectedImages()
    {
        share(this,getSelectedImages());
    }

    private List<ImageDetails> getSelectedImages()
    {

        List<ImageDetails> l=new ArrayList<>();
        for (Iterator<ImageDetails> it = list.iterator(); it.hasNext(); ) {
            ImageDetails i = it.next();
            if (i.isChecked()) {
                l.add(i);
            }
        }


        return l;
    }

    private ActionMode.Callback callActionMode =new ActionMode.Callback() {

            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items

                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.multiple_image_selector, menu);

                return true;
            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shere_:
                        //shareCurrentItem();
                        shareSelectedImages();
                        Log.d("Active Mode","sheared click");
                        listing_Enabled=false;
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    case R.id.delete_button_item:
                        deleteSelectedImages();
                        Log.d("Active Mode","delete click");
                        listing_Enabled=false;

                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                deSelectAll();
                listing_Enabled=false;
                actionMode= null;
            }
        };

    private void viewPdf(Uri Filename){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Filename, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


}

