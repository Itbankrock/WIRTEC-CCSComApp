package com.dlsu.comapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enrico Zabayle on 09/03/2018.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> implements Filterable{

    protected List<Note> noteList;
    protected List<Note> FileterednoteList;
    protected HomeActivity homeContext;
    private FirebaseUser fbCurrUser = FirebaseAuth.getInstance().getCurrentUser();

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
        this.FileterednoteList = noteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Note note = FileterednoteList.get(position);

        if(fbCurrUser.getUid().equals(note.getAuthor())){
            holder.menu.setVisibility(View.VISIBLE);
        }

        holder.title.setText(note.getTitle());
        DatabaseReference dbusers = FirebaseDatabase.getInstance().getReference("users/" + note.getAuthor());
        dbusers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.author.setText(dataSnapshot.child("name").getValue().toString().split(" ")[0]);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}});
        holder.author.setText(note.getAuthor().split(" ")[0]);
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
                homeContext.viewNote(note);
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
                    final Note thenote = FileterednoteList.get(position);

                    DatabaseReference dbcoursenotes = FirebaseDatabase.getInstance().getReference("courses/" + thenote.getCourseID() + "/notes");
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/" + thenote.getId() + "/", false);

                    dbcoursenotes.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(homeContext, "Note \"" + thenote.getTitle() + "\" has been deleted", Toast.LENGTH_SHORT).show();
                            FileterednoteList.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return FileterednoteList.size();
    }

    public void addItem(Note object){
        FileterednoteList.add(object);
        notifyDataSetChanged();
    }
    public void clearItems(){
        FileterednoteList.clear();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    FileterednoteList = noteList;
                } else {
                    List<Note> filteredList = new ArrayList<>();
                    for (Note row : noteList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    FileterednoteList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FileterednoteList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FileterednoteList = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
