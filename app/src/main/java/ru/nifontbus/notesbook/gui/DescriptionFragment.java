package ru.nifontbus.notesbook.gui;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.nifontbus.notesbook.R;
import ru.nifontbus.notesbook.data.CardData;

public class DescriptionFragment extends Fragment {

    public static final String ARG_NOTE = "TextCurrentNote";
    private CardData currentNote;

    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance(CardData note) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);

        if (getArguments() != null) {
            currentNote = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Таким способом можно получить головной элемент из макета
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        setHasOptionsMenu(true);
        // находим в контейнере элементы
        TextView tvName = view.findViewById(R.id.name_note);
        TextView tvText = view.findViewById(R.id.detail_note);
        TextView tvDate = view.findViewById(R.id.date_note);
        tvName.setText(currentNote.getTitle());
        tvText.setText(currentNote.getDescription());
        tvDate.setText(new SimpleDateFormat("dd-MM-yy").format(currentNote.getDate()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.description_menu, menu);
    }

    private void msg(String message) {
        Log.d("my", "msg: " + message);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}