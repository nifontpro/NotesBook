package ru.nifontbus.notesbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

public class CardEditFragment extends Fragment {

    private static final String ARG_ITEM_IDX = "EditorFragment.item_idx";
    private int mCurrentItemIdx = -1;
    private @DrawableRes
    int currentImageResourceId = -1;

    private CardData cardData;      // Данные по карточке
    private TextInputEditText title;
    private TextInputEditText description;
    private DatePicker datePicker;
    private AppCompatImageView image;
    private AppCompatSpinner spinner;

    // Для редактирования данных
    public static CardEditFragment newInstance(int currentItemIdx) {
        CardEditFragment fragment = new CardEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_IDX, currentItemIdx);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentItemIdx = getArguments().getInt(ARG_ITEM_IDX, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        initView(view);

        final CardsSource dataSource = CardsSourceImpl.getInstance(getResources());
        cardData = dataSource.getItemAt(mCurrentItemIdx);
        final MaterialButton btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener((v) -> {
            collectCardData();
            hideKeyboardFrom(getContext(), v);
            getFragmentManager().popBackStack();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                TypedArray imgs = getResources().obtainTypedArray(R.array.available_notes_imgs);
                currentImageResourceId = imgs.getResourceId(position, -1);
                image.setImageResource(currentImageResourceId);
                imgs.recycle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // если cardData пустая, то это добавление
        if (cardData != null) {
            populateView();
        }
        return view;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Сохранение данных
    private void collectCardData() {
        cardData.setTitle(this.title.getText().toString());
        cardData.setDescription(this.description.getText().toString());
        cardData.setDate(getDateFromDatePicker());
        cardData.setImageResourceId(this.currentImageResourceId);
    }

    // Получение даты из DatePicker
    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }

    private void initView(View view) {
        title = view.findViewById(R.id.inputTitle);
        description = view.findViewById(R.id.inputDescription);
        datePicker = view.findViewById(R.id.inputDate);
        image = view.findViewById(R.id.list_item_img);
        spinner = view.findViewById(R.id.note_spinner);
    }

    // Заполнение формы полученными данными
    @SuppressLint("NonConstantResourceId")
    private void populateView() {
        title.setText(cardData.getTitle());
        description.setText(cardData.getDescription());
        initDatePicker(cardData.getDate());

        int idRes = cardData.getImageResourceId();
        int pos = 0;
        switch (idRes) {
            case R.drawable.draw2:
                pos = 1;
                break;
            case R.drawable.draw3:
                pos = 2;
                break;
            case R.drawable.draw4:
                pos = 3;
                break;
        }
        spinner.setSelection(pos);

    }

    // Установка даты в DatePicker
    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }
}