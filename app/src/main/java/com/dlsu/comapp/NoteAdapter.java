package com.dlsu.comapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Enrico Zabayle on 09/03/2018.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>{

    protected List<Note> noteList;
    protected HomeActivity homeContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author, date;
        public EditText content;
        public ImageView menu;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.rv_notes_title);
            author = view.findViewById(R.id.rv_notes_author);
            content = view.findViewById(R.id.rv_notes_content);
            date = view.findViewById(R.id.rv_notes_date);
            menu = view.findViewById(R.id.rv_notes_menu);
        }
    }


    public NoteAdapter(List<Note> noteList, HomeActivity homeContext) {
        this.noteList = noteList;
        this.homeContext = homeContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.author.setText(note.getAuthor());
        holder.content.setText(note.getContent());
        holder.date.setText(note.getDate());

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menu, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeContext.viewNote(position);
            }
        });
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(homeContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.note_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete_note:
                    Toast.makeText(homeContext, "Note \"" + noteList.get(position).getTitle() + "\" has been deleted", Toast.LENGTH_SHORT).show();
                    noteList.remove(position);
                    notifyDataSetChanged();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
